/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.view.processdefinition;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import io.jmix.core.LoadContext;
import io.jmix.core.Messages;
import io.jmix.core.Metadata;
import io.jmix.core.Sort;
import io.jmix.flowui.DialogWindows;
import io.jmix.flowui.Dialogs;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.UiComponents;
import io.jmix.flowui.UiEventPublisher;
import io.jmix.flowui.ViewNavigators;
import io.jmix.flowui.component.codeeditor.CodeEditor;
import io.jmix.flowui.model.CollectionContainer;
import io.jmix.flowui.model.CollectionLoader;
import io.jmix.flowui.model.DataLoader;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.view.*;
import io.openbpm.control.entity.activity.ProcessActivityStatistics;
import io.openbpm.control.entity.dashboard.IncidentStatistics;
import io.openbpm.control.entity.filter.ProcessDefinitionFilter;
import io.openbpm.control.entity.filter.ProcessInstanceFilter;
import io.openbpm.control.entity.processdefinition.ProcessDefinitionData;
import io.openbpm.control.entity.processinstance.RuntimeProcessInstanceData;
import io.openbpm.control.service.activity.ActivityService;
import io.openbpm.control.service.processdefinition.ProcessDefinitionLoadContext;
import io.openbpm.control.service.processdefinition.ProcessDefinitionService;
import io.openbpm.control.service.processinstance.ProcessInstanceLoadContext;
import io.openbpm.control.service.processinstance.ProcessInstanceService;
import io.openbpm.control.view.event.TitleUpdateEvent;
import io.openbpm.control.view.processdefinition.event.ResetActivityEvent;
import io.openbpm.uikit.component.bpmnviewer.ViewerMode;
import io.openbpm.uikit.component.bpmnviewer.command.AddMarkerCmd;
import io.openbpm.uikit.component.bpmnviewer.command.ElementMarkerType;
import io.openbpm.uikit.component.bpmnviewer.command.RemoveMarkerCmd;
import io.openbpm.uikit.component.bpmnviewer.command.SetActivityStatisticsCmd;
import io.openbpm.uikit.component.bpmnviewer.event.ElementClickEvent;
import io.openbpm.uikit.fragment.bpmnviewer.BpmnViewerFragment;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
    protected CollectionContainer<RuntimeProcessInstanceData> processInstanceDataDc;
    @ViewComponent
    protected CollectionLoader<RuntimeProcessInstanceData> processInstanceDataDl;
    @ViewComponent
    protected InstanceContainer<ProcessInstanceFilter> processInstanceFilterDc;

    @ViewComponent
    protected ComboBox<ProcessDefinitionData> versionComboBox;

    @Autowired
    protected ProcessInstanceService processInstanceService;
    @Autowired
    protected ProcessDefinitionService processDefinitionService;
    @Autowired
    protected ActivityService activityService;

    @ViewComponent
    protected ProcessInstancesFragment processInstancesFragment;

    @ViewComponent
    protected BpmnViewerFragment viewerFragment;

    @ViewComponent
    protected CodeEditor bpmnXmlEditor;

    @ViewComponent
    protected Span allVersionsInstancesCountSpan;

    @ViewComponent
    protected GeneralPanelFragment generalPanel;

    @ViewComponent("tabsheet.processInstancesTab")
    protected Tab tabsheetProcessInstancesTab;

    @ViewComponent("tabsheet.bpmnXmlTab")
    protected Tab tabsheetBpmnXmlTab;

    @Subscribe
    public void onInit(final InitEvent event) {
        addClassNames(LumoUtility.Padding.Top.NONE);
        initTabIcons();

        ProcessInstanceFilter processInstanceFilter = metadata.create(ProcessInstanceFilter.class);
        processInstanceFilter.setUnfinished(true);
        processInstanceFilterDc.setItem(processInstanceFilter);
    }

    @Subscribe
    protected void onBeforeShow(BeforeShowEvent event) {
        initVersionLookup(getEditedEntity());

        updateInstancesCount();
        String processDefinitionBpmnXml = processDefinitionService.getBpmnXml(getEditedEntity().getProcessDefinitionId());

        viewerFragment.showStatisticsButton(true);
        initViewer(processDefinitionBpmnXml);
        generalPanel.refresh();
    }

    @Subscribe
    public void onReady(final ReadyEvent event) {
        sendUpdateViewTitleEvent();
    }

    @Subscribe(id = "processInstanceDataDc", target = Target.DATA_CONTAINER)
    public void onProcessInstanceDataDcCollectionChange(final CollectionContainer.CollectionChangeEvent<RuntimeProcessInstanceData> event) {
        updateInstancesCount();
    }

    @Override
    public String getPageTitle() {
        return title;
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
        tabsheetProcessInstancesTab.addComponentAsFirst(VaadinIcon.TASKS.create());
        tabsheetBpmnXmlTab.addComponentAsFirst(VaadinIcon.FILE_CODE.create());
    }


    @Subscribe("versionComboBox")
    protected void onVersionLookupValueChange(AbstractField.ComponentValueChangeEvent<ComboBox<ProcessDefinitionData>, ProcessDefinitionData> event) {
        ProcessDefinitionData selectedProcessDefinition = event.getValue();
        processDefinitionDataDc.setItem(selectedProcessDefinition);

        updateInstancesCount();
        generalPanel.refresh();

        sendUpdateViewTitleEvent();
    }

    @Subscribe(id = "processDefinitionDataDc", target = Target.DATA_CONTAINER)
    protected void onProcessDefinitionDcItemChange(InstanceContainer.ItemChangeEvent<ProcessDefinitionData> event) {
        ProcessDefinitionData processDefinition = event.getItem();


        String bpmnXml = "";
        if (processDefinition != null) {
            bpmnXml = processDefinitionService.getBpmnXml(processDefinition.getProcessDefinitionId());
        }

        initViewer(bpmnXml);
        bpmnXmlEditor.setValue(bpmnXml);

        if (processDefinition != null) {
            processInstanceFilterDc.getItem().setProcessDefinitionId(processDefinition.getProcessDefinitionId());
            processInstanceDataDl.load();
        }

        sendUpdateViewTitleEvent();
    }

    @Install(to = "processInstanceDataDl", target = Target.DATA_LOADER)
    protected List<RuntimeProcessInstanceData> processInstanceDataDlLoadDelegate(final LoadContext<RuntimeProcessInstanceData> loadContext) {
        ProcessInstanceFilter filter = processInstanceFilterDc.getItem();

        ProcessInstanceLoadContext context = new ProcessInstanceLoadContext().setFilter(filter)
                .setLoadIncidents(true);

        LoadContext.Query query = loadContext.getQuery();
        if (query != null) {
            context = context.setFirstResult(query.getFirstResult())
                    .setMaxResults(query.getMaxResults())
                    .setSort(query.getSort());
        }

        return processInstanceService.findAllRuntimeInstances(context);
    }

    @Install(to = "processDefinitionDataDl", target = Target.DATA_LOADER)
    protected ProcessDefinitionData loadProcessDefinition(LoadContext<ProcessDefinitionData> loadContext) {
        ProcessDefinitionData item = processDefinitionDataDc.getItemOrNull();
        String id = item == null ? Objects.requireNonNull(loadContext.getId()).toString() : item.getId();
        return processDefinitionService.getById(id);
    }

    @Subscribe(id = "processInstanceFilterDc", target = Target.DATA_CONTAINER)
    protected void onProcessInstanceFilterDcItemPropertyChange(final InstanceContainer.ItemPropertyChangeEvent<ProcessInstanceFilter> event) {
        if (event.getProperty().equals("activeActivityIdIn")) {
            updateCurrentVersionCount();
            processInstanceDataDl.load();
        }
    }

    @EventListener
    public void handleResetActivity(ResetActivityEvent resetActivityEvent) {
        String activityId = resetActivityEvent.getActivityId();
        viewerFragment.removeMarker(new RemoveMarkerCmd(activityId, ElementMarkerType.PRIMARY_COLOR_ACTIVITY));

        processInstanceFilterDc.getItem().setActiveActivityIdIn(null);
    }

    protected void initViewer(String bpmnXml) {
        viewerFragment.initViewer(bpmnXml);
        showStatistics();
    }

    protected void showStatistics() {
        List<ProcessActivityStatistics> processStatistics = activityService.getStatisticsByProcessId(getEditedEntity().getProcessDefinitionId());

        List<String> activeElements = new ArrayList<>();
        processStatistics.forEach(activityStatistics -> {
            Optional<Integer> totalIncidentCount = CollectionUtils.emptyIfNull(activityStatistics.getIncidents())
                    .stream()
                    .map(IncidentStatistics::getIncidentCount)
                    .filter(Objects::nonNull)
                    .reduce(Integer::sum);

            SetActivityStatisticsCmd cmd = new SetActivityStatisticsCmd(activityStatistics.getActivityId());
            cmd.setIncidentCount(totalIncidentCount.orElse(null));
            cmd.setInstanceCount(activityStatistics.getInstanceCount());

            viewerFragment.setActivityStatistics(cmd);
            activeElements.add(activityStatistics.getActivityId());
        });

        viewerFragment.setMode(ViewerMode.INTERACTIVE);
        viewerFragment.setActiveElements(activeElements);
        viewerFragment.addElementClickListener(this::handleDiagramElementClick);
    }

    protected void handleDiagramElementClick(ElementClickEvent elementClickEvent) {
        ProcessInstanceFilter instanceFilter = processInstanceFilterDc.getItem();

        List<String> activityIdIn = instanceFilter.getActiveActivityIdIn();
        String clickedElementId = elementClickEvent.getElementId();
        boolean sameElementClicked = false;

        if (CollectionUtils.isNotEmpty(activityIdIn)) {
            activityIdIn.forEach(activityId -> viewerFragment.removeMarker(new RemoveMarkerCmd(activityId, ElementMarkerType.PRIMARY_COLOR_ACTIVITY)));
            sameElementClicked = activityIdIn.contains(clickedElementId);
        }

        if (sameElementClicked) {
            instanceFilter.setActiveActivityIdIn(null);
            processInstancesFragment.clearActivity();
        } else {
            instanceFilter.setActiveActivityIdIn(List.of(clickedElementId));
            viewerFragment.addMarker(new AddMarkerCmd(clickedElementId, ElementMarkerType.PRIMARY_COLOR_ACTIVITY));
            processInstancesFragment.showActivity(clickedElementId, elementClickEvent.getElementType(),
                    elementClickEvent.getElementName());
        }
    }

    protected void updateInstancesCount() {
        ProcessDefinitionData item = processDefinitionDataDc.getItem();

        updateCurrentVersionCount();

        long allVersionsInstancesCount = processInstanceService.getCountByProcessDefinitionKey(item.getKey());
        allVersionsInstancesCountSpan.setText(String.valueOf(allVersionsInstancesCount));
    }

    protected void updateCurrentVersionCount() {
        long currentVersionInstancesCount = processInstanceService.getRuntimeInstancesCount(processInstanceFilterDc.getItem());
        updateTabCaption(currentVersionInstancesCount);
    }

    protected void updateTabCaption(long count) {
        tabsheetProcessInstancesTab.setLabel(messageBundle.formatMessage("processInstancesTab.label", count));
        tabsheetProcessInstancesTab.addComponentAsFirst(VaadinIcon.TASKS.create());
    }

    protected void initVersionLookup(ProcessDefinitionData processDefinition) {
        ProcessDefinitionFilter filter = metadata.create(ProcessDefinitionFilter.class);
        filter.setLatestVersionOnly(false);
        filter.setKey(processDefinition.getKey());

        List<ProcessDefinitionData> optionsList = processDefinitionService.findAll(new ProcessDefinitionLoadContext()
                .setFilter(filter)
                .setSort(Sort.by(Sort.Direction.DESC, "version")));
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
