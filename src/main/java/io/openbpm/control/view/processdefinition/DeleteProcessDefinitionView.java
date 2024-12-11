/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.view.processdefinition;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import io.openbpm.control.service.processdefinition.ProcessDefinitionService;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.component.checkbox.JmixCheckbox;
import io.jmix.flowui.view.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.camunda.community.rest.exception.RemoteProcessEngineException;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "bpm/deleteprocessdefinition", layout = DefaultMainViewParent.class)
@ViewController("bpm_DeleteProcessDefinition")
@ViewDescriptor("delete-process-definition-view.xml")
@DialogMode(width = "35em")
@Slf4j
public class DeleteProcessDefinitionView extends StandardView {
    protected static final String CAMUNDA_EXCEPTION_WITH_REASON = "REST-CLIENT-002 Error during remote Camunda engine invocation with";

    @Autowired
    protected Notifications notifications;

    @Autowired
    protected ProcessDefinitionService processDefinitionService;

    @ViewComponent
    protected Icon allInstancesContextHelp;

    @ViewComponent
    protected JmixCheckbox deleteProcessInstancesCheckBox;

    protected String processDefinitionId;

    @SuppressWarnings("LombokSetterMayBeUsed")
    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    @Subscribe
    public void onInit(final InitEvent event) {
        addClassNames(LumoUtility.Padding.Top.NONE, LumoUtility.Padding.Left.LARGE);
        allInstancesContextHelp.addClassNames(LumoUtility.TextColor.SECONDARY);
    }

    @Subscribe
    protected void onBeforeShow(BeforeShowEvent event) {
        deleteProcessInstancesCheckBox.setValue(true);
    }

    @Subscribe("okBtn")
    protected void onActivateBtnClick(ClickEvent<Button> event) {
        boolean deleteAllRelatedInstances = BooleanUtils.isTrue(deleteProcessInstancesCheckBox.getValue());
        try {
            processDefinitionService.deleteById(processDefinitionId, deleteAllRelatedInstances);
        } catch (Exception e) {
            if (e instanceof RemoteProcessEngineException) {
                log.error("Unable to delete process definition version", e);
                String exceptionMessage = e.getMessage();
                String errorReason = null;
                if (exceptionMessage.startsWith(CAMUNDA_EXCEPTION_WITH_REASON)) {
                    String[] split = exceptionMessage.split(":", 2);
                    errorReason = split[1];
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