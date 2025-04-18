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

public class ProcessDefinitionKeyHeaderFilter
        extends ContainerDataGridHeaderFilter<DecisionInstanceFilter, HistoricDecisionInstanceShortData> {

    protected TextField processDefinitionKey;

    public ProcessDefinitionKeyHeaderFilter(DataGrid<HistoricDecisionInstanceShortData> dataGrid,
                                            DataGridColumn<HistoricDecisionInstanceShortData> column,
                                            InstanceContainer<DecisionInstanceFilter> filterDc) {
        super(dataGrid, column, filterDc);
    }

    @Override
    protected Component createFilterComponent() {
        return createProcessKeyFilter();
    }

    @Override
    protected void resetFilterValues() {
        processDefinitionKey.clear();
    }

    @Override
    public void apply() {
        String value = processDefinitionKey.getValue();
        filterDc.getItem().setProcessDefinitionKey(value);

        filterButton.getElement().setAttribute(COLUMN_FILTER_BUTTON_ACTIVATED_ATTRIBUTE_NAME, value != null);
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
