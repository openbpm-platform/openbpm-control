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
import io.openbpm.control.entity.filter.IncidentFilter;
import io.openbpm.control.entity.incident.IncidentData;
import io.openbpm.control.view.incidentdata.IncidentHeaderFilter;
import org.apache.commons.lang3.StringUtils;

public class MessageHeaderFilter extends IncidentHeaderFilter {

    protected TextField messageField;

    public MessageHeaderFilter(Grid<IncidentData> dataGrid,
                               DataGridColumn<IncidentData> column,
                               InstanceContainer<IncidentFilter> filterDc) {
        super(dataGrid, column, filterDc);
    }


    @Override
    protected Component createFilterComponent() {
        return createMessageFilter();
    }

    @Override
    protected void resetFilterValues() {
        messageField.clear();
    }

    @Override
    public void apply() {
        String value = messageField.getValue();
        if (StringUtils.isEmpty(value)) {
            value = null;
        }
        filterDc.getItem().setIncidentMessageLike(value);

        filterButton.getElement().setAttribute(COLUMN_FILTER_BUTTON_ACTIVATED_ATTRIBUTE_NAME, value != null);
    }

    protected TextField createMessageFilter() {
        messageField = uiComponents.create(TextField.class);
        messageField.setWidthFull();
        messageField.setMinWidth("30em");
        messageField.setClearButtonVisible(true);
        messageField.setLabel(messages.getMessage(IncidentFilter.class, "IncidentFilter.incidentMessageLike"));
        messageField.setPlaceholder(messages.getMessage(getClass(), "incidentMessage.placeholder"));

        return messageField;
    }
}
