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

public class BusinessKeyHeaderFilter extends ProcessInstanceDataGridHeaderFilter {

    protected TextField businessKey;

    public BusinessKeyHeaderFilter(DataGrid<ProcessInstanceData> dataGrid,
                                   DataGridColumn<ProcessInstanceData> column, InstanceContainer<ProcessInstanceFilter> filterDc) {
        super(dataGrid, column, filterDc);
    }


    @Override
    protected Component createFilterComponent() {
        return createBusinessKeyFilter();
    }

    @Override
    protected void resetFilterValues() {
        businessKey.clear();
    }

    @Override
    public void apply() {
        String value = businessKey.getValue();
        filterDc.getItem().setBusinessKeyLike(value);

        filterButton.getElement().setAttribute(COLUMN_FILTER_BUTTON_ACTIVATED_ATTRIBUTE_NAME, value != null);
    }


    protected TextField createBusinessKeyFilter() {
        businessKey = uiComponents.create(TextField.class);
        businessKey.setWidthFull();
        businessKey.setMinWidth("30em");
        businessKey.setClearButtonVisible(true);
        businessKey.setLabel(messages.getMessage(ProcessInstanceFilter.class, "ProcessInstanceFilter.businessKeyLike"));
        businessKey.setPlaceholder(messages.getMessage(getClass(), "businessKey.placeholder"));

        return businessKey;
    }
}
