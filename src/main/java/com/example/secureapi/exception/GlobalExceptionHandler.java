package com.example.secureapi.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private String generateRequestId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(
            ResourceNotFoundException ex, WebRequest request) {
        String requestId = generateRequestId();
        log.error("Error ID: {} - {}", requestId, ex.getMessage());
        
        ErrorResponse error = new ErrorResponse(
            "RESOURCE_NOT_FOUND",
            ex.getMessage(),
            HttpStatus.NOT_FOUND.value()
        );
        error.setTimestamp(LocalDateTime.now());
        error.setPath(request.getDescription(false));
        error.setRequestId(requestId);
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(
            BadRequestException ex, WebRequest request) {
        String requestId = generateRequestId();
        log.error("Error ID: {} - {}", requestId, ex.getMessage());
        
        ErrorResponse error = new ErrorResponse(
            "BAD_REQUEST",
            ex.getMessage(),
            HttpStatus.BAD_REQUEST.value()
        );
        error.setTimestamp(LocalDateTime.now());
        error.setPath(request.getDescription(false));
        error.setRequestId(requestId);
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorized(
            UnauthorizedException ex, WebRequest request) {
        String requestId = generateRequestId();
        log.error("Error ID: {} - {}", requestId, ex.getMessage());
        
        ErrorResponse error = new ErrorResponse(
            "UNAUTHORIZED",
            "Authentication required",
            HttpStatus.UNAUTHORIZED.value()
        );
        error.setTimestamp(LocalDateTime.now());
        error.setPath(request.getDescription(false));
        error.setRequestId(requestId);
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(
            AccessDeniedException ex, WebRequest request) {
        String requestId = generateRequestId();
        log.error("Error ID: {} - {}", requestId, ex.getMessage());
        
        ErrorResponse error = new ErrorResponse(
            "FORBIDDEN",
            "You don't have permission to access this resource",
            HttpStatus.FORBIDDEN.value()
        );
        error.setTimestamp(LocalDateTime.now());
        error.setPath(request.getDescription(false));
        error.setRequestId(requestId);
        
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(
            BadCredentialsException ex, WebRequest request) {
        String requestId = generateRequestId();
        log.error("Error ID: {} - {}", requestId, ex.getMessage());
        
        ErrorResponse error = new ErrorResponse(
            "INVALID_CREDENTIALS",
            "Invalid username or password",
            HttpStatus.UNAUTHORIZED.value()
        );
        error.setTimestamp(LocalDateTime.now());
        error.setPath(request.getDescription(false));
        error.setRequestId(requestId);
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex, WebRequest request) {
        String requestId = generateRequestId();
        log.error("Error ID: {} - Validation failed", requestId);
        
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        ErrorResponse error = new ErrorResponse(
            "VALIDATION_ERROR",
            "Validation failed for one or more fields",
            HttpStatus.BAD_REQUEST.value()
        );
        error.setTimestamp(LocalDateTime.now());
        error.setPath(request.getDescription(false));
        error.setRequestId(requestId);
        error.setValidationErrors(errors);
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex, WebRequest request) {
        String requestId = generateRequestId();
        log.error("Error ID: {} - Unhandled exception: {}", requestId, ex.getMessage());
        log.debug("Full stack trace for error ID {}:", requestId, ex);
        
        ErrorResponse error = new ErrorResponse(
            "INTERNAL_ERROR",
            "An unexpected error occurred. Please try again later.",
            HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        error.setTimestamp(LocalDateTime.now());
        error.setPath(request.getDescription(false));
        error.setRequestId(requestId);
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(RateLimitExceededException.class)
    public ResponseEntity<ErrorResponse> handleRateLimitExceeded(
            RateLimitExceededException ex, WebRequest request) {
        String requestId = generateRequestId();
        log.error("Error ID: {} - {}", requestId, ex.getMessage());
        
        ErrorResponse error = new ErrorResponse(
            "RATE_LIMIT_EXCEEDED",
            "Too many requests. Please try again later.",
            HttpStatus.TOO_MANY_REQUESTS.value()
        );
        error.setTimestamp(LocalDateTime.now());
        error.setPath(request.getDescription(false));
        error.setRequestId(requestId);
        
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(error);
    }
}
