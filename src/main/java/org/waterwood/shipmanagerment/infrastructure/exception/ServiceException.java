package org.waterwood.shipmanagerment.infrastructure.exception;

public class ServiceException extends RuntimeException {
    public ServiceException(String message) {
        super(message);
    }
}
