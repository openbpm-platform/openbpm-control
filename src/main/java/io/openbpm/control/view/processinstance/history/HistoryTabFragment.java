/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.view.processinstance.history;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.tabs.Tab;
import io.jmix.core.Metadata;
import io.jmix.flowui.Fragments;
import io.jmix.flowui.component.tabsheet.JmixTabSheet;
import io.jmix.flowui.fragment.Fragment;
import io.jmix.flowui.fragment.FragmentDescriptor;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.view.MessageBundle;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.ViewComponent;
import io.openbpm.control.entity.filter.ActivityFilter;
import io.openbpm.control.entity.filter.IncidentFilter;
import io.openbpm.control.entity.filter.UserTaskFilter;
import io.openbpm.control.entity.filter.VariableFilter;
import io.openbpm.control.entity.processinstance.ProcessInstanceData;
import io.openbpm.control.service.activity.ActivityService;
import io.openbpm.control.service.incident.IncidentService;
import io.openbpm.control.service.usertask.UserTaskService;
import io.openbpm.control.service.variable.VariableService;
import io.openbpm.control.view.processinstance.LazyTabContent;
import io.openbpm.control.view.processinstance.event.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.lang.Nullable;

@FragmentDescriptor("history-tab-fragment.xml")
public class HistoryTabFragment extends Fragment<JmixTabSheet> {
    public static final String USER_TASKS_TAB_ID = "historyTasksTab";
    public static final String VARIABLES_TAB_ID = "historyVariablesTab";
    public static final String INCIDENTS_TAB_ID = "historyIncidentsTab";

    public static final int ACTIVITIES_TAB_IDX = 0;
    public static final int USER_TASKS_TAB_IDX = 1;
    public static final int VARIABLES_TAB_IDX = 2;
    public static final int INCIDENTS_TAB_IDX = 3;

    @Autowired
    protected Metadata metadata;
    @Autowired
    protected Fragments fragments;
    @ViewComponent
    protected MessageBundle messageBundle;

    @Autowired
    protected ActivityService activityService;
    @Autowired
    protected VariableService variableService;
    @Autowired
    protected UserTaskService userTaskService;
    @Autowired
    protected IncidentService incidentService;

    @ViewComponent
    protected InstanceContainer<ProcessInstanceData> processInstanceDataDc;

    @ViewComponent
    protected JmixTabSheet historyTabsheet;

    @ViewComponent
    protected ActivitiesTabFragment activitiesFragment;

    protected boolean initialized = false;

    @Subscribe
    public void onReady(ReadyEvent event) {
        initUserTasksTab();
        initVariablesTab();
        initIncidentsTab();
    }

    public void refresh() {
        if (!initialized) {
            loadAndUpdateActivitiesCount();
            loadAndUpdateUserTasksCount();
            loadAndUpdateVariablesCount();
            loadAndUpdateIncidentsCount();
            this.initialized = true;
        }
        historyTabsheet.setSelectedIndex(ACTIVITIES_TAB_IDX);
        activitiesFragment.refreshIfRequired();
    }

    @Subscribe("historyTabsheet")
    public void onHistoryTabsheetSelectedChange(final JmixTabSheet.SelectedChangeEvent event) {
        Tab selectedTab = event.getSelectedTab();
        Component tabContent = getTabContent(selectedTab);
        if (tabContent instanceof HasRefresh tabContentFragment) {
            tabContentFragment.refreshIfRequired();
        }
    }

    @EventListener
    public void handleActivityCountUpdate(HistoryActivityCountUpdateEvent event) {
        updateActivityTabCaption(event.getCount());
    }

    @EventListener
    public void handleUserTaskCountUpdate(HistoryUserTaskCountUpdateEvent event) {
        updateUserTasksTabCaption(event.getCount());
    }

    @EventListener
    public void handleVariableCountUpdate(HistoryVariableCountUpdateEvent event) {
        updateVariablesTabCaption(event.getCount());
    }

    @EventListener
    public void handleIncidentUpdate(IncidentUpdateEvent event) {
        loadAndUpdateIncidentsCount();
    }

    @EventListener
    public void handleJobRetriesUpdate(JobRetriesUpdateEvent event) {
        loadAndUpdateIncidentsCount();
    }

    @EventListener
    public void handleIncidentCountUpdate(HistoryIncidentCountUpdateEvent event) {
        updateIncidentTabCaption(event.getCount());
    }

    protected void loadAndUpdateActivitiesCount() {
        ActivityFilter activityFilter = metadata.create(ActivityFilter.class);
        activityFilter.setProcessInstanceId(processInstanceDataDc.getItem().getId());

        long activitiesCount = activityService.getHistoryActivitiesCount(activityFilter);

        updateActivityTabCaption(activitiesCount);
    }

    protected void loadAndUpdateVariablesCount() {
        VariableFilter variableFilter = metadata.create(VariableFilter.class);
        variableFilter.setProcessInstanceId(processInstanceDataDc.getItem().getId());

        long variablesCount = variableService.getHistoricVariablesCount(variableFilter);

        updateVariablesTabCaption(variablesCount);
    }

    protected void loadAndUpdateIncidentsCount() {
        IncidentFilter incidentFilter = metadata.create(IncidentFilter.class);
        incidentFilter.setProcessInstanceId(processInstanceDataDc.getItem().getId());

        long incidentCount = incidentService.getHistoricIncidentCount(incidentFilter);

        updateIncidentTabCaption(incidentCount);
    }

    protected void loadAndUpdateUserTasksCount() {
        UserTaskFilter userTaskFilter = metadata.create(UserTaskFilter.class);
        userTaskFilter.setProcessInstanceId(processInstanceDataDc.getItem().getId());

        long historyTasksCount = userTaskService.getHistoryTasksCount(userTaskFilter);

        updateUserTasksTabCaption(historyTasksCount);
    }


    @SuppressWarnings("JmixIncorrectCreateGuiComponent")
    protected void initUserTasksTab() {
        Tab userTasksTab = createTab(USER_TASKS_TAB_ID, "ProcessInstanceEditHistoryFragment.tasksTabCaption",
                VaadinIcon.USER_CARD);
        historyTabsheet.add(userTasksTab, new LazyTabContent(() -> fragments.create(getParentController(), HistoryUserTasksTabFragment.class)), USER_TASKS_TAB_IDX);
    }

    @SuppressWarnings("JmixIncorrectCreateGuiComponent")
    protected void initVariablesTab() {
        Tab variablesTab = createTab(VARIABLES_TAB_ID, "ProcessInstanceEditHistoryFragment.historicVariableInstancesTabCaption",
                VaadinIcon.COGS);
        historyTabsheet.add(variablesTab, new LazyTabContent(() -> fragments.create(getParentController(), HistoryVariablesTabFragment.class)), VARIABLES_TAB_IDX);
    }

    @SuppressWarnings("JmixIncorrectCreateGuiComponent")
    protected void initIncidentsTab() {
        Tab incidentsTab = createTab(INCIDENTS_TAB_ID, "ProcessInstanceEditHistoryFragment.incidentsTabCaption",
                VaadinIcon.WARNING);
        historyTabsheet.add(incidentsTab, new LazyTabContent(() -> fragments.create(getParentController(), HistoryIncidentsTabFragment.class)), INCIDENTS_TAB_IDX);
    }


    protected void updateUserTasksTabCaption(long userTasksCount) {
        updateTabCaption(USER_TASKS_TAB_IDX,
                "ProcessInstanceEditHistoryFragment.tasksTabCaption",
                userTasksCount, VaadinIcon.USER_CARD);
    }

    protected void updateVariablesTabCaption(long variablesCount) {
        updateTabCaption(VARIABLES_TAB_IDX,
                "ProcessInstanceEditHistoryFragment.historicVariableInstancesTabCaption",
                variablesCount, VaadinIcon.CURLY_BRACKETS);
    }

    protected void updateIncidentTabCaption(long incidentCount) {
        updateTabCaption(INCIDENTS_TAB_IDX,
                "ProcessInstanceEditHistoryFragment.incidentsTabCaption",
                incidentCount, VaadinIcon.WARNING);
    }

    protected void updateActivityTabCaption(long activitiesCount) {
        updateTabCaption(ACTIVITIES_TAB_IDX,
                "ProcessInstanceEditHistoryFragment.historicActivityInstancesTabCaption",
                activitiesCount, VaadinIcon.CUBES);
    }

    protected void updateTabCaption(int tabIndex, String messageKey, long count, VaadinIcon icon) {
        historyTabsheet.getTabAt(tabIndex).setLabel(messageBundle.formatMessage(messageKey, count));
        historyTabsheet.getTabAt(tabIndex).addComponentAsFirst(icon.create());
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
        return historyTabsheet.getContentByTab(tab)
                .getChildren()
                .findFirst()
                .orElse(null);
    }

}
