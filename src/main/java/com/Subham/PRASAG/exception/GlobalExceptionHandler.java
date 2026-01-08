package com.Subham.PRASAG.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserExistsException.class)
    public ResponseEntity<String> handleUserExists(UserExistsException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT) // 409
                .body(ex.getMessage());
    }

    @ExceptionHandler(OTPException.class)
    public ResponseEntity<String> handleOtpException(OTPException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST) // 400
                .body(ex.getMessage());
    }

    // ===================== VALIDATION ERRORS =====================
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationErrors(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(buildResponse(
                        HttpStatus.BAD_REQUEST,
                        "Validation failed",
                        errors
                ));
    }

    // ===================== BAD CREDENTIALS =====================
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentials(
            BadCredentialsException ex) {

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(buildResponse(
                        HttpStatus.UNAUTHORIZED,
                        "Invalid email or password",
                        null
                ));
    }

    // ===================== USER NOT FOUND =====================
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> handleUserNotFound(
            UsernameNotFoundException ex) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(buildResponse(
                        HttpStatus.NOT_FOUND,
                        ex.getMessage(),
                        null
                ));
    }

    // ===================== ACCESS DENIED (403) =====================
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDenied(
            AccessDeniedException ex) {

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(buildResponse(
                        HttpStatus.FORBIDDEN,
                        "You are not authorized to access this resource",
                        null
                ));
    }

    // ===================== ILLEGAL ARGUMENT =====================
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgument(
            IllegalArgumentException ex) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(buildResponse(
                        HttpStatus.BAD_REQUEST,
                        ex.getMessage(),
                        null
                ));
    }

    // ===================== RUNTIME EXCEPTION =====================
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(
            RuntimeException ex) {

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildResponse(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        ex.getMessage(),
                        null
                ));
    }

    // ===================== GENERIC EXCEPTION =====================
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(
            Exception ex) {

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildResponse(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Something went wrong",
                        null
                ));
    }

    // ===================== RESPONSE FORMAT =====================
    private Map<String, Object> buildResponse(
            HttpStatus status,
            String message,
            Object errors) {

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", status.value());
        response.put("error", status.getReasonPhrase());
        response.put("message", message);
        response.put("details", errors);

        return response;
    }
}
