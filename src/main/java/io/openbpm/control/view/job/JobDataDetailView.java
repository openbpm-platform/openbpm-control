/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.view.job;


import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import io.jmix.core.Messages;
import io.jmix.flowui.Dialogs;
import io.jmix.flowui.action.DialogAction;
import io.jmix.flowui.component.codeeditor.CodeEditor;
import io.jmix.flowui.component.formlayout.JmixFormLayout;
import io.jmix.flowui.component.textfield.TypedTextField;
import io.jmix.flowui.kit.action.ActionVariant;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.view.*;
import io.openbpm.control.entity.job.JobData;
import io.openbpm.control.entity.job.JobDefinitionData;
import io.openbpm.control.service.job.JobService;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "bpm/job/:id", layout = DefaultMainViewParent.class)
@ViewController("JobData.detail")
@ViewDescriptor("job-data-detail-view.xml")
@DialogMode(width = "50em", height = "37.5em", resizable = true)
@EditedEntityContainer("jobDataDc")
public class JobDataDetailView extends StandardDetailView<JobData> {
    @Autowired
    protected Dialogs dialogs;
    @ViewComponent
    protected MessageBundle messageBundle;

    @ViewComponent
    protected CodeEditor stackTraceField;
    @Autowired
    protected JobService jobService;
    @ViewComponent
    protected TypedTextField<String> activityField;
    @ViewComponent
    protected JmixFormLayout form;
    @ViewComponent
    protected TypedTextField<Object> jobTypeField;
    @ViewComponent
    protected JmixButton retryBtn;
    @Autowired
    protected Messages messages;

    @Subscribe
    public void onInit(final InitEvent event) {
        addClassNames(LumoUtility.Padding.Top.XSMALL);
        form.getOwnComponents().forEach(component -> component.addClassNames(LumoUtility.Padding.Top.SMALL));
    }

    @Subscribe
    protected void onBeforeShow(BeforeShowEvent event) {
        String stacktrace = jobService.getErrorDetails(getEditedEntity().getJobId());
        stackTraceField.setValue(stacktrace);

        JobDefinitionData jobDefinition = jobService.findJobDefinition(getEditedEntity().getJobDefinitionId());
        if (jobDefinition != null) {
            activityField.setValue(jobDefinition.getActivityId());
            jobTypeField.setValue(jobDefinition.getJobType());
        }

        if (getEditedEntity().getRetries() != null && getEditedEntity().getRetries() == 0) {
            retryBtn.setVisible(true);
        }
    }

    @Subscribe("retryBtn")
    protected void onRestoreFailedJobBtnClick(ClickEvent<Button> event) {
        dialogs.createOptionDialog()
                .withHeader(messageBundle.getMessage("restoreFailedJobDialog.header"))
                .withText(messageBundle.getMessage("restoreFailedJobDialog.text"))
                .withActions(new DialogAction(DialogAction.Type.YES)
                                .withIcon(VaadinIcon.ROTATE_LEFT.create())
                                .withText(messages.getMessage("actions.Retry"))
                                .withVariant(ActionVariant.PRIMARY)
                                .withHandler(actionPerformedEvent -> {
                                    jobService.setJobRetries(getEditedEntity().getJobId(), 1);
                                    close(StandardOutcome.SAVE);
                                }),
                        new DialogAction(DialogAction.Type.CANCEL))
                .open();
    }
}