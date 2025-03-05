/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.view.job;

import com.vaadin.flow.router.Route;
import io.jmix.flowui.component.codeeditor.CodeEditor;
import io.jmix.flowui.view.*;
import io.openbpm.control.action.CopyComponentValueToClipboardAction;
import io.openbpm.control.service.job.JobService;
import io.openbpm.control.view.main.MainView;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "job-error-details", layout = MainView.class)
@ViewController("JobErrorDetailsView")
@ViewDescriptor("job-error-details-view.xml")
public class JobErrorDetailsView extends StandardView {
    @Autowired
    protected JobService jobService;

    @ViewComponent
    protected CodeEditor errorDetailsCodeEditor;
    @ViewComponent
    protected CopyComponentValueToClipboardAction copy;

    protected String jobId;
    protected boolean fromHistory = false;

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public void fromHistory() {
        this.fromHistory = true;
    }

    @Subscribe
    public void onBeforeShow(final BeforeShowEvent event) {
        String errorDetails;
        if (fromHistory) {
            errorDetails = jobService.getHistoryErrorDetails(jobId);
        } else {
            errorDetails = jobService.getErrorDetails(jobId);
        }

        errorDetailsCodeEditor.setValue(errorDetails);
        copy.setTarget(errorDetailsCodeEditor);
    }
}