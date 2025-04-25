/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.view.processinstance.filter;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;
import io.openbpm.control.entity.processdefinition.ProcessDefinitionData;
import io.openbpm.control.entity.filter.ProcessDefinitionFilter;
import io.openbpm.control.entity.processinstance.ProcessInstanceData;
import io.openbpm.control.entity.filter.ProcessInstanceFilter;
import io.openbpm.control.service.processdefinition.ProcessDefinitionLoadContext;
import io.openbpm.control.service.processdefinition.ProcessDefinitionService;
import io.jmix.core.Metadata;
import io.jmix.flowui.component.combobox.JmixComboBox;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.component.grid.DataGridColumn;
import io.jmix.flowui.model.InstanceContainer;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ProcessHeaderFilter extends ProcessInstanceDataGridHeaderFilter {
    protected ProcessDefinitionService processDefinitionService;
    protected Metadata metadata;
    protected JmixComboBox<ProcessDefinitionData> processVersionComboBox;
    protected JmixComboBox<ProcessDefinitionData> processComboBox;
    protected Checkbox useSpecificVersionChkBox;

    public ProcessHeaderFilter(DataGrid<ProcessInstanceData> dataGrid, DataGridColumn<ProcessInstanceData> column,
                               InstanceContainer<ProcessInstanceFilter> filterDc) {
        super(dataGrid, column, filterDc);
    }


    @Autowired
    public void setProcessDefinitionService(ProcessDefinitionService processDefinitionService) {
        this.processDefinitionService = processDefinitionService;
    }

    @Autowired
    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    @Override
    protected Component createFilterComponent() {
        VerticalLayout layout = uiComponents.create(VerticalLayout.class);
        layout.addClassNames(LumoUtility.Gap.SMALL, LumoUtility.Padding.SMALL);
        layout.setSizeFull();

        processComboBox = createProcessComboBox();
        processVersionComboBox = createProcessVersionComboBox();
        useSpecificVersionChkBox = uiComponents.create(Checkbox.class);
        useSpecificVersionChkBox.setLabel(messages.getMessage(getClass(), "useSpecificVersion"));
        useSpecificVersionChkBox.addValueChangeListener(event -> {
            boolean useVersion = BooleanUtils.isTrue(event.getValue());
            processVersionComboBox.setVisible(useVersion);
            processVersionComboBox.setRequired(useVersion);
        });

        VerticalLayout versionLayout = uiComponents.create(VerticalLayout.class);
        versionLayout.setPadding(false);
        versionLayout.add(useSpecificVersionChkBox);
        versionLayout.addAndExpand(processVersionComboBox);

        updateComboBoxVisibility(useSpecificVersionChkBox.getValue());

        layout.add(processComboBox, versionLayout);
        return layout;
    }

    @Override
    public void apply() {
        ProcessDefinitionData value = null;
        boolean useVersion = BooleanUtils.isTrue(useSpecificVersionChkBox.getValue());

        if (useVersion) {
            value = processVersionComboBox.getValue();
            filterDc.getItem().setProcessDefinitionId(value != null ? value.getProcessDefinitionId() : null);
        } else {
            value = processComboBox.getValue();
            filterDc.getItem().setProcessDefinitionKey(value != null ? value.getKey() : null);
        }
        filterButton.getElement().setAttribute(COLUMN_FILTER_BUTTON_ACTIVATED_ATTRIBUTE_NAME, value != null);
    }

    protected void updateComboBoxVisibility(Boolean useVersion) {
        processVersionComboBox.setVisible(BooleanUtils.isTrue(useVersion));
    }

    protected void resetFilterValues() {
        processComboBox.setValue(null);
        useSpecificVersionChkBox.setValue(false);
        processVersionComboBox.setValue(null);
    }

    protected JmixComboBox<ProcessDefinitionData> createProcessComboBox() {
        JmixComboBox<ProcessDefinitionData> processComboBox = uiComponents.create(JmixComboBox.class);
        processComboBox.addClassNames(LumoUtility.Padding.Top.NONE);
        processComboBox.setClearButtonVisible(true);
        processComboBox.setLabel(messages.getMessage(getClass(), "processFilterLabel"));
        processComboBox.setMinWidth("30em");
        processComboBox.addValueChangeListener(event -> {
            processVersionComboBox.setValue(null);
            updateVersions(processVersionComboBox);
        });
        processComboBox.setItemsFetchCallback(query -> {
            ProcessDefinitionFilter filter = metadata.create(ProcessDefinitionFilter.class);
            filter.setKeyLike(query.getFilter().orElse(null));
            filter.setLatestVersionOnly(true);
            ProcessDefinitionLoadContext context = new ProcessDefinitionLoadContext().setFilter(filter);
            context.setMaxResults(query.getLimit());
            context.setFirstResult(query.getOffset());

            return processDefinitionService.findAll(context).stream();
        });
        processComboBox.setItemLabelGenerator(ProcessDefinitionData::getKey);
        return processComboBox;
    }

    protected JmixComboBox<ProcessDefinitionData> createProcessVersionComboBox() {
        JmixComboBox<ProcessDefinitionData> processVersionComboBox = uiComponents.create(JmixComboBox.class);
        processVersionComboBox.addClassNames(LumoUtility.Padding.Top.NONE);
        processVersionComboBox.setClearButtonVisible(true);
        processVersionComboBox.setPlaceholder(messages.getMessage(getClass(), "enterProcessVersionValue"));
        processVersionComboBox.setLabel(messages.getMessage(getClass(), "processVersionFilterLabel"));
        processVersionComboBox.setMinWidth("10em");
        processVersionComboBox.setItemLabelGenerator(item -> {
            Integer version = item.getVersion();
            return version != null ? String.valueOf(version) : null;
        });
        updateVersions(processVersionComboBox);


        return processVersionComboBox;
    }

    private void updateVersions(JmixComboBox<ProcessDefinitionData> processDefinitionComboBox) {
        if (processComboBox.getValue() != null) {
            ProcessDefinitionFilter filter = metadata.create(ProcessDefinitionFilter.class);
            filter.setKey(processComboBox.getValue().getKey());
            filter.setLatestVersionOnly(false);
            ProcessDefinitionLoadContext context = new ProcessDefinitionLoadContext().setFilter(filter);
            processDefinitionComboBox.setItems(processDefinitionService.findAll(context));
        } else {
            processDefinitionComboBox.setItems(List.of());
        }
    }

}
