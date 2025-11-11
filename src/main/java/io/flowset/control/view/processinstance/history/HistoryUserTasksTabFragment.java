/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.control.view.processinstance.history;

import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.event.SortEvent;
import io.jmix.core.DataLoadContext;
import io.jmix.core.LoadContext;
import io.jmix.core.Metadata;
import io.jmix.flowui.DialogWindows;
import io.jmix.flowui.UiEventPublisher;
import io.jmix.flowui.ViewNavigators;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.fragment.Fragment;
import io.jmix.flowui.fragment.FragmentDescriptor;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.model.CollectionContainer;
import io.jmix.flowui.model.CollectionLoader;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.view.Install;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.Target;
import io.jmix.flowui.view.ViewComponent;
import io.flowset.control.entity.UserTaskData;
import io.flowset.control.entity.filter.UserTaskFilter;
import io.flowset.control.entity.processinstance.ProcessInstanceData;
import io.flowset.control.service.usertask.UserTaskLoadContext;
import io.flowset.control.service.usertask.UserTaskService;
import io.flowset.control.view.processinstance.event.HistoryUserTaskCountUpdateEvent;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static io.jmix.flowui.component.UiComponentUtils.getCurrentView;

@FragmentDescriptor("history-user-tasks-tab-fragment.xml")
public class HistoryUserTasksTabFragment extends Fragment<VerticalLayout> implements HasRefresh {
    @Autowired
    protected UserTaskService userTaskService;
    @Autowired
    protected Metadata metadata;
    @Autowired
    protected UiEventPublisher uiEventPublisher;
    @Autowired
    protected DialogWindows dialogWindows;
    @Autowired
    protected ViewNavigators viewNavigators;

    @ViewComponent
    protected CollectionLoader<UserTaskData> historyTasksDl;

    @ViewComponent
    protected CollectionContainer<UserTaskData> historyTasksDc;

    @ViewComponent
    protected DataGrid<UserTaskData> historyTasksGrid;

    @ViewComponent
    protected InstanceContainer<ProcessInstanceData> processInstanceDataDc;

    protected UserTaskFilter filter;

    protected boolean initialized;

    public void refreshIfRequired() {
        if (!initialized) {
            this.filter = metadata.create(UserTaskFilter.class);
            filter.setProcessInstanceId(processInstanceDataDc.getItem().getId());

            historyTasksDl.load();
            this.initialized = true;
        }
    }

    @Subscribe("historyTasksGrid.view")
    public void onViewAction(final ActionPerformedEvent event) {
        UserTaskData userTaskData = historyTasksGrid.getSingleSelectedItem();
        if (userTaskData == null) {
            return;
        }
        dialogWindows.detail(getCurrentView(), UserTaskData.class)
                .editEntity(userTaskData)
                .build()
                .open();
    }

    @Install(to = "historyTasksDl", target = Target.DATA_LOADER)
    protected List<UserTaskData> historyTasksDlLoadDelegate(final LoadContext<UserTaskData> loadContext) {
        LoadContext.Query query = loadContext.getQuery();
        UserTaskLoadContext context = new UserTaskLoadContext().setFilter(filter);

        if (query != null) {
            context.setFirstResult(query.getFirstResult())
                    .setMaxResults(query.getMaxResults())
                    .setSort(query.getSort());
        }

        return userTaskService.findHistoricTasks(context);
    }

    @Install(to = "userTasksPagination", subject = "totalCountDelegate")
    protected Integer userTasksPaginationTotalCountDelegate(final DataLoadContext dataLoadContext) {
        long historyTasksCount = userTaskService.getHistoryTasksCount(filter);
        uiEventPublisher.publishEventForCurrentUI(new HistoryUserTaskCountUpdateEvent(this, historyTasksCount));
        return (int) historyTasksCount;
    }

    @Subscribe("historyTasksGrid")
    public void onHistoryTasksGridGridSort(final SortEvent<DataGrid<UserTaskData>, GridSortOrder<DataGrid<UserTaskData>>> event) {
        historyTasksDl.load();
    }

}
