package io.openbpm.control.entity.processinstance;

import io.jmix.core.entity.annotation.JmixId;
import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.core.metamodel.annotation.JmixProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.BooleanUtils;

@JmixEntity
@Getter
@Setter
public class RuntimeProcessInstanceData {

    @JmixId
    @JmixProperty(mandatory = true)
    protected String id;

    protected String instanceId;

    protected String businessKey;

    protected String processDefinitionId;

    protected Boolean suspended = false;

    protected Boolean hasIncidents = false;

    @JmixProperty
    public ProcessInstanceState getState() {
        return BooleanUtils.isTrue(suspended) ? ProcessInstanceState.SUSPENDED : ProcessInstanceState.ACTIVE;
    }
}