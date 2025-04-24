/*
 * Copyright (c) Haulmont 2025. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.test_support.camunda7.dto.response;

import io.openbpm.control.test_support.camunda7.dto.IdDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HistoricIncidentDto extends IdDto {
    private String processInstanceId;
    private String processDefinitionKey;
    private String processDefinitionId;
    private String rootCauseIncidentId;
    private String incidentType;
    private String activityId;
    private String incidentMessage;
    private Boolean resolved;
    private Boolean open;
    private String configuration;
}
