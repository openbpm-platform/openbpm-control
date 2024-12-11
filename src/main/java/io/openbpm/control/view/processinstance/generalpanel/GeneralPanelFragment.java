/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.view.processinstance.generalpanel;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.theme.lumo.LumoUtility;
import io.jmix.core.Messages;
import io.jmix.core.Metadata;
import io.jmix.flowui.DialogWindows;
import io.jmix.flowui.Dialogs;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.ViewNavigators;
import io.jmix.flowui.action.DialogAction;
import io.jmix.flowui.component.checkbox.JmixCheckbox;
import io.jmix.flowui.component.formlayout.JmixFormLayout;
import io.jmix.flowui.component.textarea.JmixTextArea;
import io.jmix.flowui.fragment.Fragment;
import io.jmix.flowui.fragment.FragmentDescriptor;
import io.jmix.flowui.kit.action.ActionVariant;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.view.*;
import io.openbpm.control.entity.processdefinition.ProcessDefinitionData;
import io.openbpm.control.entity.processinstance.ProcessInstanceData;
import io.openbpm.control.entity.processinstance.ProcessInstanceState;
import io.openbpm.control.service.processdefinition.ProcessDefinitionService;
import io.openbpm.control.service.processinstance.ProcessInstanceService;
import io.openbpm.control.view.processinstance.ProcessInstanceDetailView;
import io.openbpm.control.view.processinstancemigration.ProcessInstanceMigrationView;
import org.springframework.beans.factory.annotation.Autowired;

import static io.jmix.flowui.component.UiComponentUtils.getCurrentView;

@FragmentDescriptor("general-panel-fragment.xml")
public class GeneralPanelFragment extends Fragment<FlexLayout> {

    @ViewComponent
    protected InstanceContainer<ProcessInstanceData> processInstanceDataDc;

    @Autowired
    protected Dialogs dialogs;
    @Autowired
    protected Notifications notifications;
    @Autowired
    protected ViewNavigators viewNavigators;
    @Autowired
    protected Metadata metadata;
    @ViewComponent
    protected MessageBundle messageBundle;
    @Autowired
    protected DialogWindows dialogWindows;
    @Autowired
    protected Messages messages;

    @Autowired
    protected ProcessInstanceService processInstanceService;
    @Autowired
    protected ProcessDefinitionService processDefinitionService;

    @ViewComponent
    protected JmixFormLayout processInstanceInfoGroupBox;
    @ViewComponent
    protected TextField processDefinitionField;
    @ViewComponent
    protected DateTimePicker endTimeField;
    @ViewComponent
    protected JmixTextArea deleteReason;

    @ViewComponent
    protected VerticalLayout upperPanel;
    @ViewComponent
    protected JmixButton infoBtn;
    @ViewComponent
    protected Button suspendBtn;
    @ViewComponent
    protected Button activateBtn;
    @ViewComponent
    protected VerticalLayout runtimeInstanceActions;
    @ViewComponent
    protected JmixCheckbox externallyTerminatedField;


    @Subscribe
    public void onReady(ReadyEvent event) {
        processInstanceInfoGroupBox.getComponents().forEach(component -> component.addClassNames(LumoUtility.Padding.Top.SMALL));
    }


    @Subscribe(target = Target.HOST_CONTROLLER)
    public void onHostBeforeShow(View.BeforeShowEvent event) {
        ProcessInstanceData processInstanceData = processInstanceDataDc.getItem();

        initProcessDefinitionField(processInstanceData);

        boolean hasEndTime = processInstanceData.getEndTime() != null;
        endTimeField.setVisible(hasEndTime);
        deleteReason.setVisible(hasEndTime);
        externallyTerminatedField.setVisible(hasEndTime);

        initActionButtons();
    }

    protected void initProcessDefinitionField(ProcessInstanceData processInstanceData) {
        String value;
        if (processInstanceData.getProcessDefinitionVersion() != null) {
            value = messages.formatMessage("", "common.processDefinitionKeyAndVersion",
                    processInstanceData.getProcessDefinitionKey(), processInstanceData.getProcessDefinitionVersion());
        } else {
            value = processInstanceData.getProcessDefinitionId();
        }
        processDefinitionField.setValue(value);
    }

    protected void initActionButtons() {
        ProcessInstanceData item = processInstanceDataDc.getItem();
        if (item.getState() == ProcessInstanceState.COMPLETED) {
            runtimeInstanceActions.setVisible(false);
        } else {
            boolean suspended = Boolean.TRUE.equals(processInstanceDataDc.getItem().getSuspended());
            activateBtn.setVisible(suspended);
            suspendBtn.setVisible(!suspended);
        }
    }

    @Subscribe("refreshBtn")
    public void reloadProcessInstance(ClickEvent<Button> event) {
        reopenProcessInstanceDetailsView();
    }

    @Subscribe("activateBtn")
    public void activateProcessInstance(ClickEvent<Button> event) {
        dialogs.createOptionDialog()
                .withHeader(messageBundle.getMessage("activateDialog.header"))
                .withText(messageBundle.getMessage("activateDialog.text"))
                .withActions(
                        new DialogAction(DialogAction.Type.YES)
                                .withIcon(VaadinIcon.PLAY)
                                .withText(messageBundle.getMessage("activate"))
                                .withVariant(ActionVariant.PRIMARY)
                                .withHandler(e -> {
                                    String processInstanceId = processInstanceDataDc.getItem().getId();
                                    processInstanceService.activateById(processInstanceId);

                                    notifications.create(messageBundle.getMessage("processInstanceActivated"))
                                            .withType(Notifications.Type.SUCCESS)
                                            .show();

                                    reopenProcessInstanceDetailsView();
                                }),
                        new DialogAction(DialogAction.Type.CANCEL)
                )
                .open();
    }

    @Subscribe("openProcessDefinitionEditorBtn")
    public void openProcessDefinitionEditor(ClickEvent<Button> event) {
        ProcessDefinitionData processDefinitionData = processDefinitionService.getById(processInstanceDataDc.getItem().getProcessDefinitionId());
        if (processDefinitionData != null) {
            viewNavigators.detailView(getCurrentView(), ProcessDefinitionData.class)
                    .withRouteParameters(new RouteParameters("id", processDefinitionData.getId()))
                    .withBackwardNavigation(true)
                    .navigate();
        } else {
            notifications.create(messageBundle.getMessage(("processDoesNotExist")))
                    .withType(Notifications.Type.WARNING)
                    .show();
        }
    }

    @Subscribe("suspendBtn")
    public void suspendProcessInstance(ClickEvent<Button> event) {
        dialogs.createOptionDialog()
                .withHeader(messageBundle.getMessage("suspendDialog.header"))
                .withText(messageBundle.getMessage("suspendDialog.text"))
                .withActions(
                        new DialogAction(DialogAction.Type.YES)
                                .withIcon(VaadinIcon.PAUSE)
                                .withText(messageBundle.getMessage("suspend"))
                                .withVariant(ActionVariant.PRIMARY)
                                .withHandler(e -> {
                                    String processInstanceId = processInstanceDataDc.getItem().getId();
                                    processInstanceService.suspendById(processInstanceId);

                                    notifications.create(messageBundle.getMessage("processInstanceSuspended"))
                                            .withType(Notifications.Type.SUCCESS)
                                            .show();
                                    reopenProcessInstanceDetailsView();
                                }),
                        new DialogAction(DialogAction.Type.CANCEL)
                )
                .open();
    }

    @Subscribe("terminateBtn")
    public void terminateProcessInstance(ClickEvent<Button> event) {
        dialogs.createOptionDialog()
                .withHeader(messageBundle.getMessage("terminateProcessInstanceView.title"))
                .withText(messageBundle.getMessage("terminateProcessInstanceMsg"))
                .withActions(
                        new DialogAction(DialogAction.Type.YES)
                                .withIcon(VaadinIcon.DOT_CIRCLE)
                                .withText(messages.getMessage("actions.Terminate"))
                                .withVariant(ActionVariant.PRIMARY)
                                .withHandler(e -> {
                                    String processInstanceId = processInstanceDataDc.getItem().getId();
                                    processInstanceService.terminateById(processInstanceId);

                                    notifications.create(messageBundle.getMessage("processInstanceTerminated"))
                                            .withType(Notifications.Type.SUCCESS)
                                            .show();
                                    reopenProcessInstanceDetailsView();
                                }),
                        new DialogAction(DialogAction.Type.CANCEL)
                )
                .open();
    }

    @Subscribe("migrateBtn")
    protected void onMigrateBtnClick(ClickEvent<Button> event) {
        ProcessDefinitionData processDefinitionData = metadata.create(ProcessDefinitionData.class);
        ProcessInstanceData processInstanceData = processInstanceDataDc.getItem();
        processDefinitionData.setId(processInstanceData.getProcessDefinitionId());
        processDefinitionData.setKey(processInstanceData.getProcessDefinitionKey());
        processDefinitionData.setVersion(String.valueOf(processInstanceData.getProcessDefinitionVersion()));

        DialogWindow<ProcessInstanceMigrationView> dialog =
                dialogWindows.view(getCurrentView(), ProcessInstanceMigrationView.class)
                        .withAfterCloseListener(afterCloseEvent -> {
                            if (afterCloseEvent.closedWith(StandardOutcome.SAVE)) {
                                notifications.create(messageBundle.getMessage("processInstanceMigrated"))
                                        .withType(Notifications.Type.SUCCESS)
                                        .show();

                                reopenProcessInstanceDetailsView();
                            }
                        })
                        .build();

        dialog.getView().setProcessDefinitionData(processDefinitionData);
        dialog.getView().setProcessInstanceData(processInstanceData);
        dialog.open();

    }


    @Subscribe("infoBtn")
    protected void onInfoButtonClickBtnClick(ClickEvent<Button> event) {
        upperPanel.setVisible(!upperPanel.isVisible());

        if (upperPanel.isVisible()) {
            infoBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            infoBtn.setTitle(messageBundle.getMessage("hideProcessInstanceDetails"));
        } else {
            infoBtn.removeThemeVariants(ButtonVariant.LUMO_PRIMARY);
            infoBtn.setTitle(messageBundle.getMessage("viewProcessInstanceDetails"));
        }

    }

    protected void reopenProcessInstanceDetailsView() {
        ProcessInstanceDetailView view = (ProcessInstanceDetailView) getCurrentView();
        view.reopenView();
    }
}
