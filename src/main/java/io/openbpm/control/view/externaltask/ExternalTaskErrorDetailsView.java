/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.view.externaltask;


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
import io.jmix.flowui.view.DialogMode;
import io.jmix.flowui.view.MessageBundle;
import io.jmix.flowui.view.StandardView;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.ViewComponent;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;
import io.openbpm.control.service.externaltask.ExternalTaskService;
import io.openbpm.control.view.main.MainView;
import org.springframework.beans.factory.annotation.Autowired;

import static io.openbpm.control.view.util.JsUtils.COPY_SCRIPT_TEXT;

@Route(value = "external-task-error-details", layout = MainView.class)
@ViewController("ExternalTaskErrorDetailsView")
@ViewDescriptor("external-task-error-details-view.xml")
@DialogMode(width = "52em")
public class ExternalTaskErrorDetailsView extends StandardView {
    @Autowired
    protected ExternalTaskService externalTaskService;
    @ViewComponent
    protected CodeEditor errorDetailsCodeEditor;

    protected String externalTaskId;
    protected boolean fromHistory = false;
    @Autowired
    private Notifications notifications;
    @ViewComponent
    private MessageBundle messageBundle;

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