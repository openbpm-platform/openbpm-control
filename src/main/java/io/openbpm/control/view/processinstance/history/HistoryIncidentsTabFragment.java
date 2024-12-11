/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.view.processinstance.history;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.event.SortEvent;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.theme.lumo.LumoUtility;
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
import io.jmix.flowui.model.CollectionLoader;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.view.*;
import io.openbpm.control.entity.filter.IncidentFilter;
import io.openbpm.control.entity.incident.HistoricIncidentData;
import io.openbpm.control.entity.processinstance.ProcessInstanceData;
import io.openbpm.control.service.incident.IncidentLoadContext;
import io.openbpm.control.service.incident.IncidentService;
import io.openbpm.control.view.processinstance.event.HistoryUserTaskCountUpdateEvent;
import io.openbpm.control.view.processinstance.event.IncidentUpdateEvent;
import io.openbpm.control.view.processinstance.event.JobRetriesUpdateEvent;
import io.openbpm.control.view.util.ComponentHelper;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;

import java.util.List;

import static io.jmix.flowui.component.UiComponentUtils.getCurrentView;

@FragmentDescriptor("history-incidents-tab-fragment.xml")
public class HistoryIncidentsTabFragment extends Fragment<VerticalLayout> implements HasRefresh {
    @Autowired
    protected IncidentService userTaskService;
    @Autowired
    protected Metadata metadata;
    @Autowired
    protected UiEventPublisher uiEventPublisher;
    @Autowired
    protected DialogWindows dialogWindows;
    @Autowired
    protected ViewNavigators viewNavigators;

    @ViewComponent
    protected InstanceContainer<ProcessInstanceData> processInstanceDataDc;
    @ViewComponent
    protected CollectionLoader<HistoricIncidentData> incidentsDl;

    @ViewComponent
    protected DataGrid<HistoricIncidentData> incidentsGrid;


    @Autowired
    protected ComponentHelper componentHelper;

    protected IncidentFilter filter;

    protected boolean initialized;

    public void refreshIfRequired() {
        if (!initialized) {
            this.filter = metadata.create(IncidentFilter.class);
            filter.setProcessInstanceId(processInstanceDataDc.getItem().getId());

            incidentsDl.load();
            this.initialized = true;
        }
    }

    @Subscribe("incidentsGrid.view")
    public void onViewAction(final ActionPerformedEvent event) {
        HistoricIncidentData incident = incidentsGrid.getSingleSelectedItem();
        if (incident == null) {
            return;
        }
        dialogWindows.detail(getCurrentView(), HistoricIncidentData.class)
                .editEntity(incident)
                .build()
                .open();
    }

    @Install(to = "incidentsDl", target = Target.DATA_LOADER)
    protected List<HistoricIncidentData> incidentsDlLoadDelegate(final LoadContext<HistoricIncidentData> loadContext) {
        LoadContext.Query query = loadContext.getQuery();
        IncidentLoadContext context = new IncidentLoadContext().setFilter(filter);

        if (query != null) {
            context.setFirstResult(query.getFirstResult())
                    .setMaxResults(query.getMaxResults())
                    .setSort(query.getSort());
        }

        return userTaskService.findHistoricIncidents(context);
    }

    @Install(to = "incidentsPagination", subject = "totalCountDelegate")
    protected Integer incidentsPaginationTotalCountDelegate(final DataLoadContext dataLoadContext) {
        long incidentsCount = userTaskService.getHistoricIncidentCount(filter);
        uiEventPublisher.publishEventForCurrentUI(new HistoryUserTaskCountUpdateEvent(this, incidentsCount));
        return (int) incidentsCount;
    }

    @Subscribe("incidentsGrid")
    public void onHistoryTasksGridGridSort(final SortEvent<DataGrid<HistoricIncidentData>, GridSortOrder<DataGrid<HistoricIncidentData>>> event) {
        incidentsDl.load();
    }

    @Supply(to = "incidentsGrid.resolved", subject = "renderer")
    protected Renderer<HistoricIncidentData> incidentsGridResolvedRenderer() {
        return new ComponentRenderer<>(historicIncidentData -> {
            Icon icon = BooleanUtils.isTrue(historicIncidentData.getResolved()) ? VaadinIcon.CHECK.create() : VaadinIcon.CLOSE.create();
            icon.setSize("0.85em");
            return icon;
        });
    }

    @EventListener
    public void handleIncidentUpdate(IncidentUpdateEvent event) {
        this.initialized = false; //to reload on the next opening
    }

    @EventListener
    public void handleIncidentUpdate(JobRetriesUpdateEvent event) {
        this.initialized = false; //to reload on the next opening
    }

    @Install(to = "incidentsGrid.createTime", subject = "partNameGenerator")
    protected String incidentsGridCreateTimePartNameGenerator(final HistoricIncidentData historicIncidentData) {
        return "multiline-text-cell";
    }

    @Supply(to = "incidentsGrid.createTime", subject = "renderer")
    protected Renderer<HistoricIncidentData> incidentsGridCreateTimeRenderer() {
        return new ComponentRenderer<>(incidentData -> {
            Component dateSpan = componentHelper.createDateSpan(incidentData.getCreateTime());
            dateSpan.addClassNames(LumoUtility.Overflow.HIDDEN, LumoUtility.TextOverflow.ELLIPSIS);
            return dateSpan;
        });
    }

    @Install(to = "incidentsGrid.endTime", subject = "partNameGenerator")
    protected String incidentsGridEndTimePartNameGenerator(final HistoricIncidentData historicIncidentData) {
        return "multiline-text-cell";
    }

    @Supply(to = "incidentsGrid.endTime", subject = "renderer")
    protected Renderer<HistoricIncidentData> incidentsGridEndTimeRenderer() {
        return new ComponentRenderer<>(incidentData -> {
            Span dateSpan = componentHelper.createDateSpan(incidentData.getEndTime());
            dateSpan.addClassNames(LumoUtility.Overflow.HIDDEN, LumoUtility.TextOverflow.ELLIPSIS);
            return dateSpan;
        });
    }

}
