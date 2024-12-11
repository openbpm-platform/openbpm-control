package io.openbpm.control.action;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.icon.VaadinIcon;
import io.jmix.core.AccessManager;
import io.jmix.core.Messages;
import io.jmix.core.Metadata;
import io.jmix.core.metamodel.model.MetaClass;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.accesscontext.UiEntityContext;
import io.jmix.flowui.action.ActionType;
import io.jmix.flowui.action.SecuredBaseAction;
import io.jmix.flowui.kit.component.ComponentUtils;
import io.openbpm.control.entity.engine.BpmEngine;
import io.openbpm.control.exception.EngineConnectionFailedException;
import io.openbpm.control.service.engine.EngineUiService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

@ActionType(TestEngineConnectionAction.ID)
public class TestEngineConnectionAction extends SecuredBaseAction {
    public static final String ID = "control_testEngineConnection";

    private BpmEngine engine;

    protected Messages messages;
    protected EngineUiService engineUiService;
    protected Notifications notifications;
    protected Metadata metadata;
    protected AccessManager accessManager;

    public TestEngineConnectionAction() {
        super(ID);
    }

    public TestEngineConnectionAction(String id) {
        super(id);

        this.icon = ComponentUtils.convertToIcon(VaadinIcon.CONNECT);
    }

    public void setEngine(BpmEngine engine) {
        this.engine = engine;
    }

    @Autowired
    public void setMessages(Messages messages) {
        this.messages = messages;
        this.text = messages.getMessage("actions.TestConnection");
    }

    @Autowired
    public void setEngineUiService(EngineUiService engineUiService) {
        this.engineUiService = engineUiService;
    }

    @Autowired
    public void setNotifications(Notifications notifications) {
        this.notifications = notifications;
    }

    @Autowired
    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    @Autowired
    public void setAccessManager(AccessManager accessManager) {
        this.accessManager = accessManager;
    }

    @Override
    public void actionPerform(Component component) {
        if (engine != null) {
            try {
                engineUiService.getVersion(engine);
                notifications.create(messages.formatMessage("", "engineAvailable", engine.getBaseUrl()))
                        .withType(Notifications.Type.SUCCESS)
                        .show();
            } catch (EngineConnectionFailedException e) {
                if (e.getStatusCode() > 0) {
                    notifications.create(messages.getMessage("engineNotAvailable.title"),
                                    messages.formatMessage("", "engineNotAvailable.description", e.getStatusCode()))
                            .withType(Notifications.Type.ERROR)
                            .show();
                } else {
                    String errorMessage = StringUtils.defaultIfBlank(e.getResponseErrorMessage(), e.getMessage());
                    notifications.create(messages.getMessage("engineNotAvailable.title"),
                                    messages.formatMessage("", "engineNotAvailable.descriptionWithError", errorMessage))
                            .withType(Notifications.Type.ERROR)
                            .show();
                }
            }
        }
    }


    @Override
    protected boolean isPermitted() {
        if (engine == null) {
            return false;
        }

        MetaClass metaClass = metadata.getClass(engine);

        UiEntityContext entityContext = new UiEntityContext(metaClass);
        accessManager.applyRegisteredConstraints(entityContext);

        if (!entityContext.isViewPermitted()) {
            return false;
        }

        return super.isPermitted();
    }
}
