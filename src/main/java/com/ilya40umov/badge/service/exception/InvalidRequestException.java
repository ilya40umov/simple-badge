package com.ilya40umov.badge.service.exception;

/**
 * Thrown when request to the service layer is invalid.
 *
 * @author isorokoumov
 */
public class InvalidRequestException extends RuntimeException {

    public InvalidRequestException() {
    }

    public InvalidRequestException(String message) {
        super(message);
    }
}
