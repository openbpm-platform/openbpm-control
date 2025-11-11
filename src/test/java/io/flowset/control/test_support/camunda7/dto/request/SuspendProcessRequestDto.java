/*
 * Copyright (c) Haulmont 2025. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.control.test_support.camunda7.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SuspendProcessRequestDto extends SuspendRequestDto {
    private boolean includeProcessInstances;
    private String processDefinitionId;
    private String processDefinitionKey;
}
