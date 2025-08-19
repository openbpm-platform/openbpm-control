/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.entity.filter;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.entity.annotation.JmixId;
import io.jmix.core.metamodel.annotation.JmixEntity;
import io.openbpm.control.entity.processinstance.ProcessInstanceState;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@JmixEntity
@Getter
@Setter
public class ProcessInstanceFilter {
    @JmixGeneratedValue
    @JmixId
    protected UUID id;

    protected String processInstanceId;

    protected String processDefinitionId;

    protected String state;

    protected String businessKeyLike;

    protected String processDefinitionKey;

    protected OffsetDateTime startTimeAfter;

    protected OffsetDateTime startTimeBefore;

    protected OffsetDateTime endTimeAfter;

    protected OffsetDateTime endTimeBefore;

    protected Boolean withIncidents;

    protected Boolean unfinished;

    protected List<String> activeActivityIdIn;

    protected List<String> processInstanceIds;

    public Boolean getUnfinished() {
        return unfinished;
    }

    public void setUnfinished(Boolean unfinished) {
        this.unfinished = unfinished;
    }

    public Boolean getWithIncidents() {
        return withIncidents;
    }

    public void setWithIncidents(Boolean withIncidents) {
        this.withIncidents = withIncidents;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public ProcessInstanceState getState() {
        return state == null ? null : ProcessInstanceState.fromId(state);
    }

    public void setState(ProcessInstanceState state) {
        this.state = state == null ? null : state.getId();
    }

    public Boolean getActive() {
        ProcessInstanceState st = getState();
        return st == ProcessInstanceState.ACTIVE ? true : null;
    }

    public Boolean getSuspended() {
        ProcessInstanceState st = getState();
        return st == ProcessInstanceState.SUSPENDED ? true : null;
    }

    public Boolean getFinished() {
        ProcessInstanceState st = getState();
        return st == ProcessInstanceState.COMPLETED ? true : null;
    }

    public boolean hasStartTime() {
        return startTimeAfter != null || startTimeBefore != null;
    }

    public boolean hasEndTime() {
        return endTimeAfter != null || endTimeBefore != null;
    }
}