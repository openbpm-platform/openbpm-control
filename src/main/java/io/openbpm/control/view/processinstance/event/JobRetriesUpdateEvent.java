/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.view.processinstance.event;

import org.springframework.context.ApplicationEvent;


public class JobRetriesUpdateEvent extends ApplicationEvent {

    public JobRetriesUpdateEvent(Object source) {
        super(source);
    }

}