/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.view.incidentdata;

import com.vaadin.flow.component.grid.Grid;
import io.jmix.flowui.component.grid.DataGridColumn;
import io.jmix.flowui.model.InstanceContainer;
import io.openbpm.control.entity.filter.IncidentFilter;
import io.openbpm.control.entity.incident.IncidentData;
import io.openbpm.control.uicomponent.ContainerDataGridHeaderFilter;

public abstract class IncidentHeaderFilter extends ContainerDataGridHeaderFilter<IncidentFilter, IncidentData> {
    public IncidentHeaderFilter(Grid<IncidentData> dataGrid,
                                DataGridColumn<IncidentData> column,
                                InstanceContainer<IncidentFilter> filterDc) {
        super(dataGrid, column, filterDc);
    }
}