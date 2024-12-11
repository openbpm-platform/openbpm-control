/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.view.processdefinition;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.router.Route;
import io.openbpm.control.entity.processdefinition.ProcessDefinitionData;
import io.openbpm.control.service.processdefinition.ProcessDefinitionService;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.component.checkbox.JmixCheckbox;
import io.jmix.flowui.view.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.camunda.community.rest.exception.RemoteProcessEngineException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

@Route(value = "bpm/bulkdeleteprocessdefinition", layout = DefaultMainViewParent.class)
@ViewController("bpm_BulkDeleteProcessDefinition")
@ViewDescriptor("bulk-delete-process-definition-view.xml")
@DialogMode(width = "35em")
@Slf4j
public class BulkDeleteProcessDefinitionView extends ProcessDefinitionBulkOperationView {
    private static final String CAMUNDA_EXCEPTION_WITH_REASON_MESSAGE = "REST-CLIENT-002 Error during remote Camunda engine invocation with";

    @Autowired
    protected Notifications notifications;

    @Autowired
    protected ProcessDefinitionService processDefinitionService;

    @ViewComponent
    protected JmixCheckbox deleteAllVersionsCheckBox;

    @ViewComponent
    protected JmixCheckbox deleteProcessInstancesCheckBox;

    @Subscribe
    protected void onBeforeShow(BeforeShowEvent event) {
        deleteProcessInstancesCheckBox.setValue(true);
    }

    @Subscribe("okBtn")
    protected void onOkBtnClick(ClickEvent<Button> event) {
        boolean deleteAllRelatedInstances = BooleanUtils.isTrue(deleteProcessInstancesCheckBox.getValue());
        try {
            if (BooleanUtils.isTrue(deleteAllVersionsCheckBox.getValue())) {
                Set<String> processDefinitionKeys = collectProcessDefinitionKeys();
                for (String key : processDefinitionKeys) {
                    processDefinitionService.deleteAllVersionsByKey(key, deleteAllRelatedInstances);
                }
            } else {
                for (ProcessDefinitionData processDefinition : processDefinitions) {
                    processDefinitionService.deleteById(processDefinition.getId(), deleteAllRelatedInstances);
                }
            }
        } catch (Exception e) {
            if (e instanceof RemoteProcessEngineException) {
                log.error("Unable to delete process definitions", e);
                String exceptionMessage = e.getMessage();
                String errorReason = null;
                if (exceptionMessage.startsWith(CAMUNDA_EXCEPTION_WITH_REASON_MESSAGE)) {
                    String[] split = exceptionMessage.split(":", 2);
                    errorReason = split[1].replaceAll("\\.", ".\n");
                }

                notifications.create(StringUtils.defaultIfEmpty(errorReason, exceptionMessage))
                        .withType(Notifications.Type.ERROR)
                        .withDuration(10000)
                        .show();
                return;
            }
            throw e;
        }
        close(StandardOutcome.SAVE);
    }

    @Subscribe("cancelBtn")
    protected void onCancelBtnClick(ClickEvent<Button> event) {
        close(StandardOutcome.DISCARD);
    }
}