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
public class HistoricProcessInstanceDto extends IdDto {
    private String name;
    private String processDefinitionId;
    private String processDefinitionKey;
    private String processDefinitionName;
    private OffsetDateTime removalTime;
    private OffsetDateTime endTime;
    private String state;
    private String deleteReason;

}
