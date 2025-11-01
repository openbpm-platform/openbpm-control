package io.flowset.control.view.incidentdata;


import com.vaadin.flow.router.Route;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.view.*;
import io.flowset.control.service.externaltask.ExternalTaskService;
import io.flowset.control.view.main.MainView;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "retry-external-task-view", layout = MainView.class)
@ViewController(id = "RetryExternalTaskView")
@ViewDescriptor(path = "retry-external-task-view.xml")
public class RetryExternalTaskView extends StandardView {

    @Autowired
    private Notifications notifications;
    @Autowired
    protected ExternalTaskService externalTaskService;

    @ViewComponent
    private MessageBundle messageBundle;

    protected String externalTaskId;

    public void setExternalTaskId(String externalTaskId) {
        this.externalTaskId = externalTaskId;
    }

    @Subscribe("retryAction")
    public void onRetryAction(final ActionPerformedEvent event) {
        externalTaskService.setRetries(externalTaskId, 1);
        notifications.create(messageBundle.getMessage("externalTaskRetriesUpdated"))
                .withType(Notifications.Type.SUCCESS)
                .show();

        close(StandardOutcome.SAVE);
    }

    @Subscribe("cancelAction")
    public void onCancelAction(final ActionPerformedEvent event) {
        close(StandardOutcome.CLOSE);
    }
}