/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.entity;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.entity.annotation.JmixId;
import io.jmix.core.metamodel.annotation.JmixEntity;

import java.time.LocalDate;
import java.util.UUID;

@JmixEntity
public class ProcessExecutionGraphEntry {
    @JmixGeneratedValue
    @JmixId
    protected UUID id;

    protected Long completedInstancesCount = 0L;

    protected Long startedInstancesCount = 0L;

    protected LocalDate date;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getStartedInstancesCount() {
        return startedInstancesCount;
    }

    public void setStartedInstancesCount(Long startedInstancesCount) {
        this.startedInstancesCount = startedInstancesCount;
    }

    public Long getCompletedInstancesCount() {
        return completedInstancesCount;
    }

    public void setCompletedInstancesCount(Long completedInstancesCount) {
        this.completedInstancesCount = completedInstancesCount;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}