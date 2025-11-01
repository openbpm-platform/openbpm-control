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
public class HistoricUserTaskDto extends IdDto {
    private String taskDefinitionKey;
    private String name;
    private String activityInstanceId;
    private OffsetDateTime startTime;
    private OffsetDateTime endTime;
}
