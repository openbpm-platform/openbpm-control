/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.view.incidentdata.filter;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.jmix.flowui.component.datetimepicker.TypedDateTimePicker;
import io.jmix.flowui.component.grid.DataGridColumn;
import io.jmix.flowui.model.InstanceContainer;
import io.openbpm.control.entity.filter.IncidentFilter;
import io.openbpm.control.entity.incident.IncidentData;
import io.openbpm.control.view.incidentdata.IncidentHeaderFilter;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static io.openbpm.control.view.util.JsUtils.SET_DEFAULT_TIME_SCRIPT;

public class IncidentTimestampHeaderFilter extends IncidentHeaderFilter {

    private TypedDateTimePicker<LocalDateTime> timestampAfter;
    private TypedDateTimePicker<LocalDateTime> timestampBefore;

    public IncidentTimestampHeaderFilter(Grid<IncidentData> dataGrid, DataGridColumn<IncidentData> column,
                                         InstanceContainer<IncidentFilter> filterDc) {
        super(dataGrid, column, filterDc);
    }

    @Override
    protected Component createFilterComponent() {
        Component dateAfterFilter = createDateAfterFilter();
        Component dateBeforeFilter = createDateBeforeFilter();

        VerticalLayout rootLayout = uiComponents.create(VerticalLayout.class);
        rootLayout.setPadding(false);
        rootLayout.setSpacing(false);
        rootLayout.add(dateAfterFilter, dateBeforeFilter);

        return rootLayout;
    }

    @Override
    public void apply() {
        LocalDateTime dateBefore = this.timestampBefore.getValue();
        if (dateBefore != null) {
            ZoneId zoneId = this.timestampBefore.getZoneId();
            ZoneId zone = zoneId != null ? zoneId : ZoneId.systemDefault();
            filterDc.getItem().setIncidentTimestampBefore(dateBefore.atZone(zone).toOffsetDateTime());
        } else {
            filterDc.getItem().setIncidentTimestampBefore(null);
        }

        LocalDateTime dateAfter = this.timestampAfter.getValue();
        if (dateAfter != null) {
            ZoneId zoneId = this.timestampAfter.getZoneId();
            ZoneId zone = zoneId != null ? zoneId : ZoneId.systemDefault();
            filterDc.getItem().setIncidentTimestampAfter(dateAfter.atZone(zone).toOffsetDateTime());
        } else {
            filterDc.getItem().setIncidentTimestampAfter(null);
        }

        filterButton.getElement().setAttribute(COLUMN_FILTER_BUTTON_ACTIVATED_ATTRIBUTE_NAME, dateAfter != null
                || dateBefore != null);
    }

    @Override
    protected void resetFilterValues() {
        timestampAfter.clear();
        timestampBefore.clear();
    }

    @SuppressWarnings("unchecked")
    private Component createDateBeforeFilter() {
        timestampBefore = uiComponents.create(TypedDateTimePicker.class);
        timestampBefore.setMax(LocalDateTime.now());
        timestampBefore.setDatePlaceholder(messages.getMessage(getClass(), "selectDate"));
        timestampBefore.setTimePlaceholder(messages.getMessage(getClass(), "selectTime"));
        timestampBefore.setLabel(messages.getMessage(IncidentFilter.class, "IncidentFilter.incidentTimestampBefore"));
        setDefaultTime(timestampBefore);

        return timestampBefore;
    }

    @SuppressWarnings("unchecked")
    private Component createDateAfterFilter() {
        timestampAfter = uiComponents.create(TypedDateTimePicker.class);
        timestampAfter.setDatePlaceholder(messages.getMessage(getClass(), "selectDate"));
        timestampAfter.setTimePlaceholder(messages.getMessage(getClass(), "selectTime"));
        timestampAfter.setMax(LocalDateTime.now());
        timestampAfter.setLabel(messages.getMessage(IncidentFilter.class, "IncidentFilter.incidentTimestampAfter"));
        setDefaultTime(timestampAfter);

        return timestampAfter;
    }

    private void setDefaultTime(TypedDateTimePicker<LocalDateTime> dateTimePicker) {
        dateTimePicker.getElement().executeJs(SET_DEFAULT_TIME_SCRIPT);
    }
}
