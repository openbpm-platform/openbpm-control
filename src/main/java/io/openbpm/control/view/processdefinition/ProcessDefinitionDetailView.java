/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.view.processdefinition;

import com.google.common.base.Strings;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.theme.lumo.LumoUtility;
import io.jmix.core.LoadContext;
import io.jmix.core.Messages;
import io.jmix.core.Metadata;
import io.jmix.flowui.*;
import io.jmix.flowui.component.codeeditor.CodeEditor;
import io.jmix.flowui.component.datetimepicker.TypedDateTimePicker;
import io.jmix.flowui.component.formlayout.JmixFormLayout;
import io.jmix.flowui.component.tabsheet.JmixTabSheet;
import io.jmix.flowui.component.textfield.TypedTextField;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.model.CollectionContainer;
import io.jmix.flowui.model.CollectionLoader;
import io.jmix.flowui.model.DataLoader;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.view.*;
import io.openbpm.control.entity.deployment.DeploymentData;
import io.openbpm.control.entity.filter.ProcessInstanceFilter;
import io.openbpm.control.entity.processdefinition.ProcessDefinitionData;
import io.openbpm.control.entity.processinstance.ProcessInstanceData;
import io.openbpm.control.service.deployment.DeploymentService;
import io.openbpm.control.service.processdefinition.ProcessDefinitionService;
import io.openbpm.control.service.processinstance.ProcessInstanceLoadContext;
import io.openbpm.control.service.processinstance.ProcessInstanceService;
import io.openbpm.control.view.deploymentdata.DeploymentDetailView;
import io.openbpm.control.view.event.TitleUpdateEvent;
import io.openbpm.uikit.fragment.bpmnviewer.BpmnViewerFragment;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

import static io.openbpm.control.view.util.JsUtils.COPY_SCRIPT_TEXT;

@Route(value = "bpm/process-definitions/:id", layout = DefaultMainViewParent.class)
@ViewController("bpm_ProcessDefinition.detail")
@ViewDescriptor("process-definition-detail-view.xml")
@EditedEntityContainer("processDefinitionDataDc")
@DialogMode(width = "50em", height = "37.5em")
@PrimaryDetailView(ProcessDefinitionData.class)
public class ProcessDefinitionDetailView extends StandardDetailView<ProcessDefinitionData> {
    public static final CloseAction REMOVE_PROCESS_DEFINITION_CLOSE_ACTION = new StandardCloseAction("removeProcessDefinition");

    protected String title = "";

    @Autowired
    protected Dialogs dialogs;
    @Autowired
    protected DialogWindows dialogWindows;
    @ViewComponent
    protected MessageBundle messageBundle;
    @Autowired
    protected ViewNavigators viewNavigators;
    @Autowired
    protected Notifications notifications;
    @Autowired
    protected Messages messages;
    @Autowired
    protected UiComponents uiComponents;
    @Autowired
    protected UiEventPublisher uiEventPublisher;
    @Autowired
    protected Metadata metadata;

    @ViewComponent
    protected InstanceContainer<ProcessDefinitionData> processDefinitionDataDc;
    @ViewComponent
    protected DataLoader processDefinitionDataDl;

    @ViewComponent
    protected CollectionContainer<ProcessInstanceData> processInstanceDataDc;
    @ViewComponent
    protected CollectionLoader<ProcessInstanceData> processInstanceDataDl;


    @ViewComponent
    protected ComboBox<ProcessDefinitionData> versionComboBox;

    @Autowired
    protected ProcessInstanceService processInstanceService;
    @Autowired
    protected ProcessDefinitionService processDefinitionService;

    @ViewComponent
    protected JmixTabSheet tabsheet;

    @ViewComponent
    protected JmixFormLayout processDefinitionForm;
    @ViewComponent
    protected HorizontalLayout keyAndVersionHBox;
    @ViewComponent
    protected TypedTextField<String> keyField;
    @ViewComponent
    protected TypedTextField<String> idField;
    @ViewComponent
    protected ProcessInstancesFragment processInstancesFragment;

    @ViewComponent
    protected BpmnViewerFragment viewerFragment;

    @ViewComponent
    protected CodeEditor bpmnXmlEditor;

    @ViewComponent
    protected ProcessDefinitionActionsFragment actionsFragment;
    @Autowired
    protected DeploymentService deploymentService;
    @ViewComponent
    protected TypedTextField<String> deploymentSourceField;
    @ViewComponent
    protected TypedDateTimePicker<OffsetDateTime> deploymentTimeField;
    @ViewComponent
    private TypedTextField<Object> deploymentIdField;

    @Subscribe
    public void onInit(final InitEvent event) {
        addClassNames(LumoUtility.Padding.Top.NONE);
        initTabIcons();
        initProcessDefinitionFormStyles();
    }

    @Subscribe
    protected void onBeforeShow(BeforeShowEvent event) {
        initVersionLookup(getEditedEntity());
        actionsFragment.updateButtonsVisibility();
        processInstancesFragment.initInstancesCountLabels();
        String processDefinitionBpmnXml = processDefinitionService.getBpmnXml(
                getEditedEntity().getProcessDefinitionId());
        viewerFragment.initViewer(processDefinitionBpmnXml);

        initDeploymentData();
    }

    @Subscribe
    public void onReady(final ReadyEvent event) {
        sendUpdateViewTitleEvent();
    }

    @Subscribe(id = "processInstanceDataDc", target = Target.DATA_CONTAINER)
    public void onProcessInstanceDataDcCollectionChange(final CollectionContainer.CollectionChangeEvent<ProcessInstanceData> event) {
        processInstancesFragment.initInstancesCountLabels();
    }

    @Override
    public String getPageTitle() {
        return title;
    }

    @Subscribe(id = "viewDeployment", subject = "clickListener")
    public void onViewDeploymentClick(final ClickEvent<JmixButton> event) {
        viewNavigators.detailView(this, DeploymentData.class)
                .withViewClass(DeploymentDetailView.class)
                .withRouteParameters(new RouteParameters("id", getEditedEntity().getDeploymentId()))
                .withBackwardNavigation(true)
                .navigate();
    }

    protected void sendUpdateViewTitleEvent() {
        this.title = messageBundle.formatMessage("processDefinitionDetail.processName", getEditedEntity().getKey());

        FlexLayout flexLayout = uiComponents.create(FlexLayout.class);
        flexLayout.addClassNames(LumoUtility.Margin.Left.XSMALL, LumoUtility.Gap.SMALL);

        Span version = uiComponents.create(Span.class);
        version.addClassNames(LumoUtility.TextColor.SECONDARY, LumoUtility.FontWeight.BOLD);
        version.setText(messageBundle.formatMessage("processDefinitionDetail.processVersion", getEditedEntity().getVersion()));

        boolean active = BooleanUtils.isNotTrue(getEditedEntity().getSuspended());
        String text = active ? messageBundle.getMessage("active") : messageBundle.getMessage("suspended");
        Span badge = createStateBadge(active, text);

        flexLayout.add(version, badge);

        uiEventPublisher.publishEventForCurrentUI(new TitleUpdateEvent(this, title, flexLayout));
    }

    protected void initTabIcons() {
        tabsheet.getTabAt(0).addComponentAsFirst(VaadinIcon.INFO_CIRCLE_O.create());
        tabsheet.getTabAt(1).addComponentAsFirst(VaadinIcon.SITEMAP.create());
        tabsheet.getTabAt(2).addComponentAsFirst(VaadinIcon.FILE_CODE.create());
    }

    protected void initProcessDefinitionFormStyles() {
        processDefinitionForm.getOwnComponents().forEach(component -> component.addClassName(LumoUtility.Padding.Top.SMALL));
        keyAndVersionHBox.getChildren().forEach(component -> component.addClassName(LumoUtility.Padding.Top.NONE));
        processDefinitionForm.addClassNames(LumoUtility.Padding.Left.SMALL, LumoUtility.Padding.Right.SMALL);
    }


    @Subscribe("versionComboBox")
    protected void onVersionLookupValueChange(AbstractField.ComponentValueChangeEvent<ComboBox<ProcessDefinitionData>, ProcessDefinitionData> event) {
        ProcessDefinitionData selectedProcessDefinition = event.getValue();
        processDefinitionDataDc.setItem(selectedProcessDefinition);
        processInstancesFragment.initInstancesCountLabels();
        actionsFragment.updateButtonsVisibility();
        sendUpdateViewTitleEvent();
        initDeploymentData();
    }

    @Subscribe(id = "processDefinitionDataDc", target = Target.DATA_CONTAINER)
    protected void onProcessDefinitionDcItemChange(InstanceContainer.ItemChangeEvent<ProcessDefinitionData> event) {
        ProcessDefinitionData processDefinition = event.getItem();


        String bpmnXml = "";
        if (processDefinition != null) {
            bpmnXml = processDefinitionService.getBpmnXml(processDefinition.getProcessDefinitionId());
        }

        viewerFragment.initViewer(bpmnXml);
        bpmnXmlEditor.setValue(bpmnXml);

        processInstanceDataDl.load();

        sendUpdateViewTitleEvent();
    }

    @Install(to = "processInstanceDataDl", target = Target.DATA_LOADER)
    protected List<ProcessInstanceData> processInstanceDataDlLoadDelegate(final LoadContext<ProcessInstanceData> loadContext) {
        ProcessInstanceFilter filter = metadata.create(ProcessInstanceFilter.class);
        filter.setProcessDefinitionId(processDefinitionDataDc.getItem().getId());
        filter.setUnfinished(true);

        ProcessInstanceLoadContext context = new ProcessInstanceLoadContext().setFilter(filter);

        LoadContext.Query query = loadContext.getQuery();
        if (query != null) {
            context = context.setFirstResult(query.getFirstResult())
                    .setMaxResults(query.getMaxResults())
                    .setSort(query.getSort());
        }

        return processInstanceService.findAllHistoricInstances(context);
    }

    @Install(to = "processDefinitionDataDl", target = Target.DATA_LOADER)
    protected ProcessDefinitionData loadProcessDefinition(LoadContext<ProcessDefinitionData> loadContext) {
        ProcessDefinitionData item = processDefinitionDataDc.getItemOrNull();
        String id = item == null ? Objects.requireNonNull(loadContext.getId()).toString() : item.getId();
        return processDefinitionService.getById(id);
    }

    @Subscribe(id = "copyIdButton", subject = "clickListener")
    public void onCopyIdButtonClick(final ClickEvent<JmixButton> event) {
        Element buttonElement = event.getSource().getElement();
        String idToCopy = Strings.nullToEmpty(idField.getTypedValue());
        buttonElement.executeJs(COPY_SCRIPT_TEXT, idToCopy)
                .then(successResult -> notifications.create(messageBundle.getMessage("idCopied"))
                                .withPosition(Notification.Position.TOP_END)
                                .withThemeVariant(NotificationVariant.LUMO_SUCCESS)
                                .show(),
                        errorResult -> notifications.create(messageBundle.getMessage("idCopyFailed"))
                                .withPosition(Notification.Position.TOP_END)
                                .withThemeVariant(NotificationVariant.LUMO_ERROR)
                                .show());
    }

    @Subscribe(id = "copyKeyButton", subject = "clickListener")
    public void onCopyKeyButtonClick(final ClickEvent<JmixButton> event) {
        Element buttonElement = event.getSource().getElement();
        String keyToCopy = Strings.nullToEmpty(keyField.getTypedValue());
        buttonElement.executeJs(COPY_SCRIPT_TEXT, keyToCopy)
                .then(successResult -> notifications.create(messageBundle.getMessage("keyCopied"))
                                .withPosition(Notification.Position.TOP_END)
                                .withThemeVariant(NotificationVariant.LUMO_SUCCESS)
                                .show(),
                        errorResult -> notifications.create(messageBundle.getMessage("keyCopyFailed"))
                                .withPosition(Notification.Position.TOP_END)
                                .withThemeVariant(NotificationVariant.LUMO_ERROR)
                                .show());
    }

    protected void initDeploymentData() {
        DeploymentData deployment = deploymentService.findById(getEditedEntity().getDeploymentId());
        if (deployment != null) {
            deploymentIdField.setTypedValue(deployment.getDeploymentId());
            deploymentSourceField.setTypedValue(deployment.getSource());
            deploymentTimeField.setTypedValue(deployment.getDeploymentTime());
        }
    }

    protected void initVersionLookup(ProcessDefinitionData processDefinition) {
        List<ProcessDefinitionData> optionsList = processDefinitionService.findAllByKey(processDefinition.getKey());
        versionComboBox.setItems(optionsList);
        versionComboBox.setItemLabelGenerator(item -> item.getVersion() != null ? String.valueOf(item.getVersion()) : null);
        versionComboBox.setValue(processDefinition);
    }

    protected Span createStateBadge(boolean success, String text) {
        Span badge = uiComponents.create(Span.class);
        String themeNames = success ? "badge success pill" : "badge warning pill";
        badge.getElement().getThemeList().add(themeNames);

        badge.setText(text);
        return badge;
    }
}
