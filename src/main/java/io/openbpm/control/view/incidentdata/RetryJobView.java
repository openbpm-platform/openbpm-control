package io.openbpm.control.view.incidentdata;


import com.vaadin.flow.router.Route;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.view.*;
import io.openbpm.control.service.job.JobService;
import io.openbpm.control.view.main.MainView;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "retry-job-view", layout = MainView.class)
@ViewController(id = "RetryJobView")
@ViewDescriptor(path = "retry-job-view.xml")
public class RetryJobView extends StandardView {

    @Autowired
    private JobService jobService;
    @Autowired
    private Notifications notifications;

    @ViewComponent
    private MessageBundle messageBundle;

    protected String jobId;

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    @Subscribe("retryAction")
    public void onRetryAction(final ActionPerformedEvent event) {
        jobService.setJobRetries(jobId, 1);

        notifications.create(messageBundle.getMessage("jobRetriesUpdated"))
                .withType(Notifications.Type.SUCCESS)
                .show();
    }

    @Subscribe("cancelAction")
    public void onCancelAction(final ActionPerformedEvent event) {
        close(StandardOutcome.CLOSE);
    }
}