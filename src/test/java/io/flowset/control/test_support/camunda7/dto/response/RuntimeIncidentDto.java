/*
 * Copyright (c) Haulmont 2025. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.control.test_support.camunda7.dto.response;

import io.flowset.control.test_support.camunda7.dto.IdDto;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
public class RuntimeIncidentDto extends IdDto {
    private String processInstanceId;
    private String rootCauseIncidentId;
    private String incidentType;
    private String failedActivityId;
    private String activityId;
    private String incidentMessage;
    private String jobDefinitionId;
    private OffsetDateTime incidentTimestamp;
    private String processDefinitionId;
    private String executionId;
    private String configuration;
}
