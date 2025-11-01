/*
 * Copyright (c) Haulmont 2025. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.control.exception;

import io.flowset.control.restsupport.camunda.ResourceReport;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * Exception for the <code>ParseException</code> type returned in the {@link io.flowset.control.restsupport.camunda.CamundaErrorResponse}.
 * @see io.flowset.control.restsupport.camunda.ParseExceptionResponse
 */
@Getter
@Setter
public class RemoteEngineParseException extends RemoteProcessEngineException {
    protected Map<String, ResourceReport> details;

    public RemoteEngineParseException(String message) {
        super(message);
        details = new HashMap<>();
    }

    public RemoteEngineParseException(String message, Map<String, ResourceReport> details) {
        super(message);
        this.details = details;
    }
}
