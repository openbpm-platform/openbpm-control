/*
 * Copyright (c) Haulmont 2025. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.control.entity.deployment;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.entity.annotation.JmixId;
import io.jmix.core.metamodel.annotation.JmixEntity;

import java.util.List;
import java.util.UUID;

@JmixEntity
public class ResourceDeploymentReport {
    @JmixGeneratedValue
    @JmixId
    private UUID id;

    private String filename;

    private List<ResourceValidationError> validationErrors;

    public List<ResourceValidationError> getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(List<ResourceValidationError> validationErrors) {
        this.validationErrors = validationErrors;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

}