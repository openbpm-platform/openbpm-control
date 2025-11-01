/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.control.entity.variable;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.entity.annotation.JmixId;
import io.jmix.core.metamodel.annotation.JmixEntity;

import java.util.UUID;

@JmixEntity
public class ObjectTypeInfo {
    @JmixGeneratedValue
    @JmixId
    protected UUID id;

    protected String objectTypeName;

    protected String serializationDataFormat;

    public String getSerializationDataFormat() {
        return serializationDataFormat;
    }

    public void setSerializationDataFormat(String serializationDataFormat) {
        this.serializationDataFormat = serializationDataFormat;
    }

    public String getObjectTypeName() {
        return objectTypeName;
    }

    public void setObjectTypeName(String objectTypeName) {
        this.objectTypeName = objectTypeName;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}