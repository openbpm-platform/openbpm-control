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
public class VariableValueInfo {
    @JmixGeneratedValue
    @JmixId
    protected UUID id;

    protected ObjectTypeInfo object;

    protected String filename;

    protected String mimeType;

    protected String encoding;

    protected Boolean isTransient;

    public Boolean getIsTransient() {
        return isTransient;
    }

    public void setIsTransient(Boolean isTransient) {
        this.isTransient = isTransient;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public ObjectTypeInfo getObject() {
        return object;
    }

    public void setObject(ObjectTypeInfo object) {
        this.object = object;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}