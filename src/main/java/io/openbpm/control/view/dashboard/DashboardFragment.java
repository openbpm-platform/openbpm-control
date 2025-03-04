/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.view.dashboard;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.flowui.DialogWindows;
import io.jmix.flowui.asynctask.UiAsyncTasks;
import io.jmix.flowui.fragment.Fragment;
import io.jmix.flowui.fragment.FragmentDescriptor;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.view.MessageBundle;
import io.jmix.flowui.view.StandardOutcome;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.ViewComponent;
import io.openbpm.control.entity.ProcessExecutionGraphEntry;
import io.openbpm.control.entity.dashboard.ProcessDefinitionStatistics;
import io.openbpm.control.entity.engine.BpmEngine;
import io.openbpm.control.property.UiProperties;
import io.openbpm.control.service.dashboard.DashboardService;
import io.openbpm.control.service.engine.EngineService;
import io.openbpm.control.service.engine.EngineUiService;
import io.openbpm.control.view.bpmengine.BpmEngineDetailView;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static io.jmix.flowui.component.UiComponentUtils.getCurrentView;

@Slf4j
@FragmentDescriptor("dashboard-fragment.xml")
public class DashboardFragment extends Fragment<VerticalLayout> {
    @ViewComponent
    protected H3 welcomeMessage;
    @ViewComponent
    protected MessageBundle messageBundle;
    @Autowired
    protected CurrentAuthentication currentAuthentication;

    @Autowired
    protected DialogWindows dialogWindows;

    @Autowired
    protected EngineService engineService;
    @ViewComponent
    protected Div dashboardContainer;
    @ViewComponent
    protected Div noEnginesContainer;
    @ViewComponent
    protected InstanceContainer<BpmEngine> selectedEngineDc;

    @ViewComponent
    protected DeployedProcessesStatisticsCardFragment deployedProcessesCard;
    @ViewComponent
    protected ProcessInstanceStatisticsCardFragment processInstancesCard;
    @ViewComponent
    protected UserTaskStatisticsCardFragment userTasksCard;
    @ViewComponent
    protected RecentActivityCardFragment recentActivityCard;
    @ViewComponent
    protected RunningInstancesAndIncidentsFragment runningInstancesAndIncidentsCard;
    @Autowired
    protected UiAsyncTasks uiAsyncTasks;
    @Autowired
    protected DashboardService dashboardService;
    @Autowired
    protected UiProperties uiProperties;
    @Autowired
    protected EngineUiService engineUiService;

    @Subscribe
    public void onReady(final ReadyEvent event) {
        welcomeMessage.setText(messageBundle.formatMessage("welcomeMessage", currentAuthentication.getUser().getUsername()));
        updateDashboard();
    }

    public void updateDashboard() {
        boolean engineExists = engineService.engineExists();
        if (!engineExists) {
            noEnginesContainer.setVisible(true);
            dashboardContainer.setVisible(false);
        } else {
            noEnginesContainer.setVisible(false);
            dashboardContainer.setVisible(true);

            updateDashboardCards();
        }
    }

    protected void updateDashboardCards() {
        deployedProcessesCard.setLoading();
        processInstancesCard.setLoading();
        userTasksCard.setLoading();
        runningInstancesAndIncidentsCard.setLoading();
        recentActivityCard.setLoading();

        uiAsyncTasks.supplierConfigurer(() -> loadDashboardData(selectedEngineDc.getItemOrNull()))
                .withTimeout(uiProperties.getDashboardLoadTimeoutSec(), TimeUnit.SECONDS)
                .withResultHandler(dashboardData -> {
                    deployedProcessesCard.refresh(dashboardData.getProcessCount());
                    processInstancesCard.refresh(dashboardData.getRunningInstanceCount(), dashboardData.getSuspendedInstanceCount());
                    userTasksCard.refresh(dashboardData.getUserTasksCount());
                    recentActivityCard.refresh(dashboardData.getRecentActivity());
                    runningInstancesAndIncidentsCard.refresh(dashboardData.getProcessDefinitionStatistics());
                })
                .withExceptionHandler(throwable -> {
                    log.error("Error occurs on dashboard data loading", throwable);

                    deployedProcessesCard.refresh(0);
                    processInstancesCard.refresh(0, 0);
                    userTasksCard.refresh(0);
                    recentActivityCard.refresh(List.of());
                    runningInstancesAndIncidentsCard.refresh(List.of());
                })
                .supplyAsync();

    }

    @Subscribe(id = "createBpmEnginBtn", subject = "clickListener")
    public void onCreateBpmEnginBtnClick(final ClickEvent<JmixButton> event) {
        dialogWindows.detail(getCurrentView(), BpmEngine.class)
                .newEntity()
                .withAfterCloseListener(closeEvent -> {
                    if (closeEvent.closedWith(StandardOutcome.SAVE)) {
                        BpmEngineDetailView detailView = (BpmEngineDetailView) closeEvent.getView();
                        BpmEngine createdEngine = detailView.getEditedEntity();
                        engineUiService.selectEngine(createdEngine);
                    }
                })
                .open();
    }

    protected DashboardData loadDashboardData(BpmEngine engine) {
        DashboardData dashboardData = new DashboardData();

        if (engine == null) {
            return dashboardData;
        }

        BpmEngine persistedEngined = engineService.findEngineByUuid(engine.getId());

        long deployedProcessesCount = dashboardService.getDeployedProcessesCount(persistedEngined);
        dashboardData.setProcessCount(deployedProcessesCount);

        long runningProcessCount = dashboardService.getRunningProcessCount(persistedEngined);
        dashboardData.setRunningInstanceCount(runningProcessCount);

        long suspendedProcessCount = dashboardService.getSuspendedProcessCount(persistedEngined);
        dashboardData.setSuspendedInstanceCount(suspendedProcessCount);

        List<ProcessDefinitionStatistics> processStatistics = dashboardService.getProcessDefinitionStatistics(persistedEngined);
        dashboardData.setProcessDefinitionStatistics(processStatistics);

        long userTasksCount = dashboardService.getUserTasksCount(persistedEngined);
        dashboardData.setUserTasksCount(userTasksCount);

        List<ProcessExecutionGraphEntry> recentActivity = dashboardService.getRecentActivityStatistics(persistedEngined);
        dashboardData.setRecentActivity(recentActivity);

        return dashboardData;
    }


    @Getter
    @Setter
    protected static class DashboardData {
        private long processCount;
        private long runningInstanceCount;
        private long suspendedInstanceCount;
        private long userTasksCount;
        private List<ProcessExecutionGraphEntry> recentActivity = new ArrayList<>();
        private List<ProcessDefinitionStatistics> processDefinitionStatistics = new ArrayList<>();
    }
}