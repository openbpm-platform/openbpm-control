/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.view.processinstance;

import com.google.common.base.Strings;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.theme.lumo.LumoUtility;
import io.jmix.core.LoadContext;
import io.jmix.core.Messages;
import io.jmix.core.Metadata;
import io.jmix.flowui.Fragments;
import io.jmix.flowui.UiComponents;
import io.jmix.flowui.UiEventPublisher;
import io.jmix.flowui.ViewNavigators;
import io.jmix.flowui.component.tabsheet.JmixTabSheet;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.view.*;
import io.openbpm.control.dto.ActivityIncidentData;
import io.openbpm.control.entity.activity.ActivityShortData;
import io.openbpm.control.entity.decisioninstance.HistoricDecisionInstanceShortData;
import io.openbpm.control.entity.filter.DecisionInstanceFilter;
import io.openbpm.control.entity.processinstance.ProcessInstanceData;
import io.openbpm.control.entity.processinstance.ProcessInstanceState;
import io.openbpm.control.service.activity.ActivityService;
import io.openbpm.control.service.decisioninstance.DecisionInstanceLoadContext;
import io.openbpm.control.service.decisioninstance.DecisionInstanceService;
import io.openbpm.control.service.incident.IncidentService;
import io.openbpm.control.service.processdefinition.ProcessDefinitionService;
import io.openbpm.control.service.processinstance.ProcessInstanceService;
import io.openbpm.control.uicomponent.viewer.handler.CallActivityOverlayClickHandler;
import io.openbpm.control.view.decisioninstance.DecisionInstanceDetailView;
import io.openbpm.control.view.event.TitleUpdateEvent;
import io.openbpm.control.view.processinstance.event.ExternalTaskRetriesUpdateEvent;
import io.openbpm.control.view.processinstance.event.IncidentUpdateEvent;
import io.openbpm.control.view.processinstance.event.JobRetriesUpdateEvent;
import io.openbpm.control.view.processinstance.history.HistoryTabFragment;
import io.openbpm.control.view.util.ComponentHelper;
import io.openbpm.uikit.component.bpmnviewer.command.AddMarkerCmd;
import io.openbpm.uikit.component.bpmnviewer.command.ElementMarkerType;
import io.openbpm.uikit.component.bpmnviewer.command.SetElementColorCmd;
import io.openbpm.uikit.component.bpmnviewer.command.SetIncidentCountCmd;
import io.openbpm.uikit.component.bpmnviewer.command.ShowCalledInstanceOverlayCmd;
import io.openbpm.uikit.component.bpmnviewer.command.ShowDecisionInstanceLinkOverlayCmd;
import io.openbpm.uikit.fragment.bpmnviewer.BpmnViewerFragment;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Route(value = "bpm/process-instances/:id", layout = DefaultMainViewParent.class)
@ViewController("bpm_ProcessInstanceData.detail")
@ViewDescriptor("process-instance-detail-view.xml")
@EditedEntityContainer("processInstanceDataDc")
public class ProcessInstanceDetailView extends StandardDetailView<ProcessInstanceData> {
    public static final String HISTORY_TAB_ID = "historyTab";

    public static final int RUNTIME_TAB_IDX = 0;
    public static final int HISTORY_TAB_IDX = 1;

    @Autowired
    protected UiComponents uiComponents;
    @Autowired
    protected UiEventPublisher uiEventPublisher;
    @Autowired
    protected Fragments fragments;
    @Autowired
    protected ViewNavigators viewNavigators;
    @Autowired
    protected ProcessDefinitionService processDefinitionService;
    @Autowired
    protected ProcessInstanceService processInstanceService;
    @Autowired
    protected ActivityService activityService;
    @Autowired
    protected ComponentHelper componentHelper;
    @Autowired
    protected CallActivityOverlayClickHandler callActivityClickHandler;
    @Autowired
    protected Messages messages;
    @Autowired
    protected IncidentService incidentService;
    @Autowired
    protected DecisionInstanceService decisionInstanceService;
    @Autowired
    protected Metadata metadata;

    @ViewComponent
    protected JmixTabSheet relatedEntitiesTabSheet;
    @ViewComponent
    protected InstanceContainer<ProcessInstanceData> processInstanceDataDc;
    @ViewComponent
    protected BpmnViewerFragment diagramFragment;
    @ViewComponent
    protected VerticalLayout emptyDiagramBox;
    @ViewComponent
    protected MessageBundle messageBundle;

    protected String title = "";

    @Subscribe
    public void onInit(final InitEvent event) {
        relatedEntitiesTabSheet.getTabAt(RUNTIME_TAB_IDX).addComponentAsFirst(VaadinIcon.FILE_TREE_SMALL.create());

        Tab historyTab = uiComponents.create(Tab.class);
        historyTab.setId(HISTORY_TAB_ID);
        historyTab.setLabel(messageBundle.getMessage("historyTabCaption"));
        historyTab.addComponentAsFirst(VaadinIcon.TIME_BACKWARD.create());
        relatedEntitiesTabSheet.add(historyTab,
                new LazyTabContent(() -> fragments.create(this, HistoryTabFragment.class)), HISTORY_TAB_IDX);
    }

    @SuppressWarnings("JmixIncorrectCreateGuiComponent")
    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        ProcessInstanceData processInstanceData = processInstanceDataDc.getItem();
        if (processInstanceData.getEndTime() != null) {
            relatedEntitiesTabSheet.getTabAt(RUNTIME_TAB_IDX).setEnabled(false);
            Tab historyTab = relatedEntitiesTabSheet.getTabAt(HISTORY_TAB_IDX);
            relatedEntitiesTabSheet.setSelectedTab(historyTab);
            //force init a tab content because attach event is not triggered
            LazyTabContent contentByTab = (LazyTabContent) relatedEntitiesTabSheet.getContentByTab(historyTab);
            if (contentByTab != null) {
                contentByTab.init();
                Component tabContent = contentByTab.getChildren().findFirst().orElse(null);
                if (tabContent instanceof HistoryTabFragment historyTabFragment) {
                    historyTabFragment.refresh();
                }
            }
        }
        initBpmnViewerFragment();
    }

    @Subscribe
    public void onReady(final ReadyEvent event) {
        sendUpdateViewTitleEvent();
    }

    @Subscribe("relatedEntitiesTabSheet")
    public void onRelatedEntitiesTabSheetSelectedChange(final JmixTabSheet.SelectedChangeEvent event) {
        Tab selectedTab = event.getSelectedTab();
        String selectedTabId = selectedTab != null ? selectedTab.getId().orElse(null) : null;
        if (StringUtils.equals(selectedTabId, HISTORY_TAB_ID)) {
            Component tabContent = getTabContent(selectedTab);
            if (tabContent instanceof HistoryTabFragment historyTabFragment) {
                historyTabFragment.refresh();
            }
        }
    }

    @EventListener
    public void handleIncidentUpdate(IncidentUpdateEvent event) {
        initBpmnViewerFragment();
    }

    @EventListener
    public void handleJobRetriesUpdate(JobRetriesUpdateEvent event) {
        initBpmnViewerFragment();
    }

    @EventListener
    public void handleExternalRetriesUpdate(ExternalTaskRetriesUpdateEvent event) {
        initBpmnViewerFragment();
    }

    public void reopenView() {
        String instanceId = processInstanceDataDc.getItem().getInstanceId();
        close(StandardOutcome.DISCARD).then(() -> viewNavigators.view(this, ProcessInstanceDetailView.class)
                .withRouteParameters(new RouteParameters("id", instanceId))
                .withBackwardNavigation(false)
                .navigate());
    }

    @Override
    public String getPageTitle() {
        return title;
    }

    @Install(to = "processInstanceLoader", target = Target.DATA_LOADER)
    protected ProcessInstanceData processInstanceLoaderLoadDelegate(final LoadContext<ProcessInstanceData> loadContext) {
        return processInstanceService.getProcessInstanceById(Objects.requireNonNull(loadContext.getId()).toString());
    }

    protected void initBpmnViewerFragment() {
        String processBpmnXml = processDefinitionService.getBpmnXml
                (processInstanceDataDc.getItem().getProcessDefinitionId());
        if (!Strings.isNullOrEmpty(processBpmnXml)) {
            emptyDiagramBox.setVisible(false);
            diagramFragment.initViewer(processBpmnXml);
            diagramFragment.addImportCompleteListener(event -> handleProcessXmlImportComplete());
            diagramFragment.addDecisionInstanceLinkOverlayClickListener(
                    event -> handleDecisionInstanceLinkOverlayClicked(event.getDecisionInstanceId()));
            diagramFragment.addCalledProcessInstanceOverlayClickListener(event ->
                    callActivityClickHandler.handleInstancesNavigation(event.getProcessInstanceIds()));
        } else if (processBpmnXml == null) {
            emptyDiagramBox.setVisible(true);
            diagramFragment.setVisible(false);
        }
    }

    protected void handleProcessXmlImportComplete() {
        ProcessInstanceData processInstanceData = processInstanceDataDc.getItem();
        String processInstanceId = processInstanceData.getInstanceId();

        showRunningActivities(processInstanceId);
        showFinishedActivities(processInstanceId);

        if (processInstanceData.getState() != ProcessInstanceState.COMPLETED) {
            List<ActivityIncidentData> incidents = incidentService.findRuntimeIncidents(processInstanceId);
            diagramFragment.setIncidentCount(new SetIncidentCountCmd(incidents));
        }
    }

    protected void showFinishedActivities(String processInstanceId) {
        List<ActivityShortData> finishedActivities = activityService.findFinishedActivities(processInstanceId);

        Map<String, List<String>> calledInstancesByActivityId = new HashMap<>();
        for (ActivityShortData activityData : finishedActivities) {
            String activityId = activityData.getActivityId();
            if (!Strings.isNullOrEmpty(activityId)) {
                diagramFragment.setElementColor(new SetElementColorCmd(activityId, "#000000", "var(--bpmn-history-activity-color)"));
            }

            String decisionInstanceId = findDecisionInstanceByActivity(activityId);
            if (!Strings.isNullOrEmpty(activityId)) {
                String tooltipMessage = messages.formatMessage(
                        "", "viewer.openDecisionInstanceOverlay.tooltipMessage", decisionInstanceId);
                diagramFragment.showDecisionInstanceLinkOverlay(new ShowDecisionInstanceLinkOverlayCmd(activityId,
                        decisionInstanceId, tooltipMessage));
            }

            addCalledInstance(activityData, calledInstancesByActivityId);
        }

        showCalledInstanceOverlays(calledInstancesByActivityId);
    }

    protected void showRunningActivities(String processInstanceId) {
        List<ActivityShortData> runningActivities = activityService.findRunningActivities(processInstanceId);

        Map<String, List<String>> calledInstances = new HashMap<>();
        for (ActivityShortData activityData : runningActivities) {
            String activityId = activityData.getActivityId();
            if (!Strings.isNullOrEmpty(activityId)) {
                diagramFragment.addMarker(new AddMarkerCmd(activityId, ElementMarkerType.RUNNING_ACTIVITY));
            }
            addCalledInstance(activityData, calledInstances);
        }

        showCalledInstanceOverlays(calledInstances);
    }

    protected void addCalledInstance(ActivityShortData activityData, Map<String, List<String>> calledInstancesByActivityId) {
        if (StringUtils.isNotEmpty(activityData.getCalledProcessInstanceId())) {
            List<String> activityCalledInstances
                    = calledInstancesByActivityId.getOrDefault(activityData.getActivityId(), new ArrayList<>());

            activityCalledInstances.add(activityData.getCalledProcessInstanceId());
            calledInstancesByActivityId.put(activityData.getActivityId(), activityCalledInstances);
        }
    }

    protected void showCalledInstanceOverlays(Map<String, List<String>> calledInstances) {
        calledInstances.forEach((activityId, calledInstanceIds) -> {
            ShowCalledInstanceOverlayCmd showCalledInstanceOverlayCmd = new ShowCalledInstanceOverlayCmd();
            showCalledInstanceOverlayCmd.setElementId(activityId);
            showCalledInstanceOverlayCmd.setProcessInstanceIds(calledInstanceIds);

            diagramFragment.showCalledInstance(showCalledInstanceOverlayCmd);
        });
    }

    protected void sendUpdateViewTitleEvent() {
        this.title = messageBundle.formatMessage("processInstanceDetailView.title", getEditedEntity().getInstanceId());

        String titleText = messageBundle.getMessage("processInstanceDetailView.baseTitle");
        FlexLayout titleLayout = createTitleLayout();

        uiEventPublisher.publishEventForCurrentUI(new TitleUpdateEvent(this, titleText, titleLayout));
    }

    protected FlexLayout createTitleLayout() {
        FlexLayout flexLayout = uiComponents.create(FlexLayout.class);
        flexLayout.addClassNames(LumoUtility.Margin.Left.XSMALL, LumoUtility.Gap.SMALL);
        flexLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        H5 instanceId = createInstanceIdComponent();

        Span processDefinitionBadge = createProcessBadge();

        Span stateBadge = componentHelper.createProcessInstanceStateBadge(getEditedEntity().getState());
        flexLayout.add(instanceId, stateBadge, processDefinitionBadge);
        return flexLayout;
    }

    protected H5 createInstanceIdComponent() {
        H5 instanceId = new H5("\"%s\"".formatted(getEditedEntity().getInstanceId()));
        instanceId.setHeightFull();
        instanceId.addClassNames(LumoUtility.TextColor.BODY);
        return instanceId;
    }

    protected Span createProcessBadge() {
        Span processDefinitionBadge = uiComponents.create(Span.class);
        processDefinitionBadge.getElement().getThemeList().add("badge normal pill");

        Integer processVersion = getEditedEntity().getProcessDefinitionVersion();
        String processKey = getEditedEntity().getProcessDefinitionKey();

        String processBadgeText = processVersion == null ? processKey :
                messages.formatMessage("", "common.processDefinitionKeyAndVersion", processKey, processVersion);
        processDefinitionBadge.setText(processBadgeText);

        return processDefinitionBadge;
    }

    @Nullable
    protected Component getTabContent(Tab tab) {
        Component contentByTab = relatedEntitiesTabSheet.getContentByTab(tab);
        return contentByTab != null
                ? contentByTab
                .getChildren()
                .findFirst()
                .orElse(null)
                : null;
    }

    private void handleDecisionInstanceLinkOverlayClicked(String decisionInstanceId) {
        if (!Strings.isNullOrEmpty(decisionInstanceId)) {
            viewNavigators.detailView(this, HistoricDecisionInstanceShortData.class)
                    .withViewClass(DecisionInstanceDetailView.class)
                    .withRouteParameters(new RouteParameters("id", decisionInstanceId))
                    .withBackwardNavigation(true)
                    .navigate();
        }
    }

    private String findDecisionInstanceByActivity(String activityId) {
        DecisionInstanceLoadContext loadContext = new DecisionInstanceLoadContext();
        DecisionInstanceFilter filter = metadata.create(DecisionInstanceFilter.class);
        loadContext.setFilter(filter);

        filter.setActivityId(activityId);
        filter.setProcessInstanceId(processInstanceDataDc.getItem().getInstanceId());
        List<HistoricDecisionInstanceShortData> allHistoryDecisionInstances =
                decisionInstanceService.findAllHistoryDecisionInstances(loadContext);
        if (!allHistoryDecisionInstances.isEmpty()) {
            return allHistoryDecisionInstances.getFirst().getDecisionInstanceId();
        }
        return null;
    }
}