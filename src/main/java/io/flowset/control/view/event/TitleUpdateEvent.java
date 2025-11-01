/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.control.view.event;

import com.vaadin.flow.component.Component;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class TitleUpdateEvent extends ApplicationEvent {
    private final String title;
    private Component suffixComponent;

    public TitleUpdateEvent(Object source, String title, Component suffixComponent) {
        super(source);
        this.title = title;
        this.suffixComponent = suffixComponent;
    }

    public TitleUpdateEvent(Object source, String title) {
        super(source);
        this.title = title;
    }
}
