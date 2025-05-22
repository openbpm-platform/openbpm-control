/*
 * Copyright (c) Haulmont 2025. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.entity.deployment;

import io.jmix.core.metamodel.datatype.EnumClass;

import org.springframework.lang.Nullable;


public enum ValidationErrorType implements EnumClass<String> {

    ERROR("Error"),
    WARNING("Warning");

    private final String id;

    ValidationErrorType(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static ValidationErrorType fromId(String id) {
        for (ValidationErrorType at : ValidationErrorType.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}