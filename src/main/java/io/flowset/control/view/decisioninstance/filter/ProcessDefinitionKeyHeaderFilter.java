/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.control.view.decisioninstance.filter;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.textfield.TextField;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.component.grid.DataGridColumn;
import io.jmix.flowui.model.InstanceContainer;
import io.flowset.control.entity.decisioninstance.HistoricDecisionInstanceShortData;
import io.flowset.control.entity.filter.DecisionInstanceFilter;
import io.flowset.control.uicomponent.ContainerDataGridHeaderFilter;

public class ProcessDefinitionKeyHeaderFilter
        extends ContainerDataGridHeaderFilter<DecisionInstanceFilter, HistoricDecisionInstanceShortData> {

    protected TextField processDefinitionKey;

    public ProcessDefinitionKeyHeaderFilter(DataGrid<HistoricDecisionInstanceShortData> dataGrid,
                                            DataGridColumn<HistoricDecisionInstanceShortData> column,
                                            InstanceContainer<DecisionInstanceFilter> filterDc) {
        super(dataGrid, column, filterDc);
    }

    @Override
    public void apply() {
        String value = processDefinitionKey.getValue();
        filterDc.getItem().setProcessDefinitionKey(value);

        filterButton.getElement().setAttribute(COLUMN_FILTER_BUTTON_ACTIVATED_ATTRIBUTE_NAME, value != null);
    }

    @Override
    protected Component createFilterComponent() {
        return createProcessKeyFilter();
    }

    @Override
    protected void resetFilterValues() {
        processDefinitionKey.clear();
    }

    protected TextField createProcessKeyFilter() {
        processDefinitionKey = uiComponents.create(TextField.class);
        processDefinitionKey.setWidthFull();
        processDefinitionKey.setMinWidth("30em");
        processDefinitionKey.setClearButtonVisible(true);
        processDefinitionKey.setLabel(messages.getMessage(DecisionInstanceFilter.class,
                "DecisionInstanceFilter.processDefinitionKey"));
        processDefinitionKey.setPlaceholder(messages.getMessage(getClass(), "processDefinitionKey.placeHolder"));
        return processDefinitionKey;
    }
}
