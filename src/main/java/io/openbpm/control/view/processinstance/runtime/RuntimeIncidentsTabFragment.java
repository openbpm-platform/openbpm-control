/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.view.processinstance.runtime;

import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.event.SortEvent;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.theme.lumo.LumoUtility;
import io.jmix.core.DataLoadContext;
import io.jmix.core.LoadContext;
import io.jmix.core.Messages;
import io.jmix.core.Metadata;
import io.jmix.flowui.*;
import io.jmix.flowui.action.DialogAction;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.fragment.Fragment;
import io.jmix.flowui.fragment.FragmentDescriptor;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.kit.action.ActionVariant;
import io.jmix.flowui.model.CollectionLoader;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.view.*;
import io.openbpm.control.entity.filter.IncidentFilter;
import io.openbpm.control.entity.incident.IncidentData;
import io.openbpm.control.entity.processinstance.ProcessInstanceData;
import io.openbpm.control.service.externaltask.ExternalTaskService;
import io.openbpm.control.service.incident.IncidentLoadContext;
import io.openbpm.control.service.incident.IncidentService;
import io.openbpm.control.service.job.JobService;
import io.openbpm.control.view.processinstance.event.IncidentCountUpdateEvent;
import io.openbpm.control.view.processinstance.event.IncidentUpdateEvent;
import io.openbpm.control.view.util.ComponentHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static io.jmix.flowui.component.UiComponentUtils.getCurrentView;

@FragmentDescriptor("runtime-incident-tab-fragment.xml")
public class RuntimeIncidentsTabFragment extends Fragment<VerticalLayout> {

    @Autowired
    protected Metadata metadata;
    @Autowired
    protected UiEventPublisher uiEventPublisher;
    @Autowired
    protected DialogWindows dialogWindows;
    @Autowired
    protected ViewNavigators viewNavigators;
    @Autowired
    protected Dialogs dialogs;
    @Autowired
    protected Messages messages;
    @Autowired
    protected Notifications notifications;
    @Autowired
    protected ComponentHelper componentHelper;

    @Autowired
    protected IncidentService incidentService;
    @Autowired
    protected JobService jobService;
    @Autowired
    protected ExternalTaskService externalTaskService;

    @ViewComponent
    protected CollectionLoader<IncidentData> runtimeIncidentsDl;

    @ViewComponent
    protected InstanceContainer<ProcessInstanceData> processInstanceDataDc;

    @ViewComponent
    protected DataGrid<IncidentData> runtimeIncidentsGrid;

    protected IncidentFilter filter;
    protected String selectedActivityId;
    protected boolean initialized = false;

    @SuppressWarnings("LombokSetterMayBeUsed")
    public void setSelectedActivityId(String selectedActivityId) {
        this.selectedActivityId = selectedActivityId;
    }

    public void refreshIfChanged(String selectedActivityId) {
        if (!initialized) {
            this.filter = metadata.create(IncidentFilter.class);
            filter.setProcessInstanceId(processInstanceDataDc.getItem().getId());
            runtimeIncidentsDl.load();
            this.initialized = true;
            return;
        }

        if (!StringUtils.equals(this.selectedActivityId, selectedActivityId)) {
            this.selectedActivityId = selectedActivityId;
            filter.setActivityId(selectedActivityId);
            runtimeIncidentsDl.load();
        }
    }

    @Subscribe("runtimeIncidentsGrid.view")
    public void onViewAction(final ActionPerformedEvent event) {
        IncidentData incidentData = runtimeIncidentsGrid.getSingleSelectedItem();
        if (incidentData == null) {
            return;
        }
        dialogWindows.detail(getCurrentView(), IncidentData.class)
                .editEntity(incidentData)
                .withAfterCloseListener(closeEvent -> {
                    if (closeEvent.closedWith(StandardOutcome.SAVE)) {
                        reloadIncidents();
                    }
                })
                .build()
                .open();
    }

    @Install(to = "runtimeIncidentsGrid.retry", subject = "enabledRule")
    protected boolean runtimeIncidentsGridRetryEnabledRule() {
        IncidentData selectedItem = runtimeIncidentsGrid.getSingleSelectedItem();
        return selectedItem != null && selectedItem.getConfiguration() != null &&
                (selectedItem.isJobFailed() || selectedItem.isExternalTaskFailed());
    }


    @Subscribe("runtimeIncidentsGrid.retry")
    public void onRetryAction(final ActionPerformedEvent event) {
        IncidentData incident = runtimeIncidentsGrid.getSingleSelectedItem();
        if (incident == null || incident.getConfiguration() == null) {
            return;
        }

        if (incident.isExternalTaskFailed()) {
            dialogs.createOptionDialog()
                    .withHeader(messages.getMessage("io.openbpm.control.view.incidentdata/retryExternalTask.header"))
                    .withText(messages.getMessage("io.openbpm.control.view.incidentdata/retryExternalTask.text"))
                    .withActions(new DialogAction(DialogAction.Type.YES)
                                    .withText(messages.getMessage("actions.Retry"))
                                    .withIcon(VaadinIcon.ROTATE_LEFT.create())
                                    .withVariant(ActionVariant.PRIMARY)
                                    .withHandler(actionPerformedEvent -> updateExternalTaskRetries(incident)),
                            new DialogAction(DialogAction.Type.CANCEL))
                    .open();
        } else if (incident.isJobFailed()) {
            dialogs.createOptionDialog()
                    .withHeader(messages.getMessage("io.openbpm.control.view.incidentdata/retryJob.header"))
                    .withText(messages.getMessage("io.openbpm.control.view.incidentdata/retryJob.text"))
                    .withActions(new DialogAction(DialogAction.Type.YES)
                                    .withText(messages.getMessage("actions.Retry"))
                                    .withIcon(VaadinIcon.ROTATE_LEFT.create())
                                    .withVariant(ActionVariant.PRIMARY)
                                    .withHandler(actionPerformedEvent -> updateJobRetries(incident)),
                            new DialogAction(DialogAction.Type.CANCEL))
                    .open();
        }
    }


    @Install(to = "runtimeIncidentsGrid.timestamp", subject = "partNameGenerator")
    protected String runtimeIncidentsGridTimestampPartNameGenerator(final IncidentData incidentData) {
        return "multiline-text-cell";
    }

    @Supply(to = "runtimeIncidentsGrid.timestamp", subject = "renderer")
    protected Renderer<IncidentData> runtimeIncidentsGridTimestampRenderer() {
        return new ComponentRenderer<>(incidentData -> {
            Span span = componentHelper.createDateSpan(incidentData.getTimestamp());
            span.addClassNames(LumoUtility.Overflow.HIDDEN, LumoUtility.TextOverflow.ELLIPSIS);
            return span;
        });
    }

    @Install(to = "runtimeIncidentsDl", target = Target.DATA_LOADER)
    protected List<IncidentData> runtimeIncidentsDlLoadDelegate(final LoadContext<IncidentData> loadContext) {
        LoadContext.Query query = loadContext.getQuery();
        IncidentLoadContext context = new IncidentLoadContext().setFilter(filter);

        if (query != null) {
            context.setFirstResult(query.getFirstResult())
                    .setMaxResults(query.getMaxResults())
                    .setSort(query.getSort());
        }

        return incidentService.findRuntimeIncidents(context);
    }

    @Install(to = "incidentsPagination", subject = "totalCountDelegate")
    protected Integer incidentsPaginationTotalCountDelegate(final DataLoadContext dataLoadContext) {
        long incidentCount = incidentService.getRuntimeIncidentCount(filter);

        uiEventPublisher.publishEventForCurrentUI(new IncidentCountUpdateEvent(this, incidentCount));
        return (int) incidentCount;
    }

    @Subscribe("runtimeIncidentsGrid")
    public void onRuntimeIncidentsGridGridSort(final SortEvent<DataGrid<IncidentData>, GridSortOrder<DataGrid<IncidentData>>> event) {
        runtimeIncidentsDl.load();
    }

    protected void updateJobRetries(IncidentData incident) {
        jobService.setJobRetries(incident.getConfiguration(), 1);
        notifications.create(messages.getMessage("io.openbpm.control.view.incidentdata/jobRetriesUpdated"))
                .withType(Notifications.Type.SUCCESS)
                .show();

        reloadIncidents();
    }

    protected void reloadIncidents() {
        runtimeIncidentsDl.load();
        uiEventPublisher.publishEventForCurrentUI(new IncidentUpdateEvent(this));
    }

    protected void updateExternalTaskRetries(IncidentData incident) {
        externalTaskService.setRetries(incident.getConfiguration(), 1);
        notifications.create(messages.getMessage("io.openbpm.control.view.incidentdata/externalTaskRetriesUpdated"))
                .withType(Notifications.Type.SUCCESS)
                .show();

        reloadIncidents();
    }

}
