package io.openbpm.control.action;

import com.vaadin.flow.component.Component;
import io.jmix.core.AccessManager;
import io.jmix.core.Metadata;
import io.jmix.flowui.DialogWindows;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.accesscontext.UiShowViewContext;
import io.jmix.flowui.action.ActionType;
import io.jmix.flowui.action.SecuredBaseAction;
import io.jmix.flowui.view.DialogWindow;
import io.jmix.flowui.view.MessageBundle;
import io.jmix.flowui.view.StandardOutcome;
import io.openbpm.control.entity.processdefinition.ProcessDefinitionData;
import io.openbpm.control.entity.processinstance.ProcessInstanceData;
import io.openbpm.control.view.processinstancemigration.ProcessInstanceMigrationView;
import org.springframework.beans.factory.annotation.Autowired;

import static io.jmix.flowui.component.UiComponentUtils.getCurrentView;

@ActionType(MigrateProcessInstanceAction.ID)
public class MigrateProcessInstanceAction extends SecuredBaseAction {

    public static final String ID = "control_migrateProcessInstanceAction";

    protected Metadata metadata;
    protected AccessManager accessManager;
    protected DialogWindows dialogWindows;
    protected Notifications notifications;
    protected MessageBundle messageBundle;

    protected ProcessInstanceData processInstanceData;

    public MigrateProcessInstanceAction(String id) {
        super(id);
    }

    @Autowired
    public void setNotifications(Notifications notifications) {
        this.notifications = notifications;
    }

    @Autowired
    public void setMessageBundle(MessageBundle messageBundle) {
        this.messageBundle = messageBundle;
    }

    @Autowired
    public void setAccessManager(AccessManager accessManager) {
        this.accessManager = accessManager;
    }

    @Autowired
    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    @Autowired
    public void setDialogWindows(DialogWindows dialogWindows) {
        this.dialogWindows = dialogWindows;
    }

    public void setProcessInstanceData(ProcessInstanceData processInstanceData) {
        this.processInstanceData = processInstanceData;
    }

    @Override
    protected boolean isPermitted() {
        UiShowViewContext uiShowViewContext = new UiShowViewContext(ProcessInstanceMigrationView.ID);
        accessManager.applyRegisteredConstraints(uiShowViewContext);

        if (!uiShowViewContext.isPermitted()) {
            return false;
        }

        return super.isPermitted();
    }

    @Override
    public void actionPerform(Component component) {
        ProcessDefinitionData processDefinitionData = metadata.create(ProcessDefinitionData.class);
        processDefinitionData.setId(processInstanceData.getProcessDefinitionId());
        processDefinitionData.setKey(processInstanceData.getProcessDefinitionKey());
        processDefinitionData.setVersion(processInstanceData.getProcessDefinitionVersion());

        DialogWindow<ProcessInstanceMigrationView> dialog =
                dialogWindows.view(getCurrentView(), ProcessInstanceMigrationView.class)
                        .withAfterCloseListener(afterCloseEvent -> {
                            if (afterCloseEvent.closedWith(StandardOutcome.SAVE)) {
//                                notifications.create(messageBundle.getMessage("processInstanceMigrated"))
//                                        .withType(Notifications.Type.SUCCESS)
//                                        .show();
//
//                                reopenProcessInstanceDetailsView();
                            }
                        })
                        .build();

        dialog.getView().setProcessDefinitionData(processDefinitionData);
        dialog.getView().setProcessInstanceData(processInstanceData);
        dialog.open();
    }
}
