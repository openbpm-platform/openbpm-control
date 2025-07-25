package io.openbpm.control.view.processinstance;


import com.vaadin.flow.router.Route;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.view.*;
import io.openbpm.control.entity.processinstance.ProcessInstanceData;
import io.openbpm.control.service.processinstance.ProcessInstanceService;
import io.openbpm.control.view.main.MainView;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "activate-process-instance-view", layout = MainView.class)
@ViewController(id = "ActivateProcessInstanceView")
@ViewDescriptor(path = "activate-process-instance-view.xml")
public class ActivateProcessInstanceView extends StandardView {

    @Autowired
    private ProcessInstanceService processInstanceService;
    @Autowired
    private Notifications notifications;

    @ViewComponent
    private MessageBundle messageBundle;

    protected ProcessInstanceData processInstanceData;

    public void setProcessInstanceData(ProcessInstanceData processInstanceData) {
        this.processInstanceData = processInstanceData;
    }

    @Subscribe("activateAction")
    public void onActivateAction(final ActionPerformedEvent event) {
        processInstanceService.activateById(processInstanceData.getId());

        notifications.create(messageBundle.getMessage("processInstanceActivated"))
                .withType(Notifications.Type.SUCCESS)
                .show();

        close(StandardOutcome.SAVE);
    }

    @Subscribe("cancelAction")
    public void onCancelAction(final ActionPerformedEvent event) {
        close(StandardOutcome.CLOSE);
    }
}