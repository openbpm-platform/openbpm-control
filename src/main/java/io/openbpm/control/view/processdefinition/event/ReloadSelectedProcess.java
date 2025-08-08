/*
 * Copyright (c) Haulmont 2025. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.view.processdefinition.event;

import org.springframework.context.ApplicationEvent;

/**
 * Event for reloading the selected process definition in {@link io.openbpm.control.view.processdefinition.ProcessDefinitionDetailView}
 */
public class ReloadSelectedProcess extends ApplicationEvent {
    public ReloadSelectedProcess(Object source) {
        super(source);
    }
}
