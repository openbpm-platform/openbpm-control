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
public class RuntimeProcessInstanceDto extends IdDto {
    private String definitionId;
    private String businessKey;
    private Boolean ended;
    private Boolean suspended;
    private String tenantId;
    private String caseInstanceId;
}
