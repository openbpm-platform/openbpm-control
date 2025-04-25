/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.view.decisioninstance.filter;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.jmix.flowui.component.datetimepicker.TypedDateTimePicker;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.component.grid.DataGridColumn;
import io.jmix.flowui.model.InstanceContainer;
import io.openbpm.control.entity.decisioninstance.HistoricDecisionInstanceShortData;
import io.openbpm.control.entity.filter.DecisionInstanceFilter;
import io.openbpm.control.uicomponent.ContainerDataGridHeaderFilter;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static io.openbpm.control.view.util.JsUtils.SET_DEFAULT_TIME_SCRIPT;

public class EvaluationTimeHeaderFilter
        extends ContainerDataGridHeaderFilter<DecisionInstanceFilter, HistoricDecisionInstanceShortData> {

    private TypedDateTimePicker<LocalDateTime> evaluatedAfter;
    private TypedDateTimePicker<LocalDateTime> evaluatedBefore;

    public EvaluationTimeHeaderFilter(DataGrid<HistoricDecisionInstanceShortData> dataGrid,
                                      DataGridColumn<HistoricDecisionInstanceShortData> column,
                                      InstanceContainer<DecisionInstanceFilter> filterDc) {
        super(dataGrid, column, filterDc);
    }

    @Override
    public void apply() {
        LocalDateTime startTimeBefore = this.evaluatedBefore.getValue();
        if (startTimeBefore != null) {
            ZoneId zoneId = this.evaluatedBefore.getZoneId();
            ZoneId zone = zoneId != null ? zoneId : ZoneId.systemDefault();
            filterDc.getItem().setEvaluatedBefore(startTimeBefore.atZone(zone).toOffsetDateTime());
        } else {
            filterDc.getItem().setEvaluatedBefore(null);
        }
        LocalDateTime startTimeAfter = this.evaluatedAfter.getValue();
        if (startTimeAfter != null) {
            ZoneId zoneId = this.evaluatedAfter.getZoneId();
            ZoneId zone = zoneId != null ? zoneId : ZoneId.systemDefault();
            filterDc.getItem().setEvaluatedAfter(startTimeAfter.atZone(zone).toOffsetDateTime());
        } else {
            filterDc.getItem().setEvaluatedAfter(null);
        }
        filterButton.getElement().setAttribute(COLUMN_FILTER_BUTTON_ACTIVATED_ATTRIBUTE_NAME, startTimeAfter != null
                || startTimeBefore != null);
    }

    @Override
    protected Component createFilterComponent() {
        Component startTimeAfterFilter = createEvaluatedAfterFilter();
        Component startTimeBeforeFilter = createEvaluatedBeforeFilter();
        VerticalLayout rootLayout = uiComponents.create(VerticalLayout.class);
        rootLayout.setPadding(false);
        rootLayout.setSpacing(false);
        rootLayout.add(startTimeAfterFilter, startTimeBeforeFilter);
        return rootLayout;
    }

    @Override
    protected void resetFilterValues() {
        evaluatedAfter.clear();
        evaluatedBefore.clear();
    }

    @SuppressWarnings("unchecked")
    private Component createEvaluatedBeforeFilter() {
        evaluatedBefore = uiComponents.create(TypedDateTimePicker.class);
        evaluatedBefore.setMax(LocalDateTime.now());
        evaluatedBefore.setDatePlaceholder(messages.getMessage(getClass(), "selectDate"));
        evaluatedBefore.setTimePlaceholder(messages.getMessage(getClass(), "selectTime"));
        evaluatedBefore.setLabel(messages.getMessage(getClass(), "evaluatedBefore.label"));
        setDefaultTime(evaluatedBefore);
        return evaluatedBefore;
    }

    @SuppressWarnings("unchecked")
    private Component createEvaluatedAfterFilter() {
        evaluatedAfter = uiComponents.create(TypedDateTimePicker.class);
        evaluatedAfter.setMax(LocalDateTime.now());
        evaluatedAfter.setDatePlaceholder(messages.getMessage(getClass(), "selectDate"));
        evaluatedAfter.setTimePlaceholder(messages.getMessage(getClass(), "selectTime"));
        evaluatedAfter.setLabel(messages.getMessage(getClass(), "evaluatedAfter.label"));
        setDefaultTime(evaluatedAfter);
        return evaluatedAfter;
    }

    private void setDefaultTime(TypedDateTimePicker<LocalDateTime> dateTimePicker) {
        dateTimePicker.getElement().executeJs(SET_DEFAULT_TIME_SCRIPT);
    }
}
