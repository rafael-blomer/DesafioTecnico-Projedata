package br.com.rafaelblomer.factory_manager.controllers.exceptions;

import br.com.rafaelblomer.factory_manager.business.exceptions.DuplicateCodeException;
import br.com.rafaelblomer.factory_manager.business.exceptions.InsufficientStockException;
import br.com.rafaelblomer.factory_manager.business.exceptions.InvalidProductCompositionException;
import br.com.rafaelblomer.factory_manager.business.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ResourceExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> resourceNotFoundException(ResourceNotFoundException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError err = new StandardError(
                System.currentTimeMillis(), status.value(),
                "Resource not found.", e.getMessage(), request.getRequestURI()
        );
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(DuplicateCodeException.class)
    public ResponseEntity<StandardError> duplicateCodeException(DuplicateCodeException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;
        StandardError err = new StandardError(
                System.currentTimeMillis(), status.value(),
                "Duplicate code.", e.getMessage(), request.getRequestURI()
        );
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<StandardError> insufficientStockException(InsufficientStockException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        StandardError err = new StandardError(
                System.currentTimeMillis(), status.value(),
                "Insufficient stock.", e.getMessage(), request.getRequestURI()
        );
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(InvalidProductCompositionException.class)
    public ResponseEntity<StandardError> invalidProductCompositionException(InvalidProductCompositionException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError err = new StandardError(
                System.currentTimeMillis(), status.value(),
                "Invalid product composition.", e.getMessage(), request.getRequestURI()
        );
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> methodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String messages = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .reduce((msg1, msg2) -> msg1 + "; " + msg2)
                .orElse("Validation error");
        StandardError err = new StandardError(
                System.currentTimeMillis(), status.value(),
                "Validation error in fields.", messages, request.getRequestURI()
        );
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<StandardError> dataIntegrityViolationException(DataIntegrityViolationException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;
        String message = "Data integrity violation.";
        String causeMessage = e.getMostSpecificCause().getMessage().toLowerCase();
        if (causeMessage.contains("code"))
            message = "A record with this code already exists.";
        else if (causeMessage.contains("raw_material"))
            message = "This raw material is linked to one or more products and cannot be removed.";
        StandardError err = new StandardError(
                System.currentTimeMillis(), status.value(),
                "Data integrity error.", message, request.getRequestURI()
        );
        return ResponseEntity.status(status).body(err);
    }
}