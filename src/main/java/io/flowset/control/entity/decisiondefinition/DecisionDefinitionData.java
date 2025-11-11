package io.flowset.control.entity.decisiondefinition;

import io.jmix.core.entity.annotation.JmixId;
import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.core.metamodel.annotation.JmixProperty;
import lombok.Getter;
import lombok.Setter;

@JmixEntity(name = "bpm_DecisionDefinitionData")
@Getter
@Setter
public class DecisionDefinitionData {

    @JmixId
    @JmixProperty(mandatory = true)
    protected String id;

    protected String decisionDefinitionId;

    protected String name;

    protected String key;

    protected String category;

    protected String deploymentId;

    protected Integer historyTimeToLive;

    protected String resourceName;

    protected String tenantId;

    protected String version;

    protected String versionTag;

    protected String decisionRequirementsDefinitionId;

    protected String decisionRequirementsDefinitionKey;
}
