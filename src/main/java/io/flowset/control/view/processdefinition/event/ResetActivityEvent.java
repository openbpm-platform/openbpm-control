/*
 * Copyright (c) Haulmont 2025. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.control.view.processdefinition.event;

import org.springframework.context.ApplicationEvent;

/**
 * Resets a filter with the selected activity.
 */
public class ResetActivityEvent extends ApplicationEvent {
    private final String activityId;

    public ResetActivityEvent(Object source, String activityId) {
        super(source);
        this.activityId = activityId;
    }

    public String getActivityId() {
        return activityId;
    }
}
