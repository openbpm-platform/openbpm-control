/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.view.externaltask;


import com.vaadin.flow.router.Route;
import io.jmix.flowui.component.codeeditor.CodeEditor;
import io.jmix.flowui.view.DialogMode;
import io.jmix.flowui.view.StandardView;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.ViewComponent;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;
import io.openbpm.control.action.CopyComponentValueToClipboardAction;
import io.openbpm.control.service.externaltask.ExternalTaskService;
import io.openbpm.control.view.main.MainView;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "external-task-error-details", layout = MainView.class)
@ViewController("ExternalTaskErrorDetailsView")
@ViewDescriptor("external-task-error-details-view.xml")
@DialogMode(width = "52em")
public class ExternalTaskErrorDetailsView extends StandardView {
    @Autowired
    protected ExternalTaskService externalTaskService;
    @ViewComponent
    protected CodeEditor errorDetailsCodeEditor;
    @ViewComponent
    protected CopyComponentValueToClipboardAction copy;

    protected String externalTaskId;
    protected boolean fromHistory = false;

    public void setExternalTaskId(String externalTaskId) {
        this.externalTaskId = externalTaskId;
    }

    public void fromHistory() {
        this.fromHistory = true;
    }

    @Subscribe
    public void onBeforeShow(final BeforeShowEvent event) {
        String stacktrace;
        if (fromHistory) {
            stacktrace = externalTaskService.getHistoryErrorDetails(externalTaskId);
        } else {
            stacktrace = externalTaskService.getErrorDetails(externalTaskId);
        }
        errorDetailsCodeEditor.setValue(stacktrace);
        copy.setTarget(errorDetailsCodeEditor);
    }
}