/*
 * Copyright (c) Haulmont 2025. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.entity.activity;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.entity.annotation.JmixId;
import io.jmix.core.metamodel.annotation.JmixEntity;
import io.openbpm.control.entity.dashboard.IncidentStatistics;

import java.util.List;
import java.util.UUID;

@JmixEntity
public class ProcessActivityStatistics {
    @JmixGeneratedValue
    @JmixId
    private UUID id;

    private String activityId;

    private Integer instanceCount;

    private Integer failedJobCount;

    private List<IncidentStatistics> incidents;

    public List<IncidentStatistics> getIncidents() {
        return incidents;
    }

    public void setIncidents(List<IncidentStatistics> incidents) {
        this.incidents = incidents;
    }

    public Integer getFailedJobCount() {
        return failedJobCount;
    }

    public void setFailedJobCount(Integer failedJobCount) {
        this.failedJobCount = failedJobCount;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public Integer getInstanceCount() {
        return instanceCount;
    }

    public void setInstanceCount(Integer instanceCount) {
        this.instanceCount = instanceCount;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

}