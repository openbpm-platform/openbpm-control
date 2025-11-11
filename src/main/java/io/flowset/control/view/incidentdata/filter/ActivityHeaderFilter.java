/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.control.view.incidentdata.filter;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.textfield.TextField;
import io.jmix.flowui.component.grid.DataGridColumn;
import io.jmix.flowui.model.InstanceContainer;
import io.flowset.control.entity.filter.IncidentFilter;
import io.flowset.control.entity.incident.IncidentData;
import io.flowset.control.view.incidentdata.IncidentHeaderFilter;
import org.apache.commons.lang3.StringUtils;

public class ActivityHeaderFilter extends IncidentHeaderFilter {

    protected TextField activityIdField;

    public ActivityHeaderFilter(Grid<IncidentData> dataGrid,
                                DataGridColumn<IncidentData> column,
                                InstanceContainer<IncidentFilter> filterDc) {
        super(dataGrid, column, filterDc);
    }

    @Override
    protected Component createFilterComponent() {
        return createActivityFilter();
    }

    @Override
    protected void resetFilterValues() {
        activityIdField.clear();
    }

    @Override
    public void apply() {
        String value = activityIdField.getValue();
        boolean emptyValue = StringUtils.isEmpty(value);
        if (emptyValue) {
            value = null;
        }
        filterDc.getItem().setActivityId(value);

        filterButton.getElement().setAttribute(COLUMN_FILTER_BUTTON_ACTIVATED_ATTRIBUTE_NAME, !emptyValue);
    }

    protected TextField createActivityFilter() {
        activityIdField = uiComponents.create(TextField.class);
        activityIdField.setWidthFull();
        activityIdField.setMinWidth("30em");
        activityIdField.setClearButtonVisible(true);
        activityIdField.setLabel(messages.getMessage(IncidentFilter.class, "IncidentFilter.activityId"));
        activityIdField.setPlaceholder(messages.getMessage(getClass(), "activityId.placeholder"));

        return activityIdField;
    }
}
