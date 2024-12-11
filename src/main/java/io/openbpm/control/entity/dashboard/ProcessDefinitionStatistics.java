/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.entity.dashboard;

import io.openbpm.control.entity.processdefinition.ProcessDefinitionData;
import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.entity.annotation.JmixId;
import io.jmix.core.metamodel.annotation.JmixEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@JmixEntity
public class ProcessDefinitionStatistics {
    @JmixGeneratedValue
    @JmixId
    protected UUID id;

    protected ProcessDefinitionData processDefinition;

    protected String processDefinitionId;

    protected Integer instanceCount;

    protected Integer failedJobCount;

    protected List<IncidentStatistics> incidents = new ArrayList<>();

    public ProcessDefinitionData getProcessDefinition() {
        return processDefinition;
    }

    public void setProcessDefinition(ProcessDefinitionData processDefinition) {
        this.processDefinition = processDefinition;
    }


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

    public Integer getInstanceCount() {
        return instanceCount;
    }

    public void setInstanceCount(Integer instanceCount) {
        this.instanceCount = instanceCount;
    }

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}