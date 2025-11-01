/*
 * Copyright (c) Haulmont 2025. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.control.test_support.camunda7.dto.response;

import io.flowset.control.test_support.camunda7.dto.IdDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HistoricDetailDto extends IdDto {
    private String type;
    private String variableName;
    private String variableType;
    private Object value;
    private String processInstanceId;
    private String executionId;
    private String variableInstanceId;
}
