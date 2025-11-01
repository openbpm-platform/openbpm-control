package io.flowset.control.entity.deployment;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.entity.annotation.JmixId;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;

import java.util.UUID;

@JmixEntity
public class DeploymentProcessInstancesInfo {

    @JmixId
    @JmixGeneratedValue
    protected UUID id;

    protected String processDefinitionId;

    protected String processDefinitionName;

    @InstanceName
    protected String processDefinitionKey;

    protected Long processInstanceCount;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    public String getProcessDefinitionName() {
        return processDefinitionName;
    }

    public void setProcessDefinitionName(String processDefinitionName) {
        this.processDefinitionName = processDefinitionName;
    }

    public String getProcessDefinitionKey() {
        return processDefinitionKey;
    }

    public void setProcessDefinitionKey(String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }

    public Long getProcessInstanceCount() {
        return processInstanceCount;
    }

    public void setProcessInstanceCount(Long processInstanceCount) {
        this.processInstanceCount = processInstanceCount;
    }
}
