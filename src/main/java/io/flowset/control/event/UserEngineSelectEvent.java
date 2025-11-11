package io.flowset.control.event;

import io.flowset.control.entity.engine.BpmEngine;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * An event that fires if the engine is selected for user.
 */
@Getter
public class UserEngineSelectEvent extends ApplicationEvent {
    private final BpmEngine engine;
    private final String username;

    public UserEngineSelectEvent(Object source, BpmEngine engine, String username) {
        super(source);
        this.engine = engine;
        this.username = username;
    }
}
