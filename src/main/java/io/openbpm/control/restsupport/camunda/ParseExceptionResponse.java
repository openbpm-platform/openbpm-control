/*
 * Copyright (c) Haulmont 2025. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.restsupport.camunda;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * A DTO for the error response with the  <code>ParseException</code> type.
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ParseExceptionResponse extends CamundaErrorResponse {

    private Map<String, ResourceReport> details;

}
