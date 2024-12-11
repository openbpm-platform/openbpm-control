/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.view.processinstance.event;

import org.springframework.context.ApplicationEvent;


public class HistoryUserTaskCountUpdateEvent extends ApplicationEvent {
    private long count;

    public HistoryUserTaskCountUpdateEvent(Object source) {
        super(source);
    }

    public HistoryUserTaskCountUpdateEvent(Object source, long count) {
        super(source);
        this.count = count;
    }

    public long getCount() {
        return count;
    }
}
