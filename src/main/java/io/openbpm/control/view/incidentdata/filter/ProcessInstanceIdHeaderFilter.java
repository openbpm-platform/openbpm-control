/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.view.incidentdata.filter;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.textfield.TextField;
import io.jmix.flowui.component.grid.DataGridColumn;
import io.jmix.flowui.model.InstanceContainer;
import io.micrometer.common.util.StringUtils;
import io.openbpm.control.entity.filter.IncidentFilter;
import io.openbpm.control.entity.incident.IncidentData;
import io.openbpm.control.view.incidentdata.IncidentHeaderFilter;

public class ProcessInstanceIdHeaderFilter extends IncidentHeaderFilter {

    protected TextField processInstanceIdField;

    public ProcessInstanceIdHeaderFilter(Grid<IncidentData> dataGrid,
                                         DataGridColumn<IncidentData> column,
                                         InstanceContainer<IncidentFilter> filterDc) {
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
        boolean notEmptyValue = StringUtils.isNotEmpty(value);
        if (notEmptyValue) {
            filterDc.getItem().setProcessInstanceId(value);
        } else {
            filterDc.getItem().setProcessInstanceId(null);
        }


        filterButton.getElement().setAttribute(COLUMN_FILTER_BUTTON_ACTIVATED_ATTRIBUTE_NAME, notEmptyValue);
    }

    protected TextField createProcessInstanceIdFilter() {
        processInstanceIdField = uiComponents.create(TextField.class);
        processInstanceIdField.setWidthFull();
        processInstanceIdField.setMinWidth("30em");
        processInstanceIdField.setClearButtonVisible(true);
        processInstanceIdField.setLabel(messages.getMessage(IncidentFilter.class, "IncidentFilter.processInstanceId"));
        processInstanceIdField.setPlaceholder(messages.getMessage(getClass(), "processInstanceId.placeholder"));

        return processInstanceIdField;
    }
}
