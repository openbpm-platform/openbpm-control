/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.control.view.processinstance.event;

import org.springframework.context.ApplicationEvent;


public class ExternalTaskRetriesUpdateEvent extends ApplicationEvent {

    public ExternalTaskRetriesUpdateEvent(Object source) {
        super(source);
    }

}