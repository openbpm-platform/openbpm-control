/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.view.processinstance.filter;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.openbpm.control.entity.processinstance.ProcessInstanceData;
import io.openbpm.control.entity.filter.ProcessInstanceFilter;
import io.jmix.flowui.component.datetimepicker.TypedDateTimePicker;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.component.grid.DataGridColumn;
import io.jmix.flowui.model.InstanceContainer;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static io.openbpm.control.view.util.JsUtils.SET_DEFAULT_TIME_SCRIPT;

public class StartTimeHeaderFilter extends ProcessInstanceDataGridHeaderFilter {

    private TypedDateTimePicker<LocalDateTime> startTimeAfter;
    private TypedDateTimePicker<LocalDateTime> startTimeBefore;

    public StartTimeHeaderFilter(DataGrid<ProcessInstanceData> dataGrid,
                                 DataGridColumn<ProcessInstanceData> column, InstanceContainer<ProcessInstanceFilter> filterDc) {
        super(dataGrid, column, filterDc);
    }

    @Override
    protected Component createFilterComponent() {
        Component startTimeAfterFilter = createStartTimeAfterFilter();
        Component startTimeBeforeFilter = createStartTimeBeforeFilter();

        VerticalLayout rootLayout = uiComponents.create(VerticalLayout.class);
        rootLayout.setPadding(false);
        rootLayout.setSpacing(false);
        rootLayout.add(startTimeAfterFilter, startTimeBeforeFilter);

        return rootLayout;
    }

    @Override
    public void apply() {
        LocalDateTime startTimeBefore = this.startTimeBefore.getValue();
        if (startTimeBefore != null) {
            ZoneId zoneId = this.startTimeBefore.getZoneId();
            ZoneId zone = zoneId != null ? zoneId : ZoneId.systemDefault();
            filterDc.getItem().setStartTimeBefore(startTimeBefore.atZone(zone).toOffsetDateTime());
        } else {
            filterDc.getItem().setStartTimeBefore(null);
        }

        LocalDateTime startTimeAfter = this.startTimeAfter.getValue();
        if (startTimeAfter != null) {
            ZoneId zoneId = this.startTimeAfter.getZoneId();
            ZoneId zone = zoneId != null ? zoneId : ZoneId.systemDefault();
            filterDc.getItem().setStartTimeAfter(startTimeAfter.atZone(zone).toOffsetDateTime());
        } else {
            filterDc.getItem().setStartTimeAfter(null);
        }

        filterButton.getElement().setAttribute(COLUMN_FILTER_BUTTON_ACTIVATED_ATTRIBUTE_NAME, startTimeAfter != null
                || startTimeBefore != null);
    }

    @Override
    protected void resetFilterValues() {
        startTimeAfter.clear();
        startTimeBefore.clear();
    }

    @SuppressWarnings("unchecked")
    private Component createStartTimeBeforeFilter() {
        startTimeBefore = uiComponents.create(TypedDateTimePicker.class);
        startTimeBefore.setMax(LocalDateTime.now());
        startTimeBefore.setDatePlaceholder(messages.getMessage(getClass(), "selectDate"));
        startTimeBefore.setTimePlaceholder(messages.getMessage(getClass(), "selectTime"));
        startTimeBefore.setLabel(messages.getMessage(ProcessInstanceFilter.class, "ProcessInstanceFilter.startTimeBefore"));
        setDefaultTime(startTimeBefore);

        return startTimeBefore;
    }

    @SuppressWarnings("unchecked")
    private Component createStartTimeAfterFilter() {
        startTimeAfter = uiComponents.create(TypedDateTimePicker.class);
        startTimeAfter.setDatePlaceholder(messages.getMessage(getClass(), "selectDate"));
        startTimeAfter.setTimePlaceholder(messages.getMessage(getClass(), "selectTime"));
        startTimeAfter.setMax(LocalDateTime.now());
        startTimeAfter.setLabel(messages.getMessage(ProcessInstanceFilter.class, "ProcessInstanceFilter.startTimeAfter"));
        setDefaultTime(startTimeAfter);


        return startTimeAfter;
    }

    private void setDefaultTime(TypedDateTimePicker<LocalDateTime> dateTimePicker) {
        dateTimePicker.getElement().executeJs(SET_DEFAULT_TIME_SCRIPT);
    }
}
