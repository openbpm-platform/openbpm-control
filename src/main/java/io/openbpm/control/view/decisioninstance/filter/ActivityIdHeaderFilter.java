/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.view.decisioninstance.filter;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.textfield.TextField;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.component.grid.DataGridColumn;
import io.jmix.flowui.model.InstanceContainer;
import io.openbpm.control.entity.decisioninstance.HistoricDecisionInstanceShortData;
import io.openbpm.control.entity.filter.DecisionInstanceFilter;
import io.openbpm.control.uicomponent.ContainerDataGridHeaderFilter;

public class ActivityIdHeaderFilter
        extends ContainerDataGridHeaderFilter<DecisionInstanceFilter, HistoricDecisionInstanceShortData> {

    protected TextField activityId;

    public ActivityIdHeaderFilter(DataGrid<HistoricDecisionInstanceShortData> dataGrid,
                                  DataGridColumn<HistoricDecisionInstanceShortData> column,
                                  InstanceContainer<DecisionInstanceFilter> filterDc) {
        super(dataGrid, column, filterDc);
    }

    @Override
    protected Component createFilterComponent() {
        return createActivityIdFilter();
    }

    @Override
    protected void resetFilterValues() {
        activityId.clear();
    }

    @Override
    public void apply() {
        String value = activityId.getValue();
        filterDc.getItem().setActivityId(value);

        filterButton.getElement().setAttribute(COLUMN_FILTER_BUTTON_ACTIVATED_ATTRIBUTE_NAME, value != null);
    }

    protected TextField createActivityIdFilter() {
        activityId = uiComponents.create(TextField.class);
        activityId.setWidthFull();
        activityId.setMinWidth("30em");
        activityId.setClearButtonVisible(true);
        activityId.setLabel(messages.getMessage(DecisionInstanceFilter.class,
                "DecisionInstanceFilter.activityId"));
        activityId.setPlaceholder(messages.getMessage(getClass(), "activityId.placeHolder"));

        return activityId;
    }
}
