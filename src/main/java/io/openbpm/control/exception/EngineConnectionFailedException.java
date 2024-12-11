package io.openbpm.control.exception;

public class EngineConnectionFailedException extends RuntimeException {
    private int statusCode;
    private String responseErrorMessage;

    public EngineConnectionFailedException(int statusCode, String responseErrorMessage) {
        this.statusCode = statusCode;
        this.responseErrorMessage = responseErrorMessage;
    }

    public EngineConnectionFailedException(String message, int statusCode, String responseErrorMessage) {
        super(message);
        this.statusCode = statusCode;
        this.responseErrorMessage = responseErrorMessage;
    }

    public EngineConnectionFailedException(String message, Throwable cause, int statusCode, String responseErrorMessage) {
        super(message, cause);
        this.statusCode = statusCode;
        this.responseErrorMessage = responseErrorMessage;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public EngineConnectionFailedException setStatusCode(int statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public String getResponseErrorMessage() {
        return responseErrorMessage;
    }

    public EngineConnectionFailedException setResponseErrorMessage(String responseErrorMessage) {
        this.responseErrorMessage = responseErrorMessage;
        return this;
    }
}
