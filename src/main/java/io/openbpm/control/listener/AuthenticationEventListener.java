package io.openbpm.control.listener;

import io.openbpm.control.entity.engine.BpmEngine;
import io.openbpm.control.service.engine.EngineService;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationEventListener {

    protected final EngineService engineService;

    public AuthenticationEventListener(EngineService engineService) {
        this.engineService = engineService;
    }

    @EventListener
    public void onInteractiveAuthenticationSuccess(final InteractiveAuthenticationSuccessEvent event) {
        BpmEngine engine = engineService.findDefaultEngine();
        if (engine != null) {
            engineService.setSelectedEngine(engine);
        }
    }
}