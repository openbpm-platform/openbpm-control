/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.entity.processinstance;

import io.jmix.core.MetadataTools;
import io.jmix.core.entity.annotation.JmixId;
import io.jmix.core.metamodel.annotation.DependsOnProperties;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.core.metamodel.annotation.JmixProperty;

import java.util.Date;

@JmixEntity(name = "bpm_ProcessInstanceData")
public class ProcessInstanceData {

    @JmixId
    @JmixProperty(mandatory = true)
    protected String id;

    protected String deleteReason;

    protected String instanceId;

    protected Boolean suspended = false;

    protected String businessKey;

    protected String tenantId;

    protected String processDefinitionId;

    protected String processDefinitionKey;

    protected String processDefinitionName;

    protected Integer processDefinitionVersion;

    protected String rootProcessInstanceId;

    protected Date endTime;

    protected Date startTime;

    protected Boolean complete = false;

    protected Boolean internallyTerminated = false;

    protected Boolean externallyTerminated = false;

    protected Boolean hasIncidents = false;

    public Boolean getHasIncidents() {
        return hasIncidents;
    }

    public void setHasIncidents(Boolean hasIncidents) {
        this.hasIncidents = hasIncidents;
    }

    public String getDeleteReason() {
        return deleteReason;
    }

    public void setDeleteReason(String deleteReason) {
        this.deleteReason = deleteReason;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public Boolean getSuspended() {
        return suspended;
    }

    public void setSuspended(Boolean suspended) {
        this.suspended = suspended;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    public String getProcessDefinitionKey() {
        return processDefinitionKey;
    }

    public void setProcessDefinitionKey(String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }

    public String getProcessDefinitionName() {
        return processDefinitionName;
    }

    public void setProcessDefinitionName(String processDefinitionName) {
        this.processDefinitionName = processDefinitionName;
    }

    public Integer getProcessDefinitionVersion() {
        return processDefinitionVersion;
    }

    public void setProcessDefinitionVersion(Integer processDefinitionVersion) {
        this.processDefinitionVersion = processDefinitionVersion;
    }

    public String getRootProcessInstanceId() {
        return rootProcessInstanceId;
    }

    public void setRootProcessInstanceId(String rootProcessInstanceId) {
        this.rootProcessInstanceId = rootProcessInstanceId;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Boolean getComplete() {
        return complete;
    }

    public void setComplete(Boolean complete) {
        this.complete = complete;
    }

    public Boolean getFinished() {
        return endTime != null;
    }

    public Boolean getInternallyTerminated() {
        return internallyTerminated;
    }

    public void setInternallyTerminated(Boolean internallyTerminated) {
        this.internallyTerminated = internallyTerminated;
    }

    public Boolean getExternallyTerminated() {
        return externallyTerminated;
    }

    public void setExternallyTerminated(Boolean externallyTerminated) {
        this.externallyTerminated = externallyTerminated;
    }

    @JmixProperty
    public ProcessInstanceState getState() {
        if (complete || internallyTerminated || externallyTerminated) {
            return ProcessInstanceState.COMPLETED;
        }
        if (suspended) {
            return ProcessInstanceState.SUSPENDED;
        }
        return ProcessInstanceState.ACTIVE;
    }

    @InstanceName
    @DependsOnProperties({"instanceId"})
    public String getInstanceName(MetadataTools metadataTools) {
        return String.format("Process Instance %s",
                metadataTools.format(instanceId));
    }
}
