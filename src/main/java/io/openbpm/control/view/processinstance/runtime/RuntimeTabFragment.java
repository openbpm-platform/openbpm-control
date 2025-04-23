/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.view.processinstance.runtime;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.data.event.SortEvent;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.theme.lumo.LumoUtility;
import io.jmix.core.DataLoadContext;
import io.jmix.core.LoadContext;
import io.jmix.core.Metadata;
import io.jmix.flowui.DialogWindows;
import io.jmix.flowui.Fragments;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.component.grid.TreeDataGrid;
import io.jmix.flowui.component.tabsheet.JmixTabSheet;
import io.jmix.flowui.fragment.Fragment;
import io.jmix.flowui.fragment.FragmentDescriptor;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.model.CollectionContainer;
import io.jmix.flowui.model.CollectionLoader;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.view.*;
import io.openbpm.control.entity.activity.ActivityInstanceTreeItem;
import io.openbpm.control.entity.filter.*;
import io.openbpm.control.entity.processinstance.ProcessInstanceData;
import io.openbpm.control.entity.processinstance.ProcessInstanceState;
import io.openbpm.control.entity.variable.VariableInstanceData;
import io.openbpm.control.service.activity.ActivityService;
import io.openbpm.control.service.externaltask.ExternalTaskService;
import io.openbpm.control.service.incident.IncidentService;
import io.openbpm.control.service.job.JobService;
import io.openbpm.control.service.usertask.UserTaskService;
import io.openbpm.control.service.variable.VariableLoadContext;
import io.openbpm.control.service.variable.VariableService;
import io.openbpm.control.view.processinstance.LazyTabContent;
import io.openbpm.control.view.processinstance.event.*;
import io.openbpm.control.view.processvariable.VariableInstanceDataDetail;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.lang.Nullable;

import java.util.List;

import static io.jmix.flowui.component.UiComponentUtils.getCurrentView;

@FragmentDescriptor("runtime-tab-fragment.xml")
public class RuntimeTabFragment extends Fragment<HorizontalLayout> {
    public static final String USER_TASKS_TAB_ID = "userTasksTab";
    public static final String JOBS_TAB_ID = "jobsTab";
    public static final String EXTERNAL_TASKS_TAB_ID = "externalTasksTab";
    public static final String INCIDENTS_TAB_ID = "incidentsTab";

    public static final int VARIABLES_TAB_IDX = 0;
    public static final int USER_TASKS_TAB_IDX = 1;
    public static final int JOBS_TAB_IDX = 2;
    public static final int EXTERNAL_TASKS_TAB_IDX = 3;
    public static final int INCIDENTS_TAB_IDX = 4;

    @Autowired
    protected Metadata metadata;
    @Autowired
    protected Fragments fragments;

    @ViewComponent
    protected MessageBundle messageBundle;
    @Autowired
    protected DialogWindows dialogWindows;

    @Autowired
    protected ActivityService activityService;
    @Autowired
    protected VariableService variableService;
    @Autowired
    protected UserTaskService userTaskService;
    @Autowired
    protected ExternalTaskService externalTaskService;
    @Autowired
    protected JobService jobService;

    @ViewComponent
    protected CollectionLoader<ActivityInstanceTreeItem> runtimeActivityInstancesDl;
    @ViewComponent
    protected CollectionContainer<ActivityInstanceTreeItem> runtimeActivityInstancesDc;
    @ViewComponent
    protected InstanceContainer<ProcessInstanceData> processInstanceDataDc;
    @ViewComponent
    protected CollectionLoader<VariableInstanceData> runtimeVariablesDl;
    @ViewComponent
    protected DataGrid<VariableInstanceData> runtimeVariablesGrid;

    @ViewComponent
    protected TreeDataGrid<ActivityInstanceTreeItem> activityInstancesTree;

    @ViewComponent
    protected JmixTabSheet runtimeTabsheet;
    @ViewComponent
    protected VerticalLayout activityTreeContainer;

    protected VariableFilter variableFilter;
    @Autowired
    protected IncidentService incidentService;

    @Subscribe
    public void onReady(ReadyEvent event) {
        activityTreeContainer.addClassNames(LumoUtility.Padding.Top.SMALL, LumoUtility.Padding.Right.SMALL);
    }

    @Subscribe(target = Target.HOST_CONTROLLER)
    public void onHostBeforeShow(View.BeforeShowEvent event) {
        ProcessInstanceData item = processInstanceDataDc.getItem();
        if (item.getState() != ProcessInstanceState.COMPLETED) {
            this.variableFilter = metadata.create(VariableFilter.class);
            this.variableFilter.setProcessInstanceId(item.getInstanceId());

            updateVariablesTabCaption(VARIABLES_TAB_IDX);
            initUserTasksTab();
            initJobsTab();
            initExternalTasksTab();
            initIncidentsTab();

            runtimeActivityInstancesDl.load();
            activityInstancesTree.expand(runtimeActivityInstancesDc.getItems());
            runtimeVariablesDl.load();

            loadAndUpdateVariablesCount();
            loadAndUpdateUserTasksCount();
            loadAndUpdateJobsCount();
            loadAndUpdateExternalTasksCount();
            loadAndUpdateIncidentsCount();
        }
    }

    protected void initIncidentsTab() {
        Tab incidentsTab = createTab(INCIDENTS_TAB_ID, "incidentsTabCaption", VaadinIcon.WARNING);
        runtimeTabsheet.add(incidentsTab, new LazyTabContent(this::createIncidentsFragment), INCIDENTS_TAB_IDX);
    }

    @Subscribe("runtimeTabsheet")
    public void onRuntimeTabsheetSelectedChange(final JmixTabSheet.SelectedChangeEvent event) {
        Tab selectedTab = event.getSelectedTab();
        String tabId = selectedTab.getId().orElse(null);
        if (StringUtils.equals(tabId, USER_TASKS_TAB_ID)) {
            Component tabContent = getTabContent(selectedTab);
            if (tabContent instanceof RuntimeUserTasksTabFragment userTasksFragment) {
                userTasksFragment.refreshIfChanged(getSelectedActivityInstanceId());
            }
        } else if (StringUtils.equals(tabId, JOBS_TAB_ID)) {
            Component tabContent = getTabContent(selectedTab);
            if (tabContent instanceof JobsTabFragment jobsTabFragment) {
                jobsTabFragment.refreshIfRequired();
            }
        } else if (StringUtils.equals(tabId, EXTERNAL_TASKS_TAB_ID)) {
            Component tabContent = getTabContent(selectedTab);
            if (tabContent instanceof ExternalTasksTabFragment externalTasksTabFragment) {
                externalTasksTabFragment.refreshIfChanged(getSelectedActivityId());
            }
        } else if (StringUtils.equals(tabId, INCIDENTS_TAB_ID)) {
            Component tabContent = getTabContent(selectedTab);
            if (tabContent instanceof RuntimeIncidentsTabFragment incidentsTabFragment) {
                incidentsTabFragment.refreshIfChanged(getSelectedActivityId());
            }
        }
    }

    @EventListener
    public void handleUserTaskCountUpdate(UserTaskCountUpdateEvent event) {
        updateUserTasksTabCaption(event.getCount());
    }

    @EventListener
    public void handleIncidentCountUpdate(IncidentCountUpdateEvent event) {
        updateIncidentsTabCaption(event.getCount());
    }

    @EventListener
    public void handleIncidentUpdate(IncidentUpdateEvent event) {
        loadAndUpdateIncidentsCount();
    }

    @EventListener
    public void handleJobRetriesUpdate(JobRetriesUpdateEvent event) {
        loadAndUpdateJobsCount();
        loadAndUpdateIncidentsCount();
    }

    @EventListener
    public void handleExternalTaskRetriesUpdate(ExternalTaskRetriesUpdateEvent event) {
        loadAndUpdateExternalTasksCount();
        loadAndUpdateIncidentsCount();
    }

    @EventListener
    public void handleJobCountUpdate(JobCountUpdateEvent event) {
        updateJobsTabCaption(event.getCount());
    }

    @EventListener
    public void handleExternalTaskCountUpdate(ExternalTaskCountUpdateEvent event) {
        updateExternalTasksTabCaption(event.getCount());
    }

    @Install(to = "activityInstancesTree.activityId", subject = "tooltipGenerator")
    protected String activityInstancesTreeActivityIdTooltipGenerator(final ActivityInstanceTreeItem activityInstanceTreeItem) {
        if (activityInstanceTreeItem.getParentActivityInstance() == null) {
            return messageBundle.formatMessage("rootActivityInstanceTreeItem.tooltip", activityInstanceTreeItem.getActivityName());
        }
        return String.format("%s: %s", activityInstanceTreeItem.getActivityType(), activityInstanceTreeItem.getActivityName());
    }

    @Supply(to = "runtimeVariablesGrid.value", subject = "renderer")
    protected Renderer<VariableInstanceData> runtimeVariablesGridValueRenderer() {
        return new TextRenderer<>(this::getVariableValueColumnText);
    }

    @Install(to = "runtimeVariablesGrid.value", subject = "tooltipGenerator")
    protected String runtimeVariablesGridValueIdTooltipGenerator(final VariableInstanceData variableInstanceData) {
        return getVariableValueColumnText(variableInstanceData);
    }

    @Subscribe("runtimeVariablesGrid.edit")
    public void onRuntimeVariablesGridEdit(final ActionPerformedEvent event) {
        VariableInstanceData variableInstanceData = runtimeVariablesGrid.getSingleSelectedItem();
        if (variableInstanceData == null) {
            return;
        }
        dialogWindows.detail(getCurrentView(), VariableInstanceData.class)
                .editEntity(variableInstanceData)
                .withViewClass(VariableInstanceDataDetail.class)
                .withAfterCloseListener(afterCloseEvent -> {
                    if (afterCloseEvent.closedWith(StandardOutcome.SAVE)) {
                        runtimeVariablesDl.load();
                    }
                })
                .build()
                .open();
    }


    @Install(to = "runtimeActivityInstancesDl", target = Target.DATA_LOADER)
    protected List<ActivityInstanceTreeItem> runtimeActivityInstancesDlLoadDelegate(final LoadContext<ActivityInstanceTreeItem> loadContext) {
        return activityService.getActivityInstancesTree(processInstanceDataDc.getItem().getInstanceId());
    }

    @Install(to = "runtimeVariablesDl", target = Target.DATA_LOADER)
    protected List<VariableInstanceData> runtimeVariablesDlLoadDelegate(final LoadContext<VariableInstanceData> loadContext) {
        LoadContext.Query query = loadContext.getQuery();
        VariableLoadContext context = new VariableLoadContext()
                .setFilter(variableFilter);

        if (query != null) {
            context.setFirstResult(query.getFirstResult())
                    .setMaxResults(query.getMaxResults())
                    .setSort(query.getSort());
        }

        return variableService.findRuntimeVariables(context);
    }


    @Install(to = "runtimeVariablesPagination", subject = "totalCountDelegate")
    protected Integer runtimeVariablesPaginationTotalCountDelegate(final DataLoadContext dataLoadContext) {
        int runtimeVariablesCount = (int) variableService.getRuntimeVariablesCount(variableFilter);
        updateVariablesTabCaption(runtimeVariablesCount);
        return runtimeVariablesCount;
    }


    @Subscribe("runtimeVariablesGrid")
    public void onRuntimeVariablesGridGridSort(final SortEvent<DataGrid<VariableInstanceData>, GridSortOrder<DataGrid<VariableInstanceData>>> event) {
        runtimeVariablesDl.load();
    }

    @Subscribe(id = "runtimeActivityInstancesDc", target = Target.DATA_CONTAINER)
    public void onRuntimeActivityInstancesDcItemChange(InstanceContainer.ItemChangeEvent<ActivityInstanceTreeItem> event) {
        ActivityInstanceTreeItem treeItem = event.getItem();
        this.variableFilter.setActivityInstanceId(treeItem != null ? treeItem.getActivityInstanceId() : null);

        int selectedTabIdx = runtimeTabsheet.getSelectedIndex();
        switch (selectedTabIdx) {
            case VARIABLES_TAB_IDX -> runtimeVariablesDl.load();
            case USER_TASKS_TAB_IDX -> {
                Component tabContent = getTabContent(runtimeTabsheet.getSelectedTab());
                if (tabContent instanceof RuntimeUserTasksTabFragment userTasksFragment) {
                    userTasksFragment.refreshIfChanged(getSelectedActivityInstanceId());
                }
            }
            case INCIDENTS_TAB_IDX -> {
                Component tabContent = getTabContent(runtimeTabsheet.getSelectedTab());
                if (tabContent instanceof RuntimeIncidentsTabFragment incidentsTabFragment) {
                    incidentsTabFragment.refreshIfChanged(getSelectedActivityId());
                }
            }
            case EXTERNAL_TASKS_TAB_IDX -> {
                Component tabContent = getTabContent(runtimeTabsheet.getSelectedTab());
                if (tabContent instanceof ExternalTasksTabFragment externalTasksTabFragment) {
                    externalTasksTabFragment.refreshIfChanged(getSelectedActivityId());
                }
            }
            default -> {}
        }

        loadAndUpdateUserTasksCount();
        loadAndUpdateVariablesCount();
        loadAndUpdateIncidentsCount();
        loadAndUpdateExternalTasksCount();
    }

    protected void loadAndUpdateVariablesCount() {
        int runtimeVariablesCount = (int) variableService.getRuntimeVariablesCount(variableFilter);

        updateVariablesTabCaption(runtimeVariablesCount);
    }

    protected void loadAndUpdateJobsCount() {
        JobFilter jobFilter = metadata.create(JobFilter.class);
        jobFilter.setProcessInstanceId(processInstanceDataDc.getItem().getId());

        long jobsCount = jobService.getCount(jobFilter);
        updateJobsTabCaption(jobsCount);
    }

    protected void loadAndUpdateUserTasksCount() {
        UserTaskFilter userTaskFilter = metadata.create(UserTaskFilter.class);
        userTaskFilter.setActivityInstanceId(getSelectedActivityInstanceId());
        userTaskFilter.setProcessInstanceId(processInstanceDataDc.getItem().getId());

        long runtimeTasksCount = userTaskService.getRuntimeTasksCount(userTaskFilter);
        updateUserTasksTabCaption(runtimeTasksCount);
    }

    protected void loadAndUpdateExternalTasksCount() {
        ExternalTaskFilter filter = metadata.create(ExternalTaskFilter.class);
        filter.setActivityId(getSelectedActivityId());
        filter.setProcessInstanceId(processInstanceDataDc.getItem().getId());

        long tasksCount = externalTaskService.getRunningTasksCount(filter);
        updateExternalTasksTabCaption(tasksCount);
    }

    protected void loadAndUpdateIncidentsCount() {
        IncidentFilter filter = metadata.create(IncidentFilter.class);
        filter.setActivityId(getSelectedActivityId());
        filter.setProcessInstanceId(processInstanceDataDc.getItem().getId());

        long runtimeIncidentsCount = incidentService.getRuntimeIncidentCount(filter);
        updateIncidentsTabCaption(runtimeIncidentsCount);
    }

    @Nullable
    protected String getSelectedActivityInstanceId() {
        ActivityInstanceTreeItem item = activityInstancesTree.getSingleSelectedItem();
        return item != null ? item.getActivityInstanceId() : null;
    }

    @Nullable
    protected String getSelectedActivityId() {
        ActivityInstanceTreeItem item = activityInstancesTree.getSingleSelectedItem();
        return item != null ? item.getActivityId() : null;
    }

    @SuppressWarnings("JmixIncorrectCreateGuiComponent")
    protected void initUserTasksTab() {
        Tab userTasksTab = createTab(USER_TASKS_TAB_ID, "tasksTabCaption", VaadinIcon.USER_CARD);
        runtimeTabsheet.add(userTasksTab, new LazyTabContent(this::createUserTasksFragment), USER_TASKS_TAB_IDX);
    }

    @SuppressWarnings("JmixIncorrectCreateGuiComponent")
    protected void initJobsTab() {
        Tab jobsTab = createTab(JOBS_TAB_ID, "jobsTabCaption", VaadinIcon.COGS);
        runtimeTabsheet.add(jobsTab, new LazyTabContent(() -> fragments.create(getParentController(), JobsTabFragment.class)), JOBS_TAB_IDX);
    }

    @SuppressWarnings("JmixIncorrectCreateGuiComponent")
    protected void initExternalTasksTab() {
        Tab externalTasksTab = createTab(EXTERNAL_TASKS_TAB_ID, "externalTasksTabCaption", VaadinIcon.CLUSTER);
        runtimeTabsheet.add(externalTasksTab, new LazyTabContent(() -> fragments.create(getParentController(), ExternalTasksTabFragment.class)), EXTERNAL_TASKS_TAB_IDX);
    }

    protected void updateUserTasksTabCaption(long userTasksCount) {
        updateTabCaption(USER_TASKS_TAB_IDX, "tasksTabCaption", userTasksCount, VaadinIcon.USER_CARD);
    }

    protected void updateExternalTasksTabCaption(long externalTaskCount) {
        updateTabCaption(EXTERNAL_TASKS_TAB_IDX, "externalTasksTabCaption", externalTaskCount, VaadinIcon.CLUSTER);
    }

    protected void updateIncidentsTabCaption(long incidentCount) {
        updateTabCaption(INCIDENTS_TAB_IDX, "incidentsTabCaption", incidentCount, VaadinIcon.WARNING);
    }

    protected void updateJobsTabCaption(long jobsCount) {
        updateTabCaption(JOBS_TAB_IDX, "jobsTabCaption", jobsCount, VaadinIcon.COGS);
    }

    protected void updateVariablesTabCaption(int runtimeVariablesCount) {
        updateTabCaption(VARIABLES_TAB_IDX, "variablesTabCaption", runtimeVariablesCount, VaadinIcon.CURLY_BRACKETS);
    }


    protected Component createUserTasksFragment() {
        RuntimeUserTasksTabFragment userTasksFragment = fragments.create(getParentController(), RuntimeUserTasksTabFragment.class);
        userTasksFragment.setSelectedActivityInstanceId(getSelectedActivityInstanceId());

        return userTasksFragment;
    }

    protected Component createIncidentsFragment() {
        RuntimeIncidentsTabFragment incidentsTabFragment = fragments.create(getParentController(), RuntimeIncidentsTabFragment.class);
        incidentsTabFragment.setSelectedActivityId(getSelectedActivityId());

        return incidentsTabFragment;
    }

    @Nullable
    protected String getVariableValueColumnText(VariableInstanceData variableInstance) {
        if (variableInstance.getValue() != null) {
            return variableInstance.getValue().toString();
        }
        return null;
    }

    protected void updateTabCaption(int tabIndex, String messageKey, long count, VaadinIcon icon) {
        runtimeTabsheet.getTabAt(tabIndex).setLabel(messageBundle.formatMessage(messageKey, count));
        runtimeTabsheet.getTabAt(tabIndex).addComponentAsFirst(icon.create());
    }

    protected Tab createTab(String id, String messageKey, VaadinIcon icon) {
        Tab tab = uiComponents.create(Tab.class);
        tab.setId(id);
        tab.setLabel(messageBundle.formatMessage(messageKey, 0));
        tab.addComponentAsFirst(icon.create());
        return tab;
    }

    @Nullable
    protected Component getTabContent(Tab tab) {
        return runtimeTabsheet.getContentByTab(tab)
                .getChildren()
                .findFirst()
                .orElse(null);
    }

}
