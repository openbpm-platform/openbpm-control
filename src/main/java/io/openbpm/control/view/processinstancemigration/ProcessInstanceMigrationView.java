/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.view.processinstancemigration;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.openbpm.control.entity.processdefinition.ProcessDefinitionData;
import io.openbpm.control.entity.processinstance.ProcessInstanceData;
import io.openbpm.control.service.processinstance.MigrationService;
import io.openbpm.control.service.processdefinition.ProcessDefinitionService;
import io.jmix.flowui.Dialogs;
import io.jmix.flowui.component.textfield.TypedTextField;
import io.jmix.flowui.view.*;
import io.openbpm.control.service.processinstance.ProcessInstanceService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route(value = "bpm/processinstancemigration", layout = DefaultMainViewParent.class)
@ViewController(ProcessInstanceMigrationView.ID)
@ViewDescriptor("process-instance-migration-view.xml")
public class ProcessInstanceMigrationView extends StandardView {

    public static final String ID = "bpm_ProcessInstanceMigration";

    @Autowired
    protected ProcessDefinitionService processDefinitionService;
    @Autowired
    private ProcessInstanceService processInstanceService;
    @Autowired
    protected MigrationService migrationService;
    @Autowired
    protected Dialogs dialogs;

    @ViewComponent
    protected MessageBundle messageBundle;
    @ViewComponent
    protected ComboBox<ProcessDefinitionData> processDefinitionVersionComboBox;
    @ViewComponent
    protected ComboBox<String> processDefinitionKeyComboBox;
    @ViewComponent
    protected HorizontalLayout rootHBox;
    @ViewComponent
    protected Icon arrowIcon;
    @ViewComponent
    protected TypedTextField<Object> sourceDefinitionKeyField;
    @ViewComponent
    protected TypedTextField<Integer> sourceDefinitionVersionField;
    @ViewComponent
    private JmixButton migrateBtn;
    @ViewComponent
    private HorizontalLayout migrationWarningPanel;

    protected ProcessInstanceData processInstanceData;
    protected ProcessDefinitionData processDefinitionData;

    @SuppressWarnings("LombokSetterMayBeUsed")
    public void setProcessDefinitionData(ProcessDefinitionData processDefinitionData) {
        this.processDefinitionData = processDefinitionData;
    }

    @SuppressWarnings("LombokSetterMayBeUsed")
    public void setProcessInstanceData(ProcessInstanceData processInstanceData) {
        this.processInstanceData = processInstanceData;
    }

    @Subscribe
    public void onInit(final InitEvent event) {
        addClassNames(LumoUtility.Padding.Top.NONE,
                LumoUtility.Padding.Left.LARGE,
                LumoUtility.Padding.Right.LARGE,
                LumoUtility.Padding.Bottom.MEDIUM);
        arrowIcon.addClassNames(LumoUtility.TextColor.SECONDARY);
        rootHBox.getChildren()
                .filter(component -> component instanceof VerticalLayout)
                .forEach(component ->
                        component.addClassNames(LumoUtility.Border.ALL,
                                LumoUtility.BorderRadius.LARGE,
                                LumoUtility.BorderColor.CONTRAST_30)
                );
    }

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        sourceDefinitionKeyField.setValue(processDefinitionData.getKey());
        sourceDefinitionVersionField.setTypedValue(processDefinitionData.getVersion());
        List<String> processDefinitionKeys = processDefinitionService.findLatestVersions()
                .stream()
                .map(ProcessDefinitionData::getKey)
                .toList();
        processDefinitionKeyComboBox.setItems(processDefinitionKeys);

        processDefinitionKeyComboBox.setValue(processDefinitionData.getKey());

        if (processInstanceData != null) {
            return;
        }

        long runningInstancesCount = processInstanceService.getCountByProcessDefinitionId(
                processDefinitionData.getProcessDefinitionId());
        if (runningInstancesCount == 0) {
            migrateBtn.setEnabled(false);
            migrationWarningPanel.setVisible(true);
        }
    }

    @Subscribe("processDefinitionKeyComboBox")
    public void onProcessDefinitionKeyComboBoxValueChange(AbstractField.ComponentValueChangeEvent<ComboBox<String>, String> event) {
        String processDefinitionKey = event.getValue();
        List<ProcessDefinitionData> definitions = processDefinitionService.findAllByKey(processDefinitionKey);
        processDefinitionVersionComboBox.setItems(definitions);
        processDefinitionVersionComboBox.setItemLabelGenerator(item -> {
            Integer version = item.getVersion();
            return version != null ? String.valueOf(version) : null;
        });

        if (!definitions.isEmpty()) {
            processDefinitionVersionComboBox.setValue(definitions.getFirst());
        }
    }

    @Subscribe("migrateBtn")
    public void onMigrateBtnClick(ClickEvent<Button> event) {
        if (processInstanceData != null) {
            migrateSingleProcessInstance();
        } else {
            migrateAllProcessInstances();
        }
    }

    @Subscribe("cancelBtn")
    public void onCancelBtnClick(ClickEvent<Button> event) {
        close(StandardOutcome.DISCARD);
    }

    protected void migrateSingleProcessInstance() {
        ProcessDefinitionData dstProcessDefinition = processDefinitionVersionComboBox.getValue();
        String destinationProcessDefinitionId = dstProcessDefinition.getId();
        String processInstanceId = processInstanceData.getId();
        List<String> validationMessages = migrationService.validateMigrationOfSingleProcessInstance(processInstanceId,
                destinationProcessDefinitionId);
        if (validationMessages.isEmpty()) {
            migrationService.migrateSingleProcessInstance(processInstanceId, destinationProcessDefinitionId);
            close(StandardOutcome.SAVE);
        } else {
            displayValidationError(validationMessages);
        }
    }

    protected void migrateAllProcessInstances() {
        ProcessDefinitionData dstProcessDefinition = processDefinitionVersionComboBox.getValue();
        String dstProcessDefinitionId = dstProcessDefinition.getId();
        String srcProcessDefinitionId = processDefinitionData.getId();
        List<String> validationMessages = migrationService.validateMigrationOfProcessInstances(srcProcessDefinitionId, dstProcessDefinitionId);
        if (validationMessages.isEmpty()) {
            migrationService.migrateAllProcessInstances(srcProcessDefinitionId, dstProcessDefinitionId);
            close(StandardOutcome.SAVE);
        } else {
            displayValidationError(validationMessages);
        }
    }

    protected void displayValidationError(List<String> validationMessages) {
        String combinedErrorMsg = String.join("\n", validationMessages);
        dialogs.createMessageDialog()
                .withHeader(messageBundle.getMessage("processInstanceMigrationView.migrationValidationError"))
                .withText(combinedErrorMsg)
                .withHeight("12.5em")
                .open();
    }
}