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

public class ProcessInstanceIdHeaderFilter
        extends ContainerDataGridHeaderFilter<DecisionInstanceFilter, HistoricDecisionInstanceShortData> {

    protected TextField processInstanceId;

    public ProcessInstanceIdHeaderFilter(DataGrid<HistoricDecisionInstanceShortData> dataGrid,
                                         DataGridColumn<HistoricDecisionInstanceShortData> column,
                                         InstanceContainer<DecisionInstanceFilter> filterDc) {
        super(dataGrid, column, filterDc);
    }

    @Override
    protected Component createFilterComponent() {
        return createProcessInstanceIdFilter();
    }

    @Override
    protected void resetFilterValues() {
        processInstanceId.clear();
    }

    @Override
    public void apply() {
        String value = processInstanceId.getValue();
        filterDc.getItem().setProcessInstanceId(value);

        filterButton.getElement().setAttribute(COLUMN_FILTER_BUTTON_ACTIVATED_ATTRIBUTE_NAME, value != null);
    }

    protected TextField createProcessInstanceIdFilter() {
        processInstanceId = uiComponents.create(TextField.class);
        processInstanceId.setWidthFull();
        processInstanceId.setMinWidth("30em");
        processInstanceId.setClearButtonVisible(true);
        processInstanceId.setLabel(messages.getMessage(DecisionInstanceFilter.class,
                "DecisionInstanceFilter.processInstanceId"));
        processInstanceId.setPlaceholder(messages.getMessage(getClass(), "processInstanceId.placeHolder"));

        return processInstanceId;
    }
}
