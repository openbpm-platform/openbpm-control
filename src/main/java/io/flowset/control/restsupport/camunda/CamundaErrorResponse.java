/*
 * Copyright (c) Haulmont 2025. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.control.restsupport.camunda;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Generic DTO for the error response returned by Camunda 7 REST.
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CamundaErrorResponse {

    @JsonProperty("type")
    protected String type;

    @JsonProperty("message")
    protected String message;

    @JsonProperty("code")
    protected String code;

}
