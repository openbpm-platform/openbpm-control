/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.control.view.processinstance.filter;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.flowset.control.entity.processinstance.ProcessInstanceData;
import io.flowset.control.entity.filter.ProcessInstanceFilter;
import io.jmix.flowui.component.datetimepicker.TypedDateTimePicker;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.component.grid.DataGridColumn;
import io.jmix.flowui.model.InstanceContainer;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static io.flowset.control.view.util.JsUtils.SET_DEFAULT_TIME_SCRIPT;

public class EndTimeHeaderFilter extends ProcessInstanceDataGridHeaderFilter {

    protected TypedDateTimePicker<LocalDateTime> endTimeBefore;
    protected TypedDateTimePicker<LocalDateTime> endTimeAfter;

    public EndTimeHeaderFilter(DataGrid<ProcessInstanceData> dataGrid, DataGridColumn<ProcessInstanceData> column,
                               InstanceContainer<ProcessInstanceFilter> filterDc) {
        super(dataGrid, column, filterDc);
    }


    @Override
    protected Component createFilterComponent() {
        Component endTimeAfterFilter = createEndTimeAfterFilter();
        Component endTimeBeforeFilter = createEndTimeBeforeFilter();

        VerticalLayout rootLayout = uiComponents.create(VerticalLayout.class);
        rootLayout.setPadding(false);
        rootLayout.setSpacing(false);
        rootLayout.add(endTimeAfterFilter, endTimeBeforeFilter);

        return rootLayout;
    }


    @Override
    protected void resetFilterValues() {
        endTimeBefore.clear();
        endTimeAfter.clear();
    }

    @Override
    public void apply() {
        LocalDateTime endTimeBefore = this.endTimeBefore.getValue();
        if (endTimeBefore != null) {
            ZoneId zoneId = this.endTimeBefore.getZoneId();
            ZoneId zone = zoneId != null ? zoneId : ZoneId.systemDefault();
            filterDc.getItem().setEndTimeBefore(endTimeBefore.atZone(zone).toOffsetDateTime());
        } else {
            filterDc.getItem().setEndTimeBefore(null);
        }

        LocalDateTime endTimeAfter = this.endTimeAfter.getValue();
        if (endTimeAfter != null) {
            ZoneId zoneId = this.endTimeAfter.getZoneId();
            ZoneId zone = zoneId != null ? zoneId : ZoneId.systemDefault();
            filterDc.getItem().setEndTimeAfter(endTimeAfter.atZone(zone).toOffsetDateTime());
        } else {
            filterDc.getItem().setEndTimeAfter(null);
        }

        filterButton.getElement().setAttribute(COLUMN_FILTER_BUTTON_ACTIVATED_ATTRIBUTE_NAME, endTimeAfter != null
                || endTimeBefore != null);
    }

    protected Component createEndTimeBeforeFilter() {
        endTimeBefore = uiComponents.create(TypedDateTimePicker.class);
        endTimeBefore.setMax(LocalDateTime.now());
        endTimeBefore.setDatePlaceholder(messages.getMessage(getClass(), "selectDate"));
        endTimeBefore.setTimePlaceholder(messages.getMessage(getClass(), "selectTime"));
        endTimeBefore.setLabel(messages.getMessage(ProcessInstanceFilter.class, "ProcessInstanceFilter.endTimeBefore"));

        return endTimeBefore;
    }

    protected Component createEndTimeAfterFilter() {
        endTimeAfter = uiComponents.create(TypedDateTimePicker.class);
        endTimeAfter.setMax(LocalDateTime.now());
        endTimeAfter.setDatePlaceholder(messages.getMessage(getClass(), "selectDate"));
        endTimeAfter.setTimePlaceholder(messages.getMessage(getClass(), "selectTime"));
        endTimeAfter.setLabel(messages.getMessage(ProcessInstanceFilter.class, "ProcessInstanceFilter.endTimeAfter"));
        setDefaultTime(endTimeAfter);

        return endTimeAfter;
    }

    private void setDefaultTime(TypedDateTimePicker<LocalDateTime> dateTimePicker) {
        dateTimePicker.getElement().executeJs(SET_DEFAULT_TIME_SCRIPT);
    }
}
