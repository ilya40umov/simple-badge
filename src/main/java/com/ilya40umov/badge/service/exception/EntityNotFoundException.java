package com.ilya40umov.badge.service.exception;

/**
 * Thrown when the referenced entity is not found in DB.
 *
 * @author isorokoumov
 */
public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException() {
    }

    public EntityNotFoundException(String message) {
        super(message);
    }

}
