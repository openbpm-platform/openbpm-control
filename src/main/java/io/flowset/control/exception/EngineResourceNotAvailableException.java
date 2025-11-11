package io.flowset.control.exception;

public class EngineResourceNotAvailableException extends RuntimeException {

    public EngineResourceNotAvailableException(String resourceName) {
        super(String.format("Can't open resource with name %s", resourceName));
    }
}
