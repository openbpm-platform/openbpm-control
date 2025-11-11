/*
 * Copyright (c) Haulmont 2025. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.control.restsupport.camunda;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResourceReport {
    private List<ProblemDetails> errors;

    private List<ProblemDetails> warnings;


    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ProblemDetails {
        private String message;

        private Integer line;

        private Integer column;

        private String mainElementId;
    }
}
