/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.entity.processinstance;

import io.jmix.core.metamodel.datatype.EnumClass;
import org.springframework.lang.Nullable;

public enum ProcessInstanceState implements EnumClass<String> {

    ACTIVE("active"),
    COMPLETED("completed"),
    SUSPENDED("suspended");

    private final String id;

    ProcessInstanceState(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static ProcessInstanceState fromId(String id) {
        for (ProcessInstanceState at : ProcessInstanceState.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}