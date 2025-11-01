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
public class ProcessDefinitionDto extends IdDto {
    private String name;
    private String description;
    private String key;
    private Integer version;
    private String deploymentId;
    private String resource;
    private boolean suspended;
}
