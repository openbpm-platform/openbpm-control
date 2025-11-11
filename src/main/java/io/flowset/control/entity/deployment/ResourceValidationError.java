/*
 * Copyright (c) Haulmont 2025. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.control.entity.deployment;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.entity.annotation.JmixId;
import io.jmix.core.metamodel.annotation.JmixEntity;

import java.util.UUID;

@JmixEntity
public class ResourceValidationError {
    @JmixGeneratedValue
    @JmixId
    private UUID id;

    private String message;

    private Integer line;

    private Integer column;

    private String mainElementId;

    private String type;

    public ValidationErrorType getType() {
        return type == null ? null : ValidationErrorType.fromId(type);
    }

    public void setType(ValidationErrorType type) {
        this.type = type == null ? null : type.getId();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getMainElementId() {
        return mainElementId;
    }

    public void setMainElementId(String mainElementId) {
        this.mainElementId = mainElementId;
    }

    public Integer getColumn() {
        return column;
    }

    public void setColumn(Integer column) {
        this.column = column;
    }

    public Integer getLine() {
        return line;
    }

    public void setLine(Integer line) {
        this.line = line;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}