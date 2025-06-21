package io.openbpm.control.action;

import com.vaadin.flow.component.Component;
import io.jmix.flowui.action.ActionType;
import io.jmix.flowui.action.SecuredBaseAction;

@ActionType(ActivateProcessInstanceAction.ID)
public class ActivateProcessInstanceAction extends SecuredBaseAction {

    protected static final String ID = "control_activateProcessInstanceAction";

    public ActivateProcessInstanceAction(String id) {
        super(id);
    }

//    @Override
//    protected boolean isPermitted() {
//
//    }
//
//    @Override
//    public void actionPerform(Component component) {
//
//
//    }
}
