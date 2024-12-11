/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.view.processinstance.filter;

import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.component.grid.DataGridColumn;
import io.jmix.flowui.model.InstanceContainer;
import io.openbpm.control.entity.filter.ProcessInstanceFilter;
import io.openbpm.control.entity.processinstance.ProcessInstanceData;
import io.openbpm.control.uicomponent.ContainerDataGridHeaderFilter;

public abstract class ProcessInstanceDataGridHeaderFilter extends ContainerDataGridHeaderFilter<ProcessInstanceFilter, ProcessInstanceData> {

    public ProcessInstanceDataGridHeaderFilter(DataGrid<ProcessInstanceData> dataGrid,
                                               DataGridColumn<ProcessInstanceData> column,
                                               InstanceContainer<ProcessInstanceFilter> filterDc) {
        super(dataGrid, column, filterDc);
    }
}
