/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BpmProcessDefinition {
    private String key;
    private String name;
}