/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.view.processinstance.filter;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.textfield.TextField;
import io.openbpm.control.entity.filter.ProcessInstanceFilter;
import io.openbpm.control.entity.processinstance.ProcessInstanceData;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.component.grid.DataGridColumn;
import io.jmix.flowui.model.InstanceContainer;

public class IdHeaderFilter extends ProcessInstanceDataGridHeaderFilter {

    protected TextField processInstanceIdField;

    public IdHeaderFilter(DataGrid<ProcessInstanceData> dataGrid,
                          DataGridColumn<ProcessInstanceData> column, InstanceContainer<ProcessInstanceFilter> filterDc) {
        super(dataGrid, column, filterDc);
    }

    @Override
    protected Component createFilterComponent() {
        return createProcessInstanceIdFilter();
    }

    @Override
    protected void resetFilterValues() {
        processInstanceIdField.clear();
    }

    @Override
    public void apply() {
        String value = processInstanceIdField.getValue();
        filterDc.getItem().setProcessInstanceId(value);

        filterButton.getElement().setAttribute(COLUMN_FILTER_BUTTON_ACTIVATED_ATTRIBUTE_NAME, value != null);
    }

    protected TextField createProcessInstanceIdFilter() {
        processInstanceIdField = uiComponents.create(TextField.class);
        processInstanceIdField.setWidthFull();
        processInstanceIdField.setMinWidth("30em");
        processInstanceIdField.setClearButtonVisible(true);
        processInstanceIdField.setLabel(messages.getMessage(ProcessInstanceFilter.class, "ProcessInstanceFilter.processInstanceId"));
        processInstanceIdField.setPlaceholder(messages.getMessage(getClass(), "processInstanceId.placeholder"));

        return processInstanceIdField;
    }
}
