/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.control.view.incidentdata;

import com.vaadin.flow.component.grid.Grid;
import io.jmix.flowui.component.grid.DataGridColumn;
import io.jmix.flowui.model.InstanceContainer;
import io.flowset.control.entity.filter.IncidentFilter;
import io.flowset.control.entity.incident.IncidentData;
import io.flowset.control.uicomponent.ContainerDataGridHeaderFilter;

public abstract class IncidentHeaderFilter extends ContainerDataGridHeaderFilter<IncidentFilter, IncidentData> {
    public IncidentHeaderFilter(Grid<IncidentData> dataGrid,
                                DataGridColumn<IncidentData> column,
                                InstanceContainer<IncidentFilter> filterDc) {
        super(dataGrid, column, filterDc);
    }
}