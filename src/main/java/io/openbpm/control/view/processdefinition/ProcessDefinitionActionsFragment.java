/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.view.processdefinition;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;
import io.openbpm.control.entity.processdefinition.ProcessDefinitionData;
import io.openbpm.control.entity.processinstance.ProcessInstanceData;
import io.openbpm.control.service.processdefinition.ProcessDefinitionService;
import io.openbpm.control.view.processinstancemigration.ProcessInstanceMigrationView;
import io.openbpm.control.view.startprocess.StartProcessWithVariableView;
import io.jmix.core.Messages;
import io.jmix.flowui.DialogWindows;
import io.jmix.flowui.Dialogs;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.fragment.Fragment;
import io.jmix.flowui.fragment.FragmentDescriptor;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.kit.component.dropdownbutton.DropdownButton;
import io.jmix.flowui.kit.component.dropdownbutton.DropdownButtonItem;
import io.jmix.flowui.model.CollectionContainer;
import io.jmix.flowui.model.HasLoader;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

import static io.openbpm.control.view.processdefinition.ProcessDefinitionDetailView.REMOVE_PROCESS_DEFINITION_CLOSE_ACTION;
import static io.jmix.flowui.component.UiComponentUtils.getCurrentView;

@FragmentDescriptor("process-definition-actions-fragment.xml")
public class ProcessDefinitionActionsFragment extends Fragment<HorizontalLayout> {

    @Autowired
    protected DialogWindows dialogWindows;
    @Autowired
    protected Notifications notifications;
    @Autowired
    protected Messages messages;
    @Autowired
    protected Dialogs dialogs;
    @ViewComponent
    protected MessageBundle messageBundle;

    @ViewComponent
    protected InstanceContainer<ProcessDefinitionData> processDefinitionDataDc;
    @ViewComponent
    protected CollectionContainer<ProcessInstanceData> processInstanceDataDc;

    @ViewComponent
    protected HorizontalLayout buttonsGroup;
    @ViewComponent
    protected JmixButton startProcessBtn;
    @ViewComponent
    protected Button activateBtn;
    @ViewComponent
    protected DropdownButton suspendedVersionActionsDropdown;
    @ViewComponent
    protected DropdownButton activeVersionActionsDropdown;


    @Autowired
    protected ProcessDefinitionService processDefinitionService;

    @Subscribe
    public void onReady(ReadyEvent event) {
        buttonsGroup.addClassNames(LumoUtility.Position.STICKY, LumoUtility.Background.BASE,
                LumoUtility.Width.FULL, LumoUtility.Padding.Bottom.SMALL,
                LumoUtility.Padding.Top.SMALL);
    }

    public void updateButtonsVisibility() {
        ProcessDefinitionData item = processDefinitionDataDc.getItem();
        boolean suspended = Boolean.TRUE.equals(item.getSuspended());
        activateBtn.setVisible(suspended);
        if (suspended) {
            activateBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        } else {
            activateBtn.removeThemeVariants(ButtonVariant.LUMO_PRIMARY);
        }
        startProcessBtn.setVisible(!suspended);

        suspendedVersionActionsDropdown.setVisible(suspended);
        activeVersionActionsDropdown.setVisible(!suspended);
    }

    @Subscribe(id = "startProcessBtn", subject = "clickListener")
    public void onStartProcessBtnClick(final ClickEvent<JmixButton> event) {
        dialogWindows.detail(getView(), ProcessDefinitionData.class)
                .withViewClass(StartProcessWithVariableView.class)
                .editEntity(processDefinitionDataDc.getItem())
                .withAfterCloseListener(e -> {
                    if (e.closedWith(StandardOutcome.SAVE)) {
                        reloadProcessInstances();
                        notifications.create(messages.formatMessage(getClass(), "startProcess.success",
                                        e.getView().getEditedEntity().getProcessDefinitionId()))
                                .withType(Notifications.Type.SUCCESS)
                                .build()
                                .open();
                    }
                })
                .open();
    }

    @Subscribe("activateBtn")
    protected void onActivateBtnClick(ClickEvent<Button> event) {
        dialogWindows.view(getView(), ActivateProcessDefinitionView.class)
                .withAfterCloseListener(closeEvent -> {
                    if (closeEvent.closedWith(StandardOutcome.SAVE)) {
                        reloadProcessDefinition();
                        updateButtonsVisibility();
                    }
                })
                .withViewConfigurer(view -> view.setProcessDefinitionId(processDefinitionDataDc.getItem().getId()))
                .build()
                .open();
    }

    @Subscribe("deleteBtn")
    protected void onDeleteBtnClick(ClickEvent<Button> event) {
        dialogWindows.view(getView(), DeleteProcessDefinitionView.class)
                .withAfterCloseListener(closeEvent -> {
                    if (closeEvent.closedWith(StandardOutcome.SAVE)) {
                        getView().close(REMOVE_PROCESS_DEFINITION_CLOSE_ACTION);
                    }
                })
                .withViewConfigurer(view -> view.setProcessDefinitionId(processDefinitionDataDc.getItem().getId()))
                .build()
                .open();
    }

    @Subscribe("activeVersionActionsDropdown.suspend")
    public void onActiveVersionActionsDropdownSuspendClick(final DropdownButtonItem.ClickEvent event) {
        openSuspendConfirmDialog();
    }

    @Subscribe("activeVersionActionsDropdown.migrate")
    public void onActiveVersionActionsDropdownMigrateClick(final DropdownButtonItem.ClickEvent event) {
        openProcessInstanceMigrationDialog();
    }

    @Subscribe("suspendedVersionActionsDropdown.migrate")
    public void onSuspendedVersionActionsDropdownMigrateClick(final DropdownButtonItem.ClickEvent event) {
        openProcessInstanceMigrationDialog();
    }

    protected void openSuspendConfirmDialog() {
        dialogWindows.view(getView(), SuspendProcessDefinitionView.class)
                .withAfterCloseListener(closeEvent -> {
                    if (closeEvent.closedWith(StandardOutcome.SAVE)) {
                        reloadProcessDefinition();
                        updateButtonsVisibility();
                    }
                })
                .withViewConfigurer(view -> view.setProcessDefinitionId(processDefinitionDataDc.getItem().getId()))
                .build()
                .open();
    }

    protected void openProcessInstanceMigrationDialog() {
        dialogWindows.view(getView(), ProcessInstanceMigrationView.class)
                .withAfterCloseListener(afterCloseEvent -> {
                    if (afterCloseEvent.closedWith(StandardOutcome.SAVE)) {
                        reloadProcessInstances();
                        notifications.create(messageBundle.getMessage("processInstancesMigrationStarted"))
                                .withType(Notifications.Type.SUCCESS)
                                .show();
                    }
                })
                .withViewConfigurer(view -> view.setProcessDefinitionData(processDefinitionDataDc.getItem()))
                .build()
                .open();
    }

    protected void reloadProcessDefinition() {
        if (processDefinitionDataDc instanceof HasLoader container && container.getLoader() != null) {
            container.getLoader().load();
        }
    }

    protected void reloadProcessInstances() {
        if (processInstanceDataDc instanceof HasLoader container && container.getLoader() != null) {
            container.getLoader().load();
        }
    }

    protected View<?> getView() {
        return (View<?>) getParentController();
    }

    @Subscribe(id = "closeBtn", subject = "clickListener")
    public void onCloseBtnClick(final ClickEvent<JmixButton> event) {
        getCurrentView().close(StandardOutcome.CLOSE);
    }

}
