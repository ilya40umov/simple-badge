package com.ilya40umov.badge.rest.v1;

import com.ilya40umov.badge.service.exception.EntityNotFoundException;
import com.ilya40umov.badge.service.exception.InvalidRequestException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

/**
 * Controls how REST API should behave in exceptional situations.
 *
 * @author isorokoumov
 */
@ControllerAdvice("com.ilya40umov.badge.rest.v1")
public class ErrorHandlingAdvice {

    // TODO consider improving error handling further based on the following article:
    // http://www.baeldung.com/global-error-handler-in-a-spring-rest-api

    /**
     * Handler for exceptions raised in case when a requested entity was found.
     */
    @ResponseStatus(NOT_FOUND)
    @ResponseBody
    @ExceptionHandler(EntityNotFoundException.class)
    public ErrorResponse handleEntityNotFoundException() {
        return new ErrorResponse().setStatus(NOT_FOUND).setError("Resource Not Found");
    }

    /**
     * Handler for validation exceptions produced by {@link javax.validation.Valid} annotation.
     */
    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        ErrorResponse errorResponse = new ErrorResponse()
                .setStatus(BAD_REQUEST)
                .setError("Invalid Request");
        ex.getBindingResult().getGlobalErrors().forEach(errorResponse::addGlobalError);
        ex.getBindingResult().getFieldErrors().forEach(errorResponse::addFieldError);
        return errorResponse;
    }

    /**
     * Handler for {@link InvalidRequestException} that is usually thrown by business logic.
     */
    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(InvalidRequestException.class)
    public ErrorResponse handleInvalidRequestException(InvalidRequestException ex) {
        return new ErrorResponse()
                .setStatus(BAD_REQUEST)
                .setError("Invalid Request")
                .setMessage(ex.getMessage());
    }

    /**
     * Handler for exceptions caused by data integrity violations.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ErrorResponse handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        Optional<SQLIntegrityConstraintViolationException> sqlConstraintViolation =
                traceSqlConstraintViolation(ex);
        return sqlConstraintViolation
                .map(e -> new ErrorResponse()
                        .setStatus(CONFLICT)
                        .setError("Integrity Constraint Violation")
                        .setMessage(e.getMessage()))
                .orElseGet(() -> new ErrorResponse()
                        .setStatus(CONFLICT)
                        .setError("Data Integrity Violation")
                        .setMessage(ex.getMessage()));
    }

    private static Optional<SQLIntegrityConstraintViolationException> traceSqlConstraintViolation(
            Throwable throwable) {
        if (throwable instanceof SQLIntegrityConstraintViolationException) {
            return Optional.of((SQLIntegrityConstraintViolationException) throwable);
        }
        if (throwable.getCause() == null) {
            return Optional.empty();
        }
        return traceSqlConstraintViolation(throwable.getCause());
    }

    /**
     * Represents error information sent back to the REST client.
     */
    static class ErrorResponse {

        private int status;

        private String error;
        private String message;
        private List<ValidationError> globalErrors = new ArrayList<>();
        private List<ValidationError> fieldErrors = new ArrayList<>();

        public int getStatus() {
            return status;
        }

        ErrorResponse setStatus(HttpStatus httpStatus) {
            this.status = httpStatus.value();
            return this;
        }

        public String getError() {
            return error;
        }

        public ErrorResponse setError(String error) {
            this.error = error;
            return this;
        }

        public String getMessage() {
            return message;
        }

        ErrorResponse setMessage(String message) {
            this.message = message;
            return this;
        }

        public List<ValidationError> getGlobalErrors() {
            return globalErrors;
        }

        ErrorResponse addGlobalError(ObjectError globalError) {
            this.globalErrors.add(new ValidationError().setObject(globalError.getObjectName())
                    .setMessage(globalError.getDefaultMessage()));
            return this;
        }

        public List<ValidationError> getFieldErrors() {
            return fieldErrors;
        }

        ErrorResponse addFieldError(FieldError fieldError) {
            this.fieldErrors.add(new ValidationError().setObject(fieldError.getObjectName())
                    .setField(fieldError.getField()).setMessage(fieldError.getDefaultMessage()));
            return this;
        }

    }

    /**
     * Represents a single validation problem.
     */
    static class ValidationError {

        private String object;
        private String field;
        private String message;

        public String getObject() {
            return object;
        }

        ValidationError setObject(String object) {
            this.object = object;
            return this;
        }

        public String getField() {
            return field;
        }

        ValidationError setField(String field) {
            this.field = field;
            return this;
        }

        public String getMessage() {
            return message;
        }

        ValidationError setMessage(String message) {
            this.message = message;
            return this;
        }
    }

}
