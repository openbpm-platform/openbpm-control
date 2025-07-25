package io.openbpm.control.view.processinstance;


import com.vaadin.flow.router.Route;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.view.*;
import io.openbpm.control.service.processinstance.ProcessInstanceService;
import io.openbpm.control.view.main.MainView;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route(value = "bulk-suspend-process-instance-view", layout = MainView.class)
@ViewController(id = "BulkSuspendProcessInstanceView")
@ViewDescriptor(path = "bulk-suspend-process-instance-view.xml")
public class BulkSuspendProcessInstanceView extends StandardView {

    @Autowired
    private ProcessInstanceService processInstanceService;
    @Autowired
    private Notifications notifications;

    @ViewComponent
    private MessageBundle messageBundle;

    protected List<String> instancesIds;

    public void setInstancesIds(List<String> instancesIds) {
        this.instancesIds = instancesIds;
    }

    @Subscribe("suspendAction")
    public void onSuspendAction(final ActionPerformedEvent event) {
        processInstanceService.suspendByIdsAsync(instancesIds);
        notifications.create(messageBundle.getMessage("bulkSuspendProcessInstancesStarted"))
                .withType(Notifications.Type.SUCCESS)
                .show();
    }

    @Subscribe("cancelAction")
    public void onCancelAction(final ActionPerformedEvent event) {
        close(StandardOutcome.CLOSE);
    }
}