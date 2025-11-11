/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.control.dto;

import io.flowset.uikit.component.bpmnviewer.model.ElementIncidentData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ActivityIncidentData implements ElementIncidentData {
    private String elementId;
    private Integer incidentCount;
}