/*
 * Copyright (c) Haulmont 2025. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.test_support.camunda7.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Singular;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CompleteUserTaskDto {
    @Singular
    private Map<String, VariableValueDto> variables;
    private boolean withVariablesInReturn;

    public CompleteUserTaskDto() {
        variables = new HashMap<>();
    }
}
