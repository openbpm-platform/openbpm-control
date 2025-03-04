/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.view.job;


import com.google.common.base.Strings;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.component.codeeditor.CodeEditor;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.view.*;
import io.openbpm.control.service.job.JobService;
import io.openbpm.control.view.main.MainView;
import org.springframework.beans.factory.annotation.Autowired;

import static io.openbpm.control.view.util.JsUtils.COPY_SCRIPT_TEXT;

@Route(value = "job-error-details", layout = MainView.class)
@ViewController("JobErrorDetailsView")
@ViewDescriptor("job-error-details-view.xml")
public class JobErrorDetailsView extends StandardView {
    @Autowired
    protected JobService jobService;

    @ViewComponent
    protected CodeEditor errorDetailsCodeEditor;

    protected String jobId;
    protected boolean fromHistory = false;
    @Autowired
    private Notifications notifications;
    @ViewComponent
    private MessageBundle messageBundle;

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
    }

    @Subscribe(id = "copy", subject = "clickListener")
    public void onButtonClick(final ClickEvent<JmixButton> event) {
        Element buttonElement = event.getSource().getElement();
        String valueToCopy = Strings.nullToEmpty(errorDetailsCodeEditor.getValue());
        buttonElement.executeJs(COPY_SCRIPT_TEXT, valueToCopy)
                .then(successResult -> notifications.create(messageBundle.getMessage("stacktraceCopied"))
                                .withPosition(Notification.Position.TOP_END)
                                .withThemeVariant(NotificationVariant.LUMO_SUCCESS)
                                .show(),
                        errorResult -> notifications.create(messageBundle.getMessage("stacktraceCopyFailed"))
                                .withPosition(Notification.Position.TOP_END)
                                .withThemeVariant(NotificationVariant.LUMO_ERROR)
                                .show());
        UI.getCurrent().getPage().executeJs("window.copyToClipboard($0)", errorDetailsCodeEditor.getValue());
    }
}