package com.oncologic.clinic.exception;

import com.oncologic.clinic.exception.runtime.*;
import com.oncologic.clinic.exception.runtime.dashboard.DashboardAccessException;
import com.oncologic.clinic.exception.runtime.dashboard.UserNotDoctorException;
import com.oncologic.clinic.exception.runtime.patient.AppointmentResultUpdateException;
import com.oncologic.clinic.exception.runtime.patient.DuplicateMedicalHistoryException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    // ===== AUTHENTICATION AND AUTHORIZATION ERRORS =====

    @ExceptionHandler(BadCredentialsException.class)
    public ProblemDetail handleBadCredentialsException(BadCredentialsException ex) {
        String message = (ex.getMessage() == null || ex.getMessage().isEmpty())
                ? "The username or password is incorrect"
                : ex.getMessage();
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, message);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleAccessDeniedException(AccessDeniedException ex) {
        String message = (ex.getMessage() == null || ex.getMessage().isEmpty())
                ? "You are not authorized to access this resource"
                : ex.getMessage();
        return ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, message);
    }

    @ExceptionHandler(SignatureException.class)
    public ProblemDetail handleSignatureException(SignatureException ex) {
        String message = (ex.getMessage() == null || ex.getMessage().isEmpty())
                ? "The JWT signature is invalid"
                : ex.getMessage();
        return ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, message);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ProblemDetail handleExpiredJwtException(ExpiredJwtException ex) {
        String message = (ex.getMessage() == null || ex.getMessage().isEmpty())
                ? "The JWT token has expired"
                : ex.getMessage();
        return ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, message);
    }

    // ===== DASHBOARD SPECIFIC ERRORS =====

    /**
     * Handles cases where user is not a doctor or doesn't have doctor permissions
     * Returns 403 Forbidden instead of 500
     */
    @ExceptionHandler(UserNotDoctorException.class)
    public ResponseEntity<Map<String, Object>> handleUserNotDoctorException(UserNotDoctorException ex) {
        log.warn("User attempted to access doctor dashboard without doctor permissions: {}", ex.getMessage());

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.FORBIDDEN.value());
        response.put("error", "Access Denied");
        response.put("message", "You must be a doctor to access this resource");
        response.put("path", "/doctor-dashboard");

        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    /**
     * Handles dashboard access issues that are not user permission related
     * Returns 503 Service Unavailable for temporary issues
     */
    @ExceptionHandler(DashboardAccessException.class)
    public ResponseEntity<Map<String, Object>> handleDashboardAccessException(DashboardAccessException ex) {
        log.error("Dashboard access error: {}", ex.getMessage(), ex);

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.SERVICE_UNAVAILABLE.value());
        response.put("error", "Service Temporarily Unavailable");
        response.put("message", "Dashboard service is temporarily unavailable. Please try again later.");

        return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
    }

    // ===== VALIDATION ERRORS (400 BAD REQUEST) =====

    /**
     * Handles Bean Validation errors (@Valid, @NotBlank, etc.)
     * Returns 400 Bad Request with field-specific error details
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> response = new HashMap<>();
        Map<String, String> fieldErrors = new HashMap<>();

        // Extract specific field errors
        ex.getBindingResult().getAllErrors().forEach(error -> {
            if (error instanceof FieldError fieldError) {
                fieldErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
            } else {
                fieldErrors.put("global", error.getDefaultMessage());
            }
        });

        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Validation Failed");
        response.put("message", "Invalid input data provided");
        response.put("fieldErrors", fieldErrors);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // ===== DATA INTEGRITY ERRORS =====

    /**
     * Handles data integrity violations (FK constraints, unique constraints, etc.)
     * Returns 409 Conflict for constraints or 400 for other data issues
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        Map<String, Object> response = new HashMap<>();

        String message = ex.getMessage();
        HttpStatus status;

        // Determine an error type based on message content
        if (message != null && (message.contains("foreign key constraint") ||
                message.contains("violates foreign key") ||
                message.contains("constraint"))) {
            status = HttpStatus.CONFLICT;
            response.put("error", "Data Conflict");
            response.put("message", "Cannot perform this operation because it violates data integrity constraints. " +
                    "This record may be referenced by other records.");
        } else if (message != null && (message.contains("unique constraint") ||
                message.contains("duplicate key"))) {
            status = HttpStatus.CONFLICT;
            response.put("error", "Duplicate Data");
            response.put("message", "A record with this information already exists.");
        } else {
            status = HttpStatus.BAD_REQUEST;
            response.put("error", "Data Integrity Error");
            response.put("message", "The operation could not be completed due to data integrity issues.");
        }

        response.put("timestamp", LocalDateTime.now());
        response.put("status", status.value());

        return new ResponseEntity<>(response, status);
    }

    // ===== EXISTING DOMAIN ERRORS =====

    @ExceptionHandler(DomainNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(DomainNotFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.NOT_FOUND.value());
        response.put("error", "Not Found");
        response.put("message", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(DomainCreationException.class)
    public ProblemDetail handleCreationException(DomainCreationException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, ex.getMessage());
        problem.setProperty("errorType", "CREATION_ERROR");
        return problem;
    }

    @ExceptionHandler(AppointmentResultUpdateException.class)
    public ProblemDetail handleUpdateException(AppointmentResultUpdateException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, ex.getMessage());
        problem.setProperty("errorType", "UPDATE_ERROR");
        return problem;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Bad Request");
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateMedicalHistoryException.class)
    public ProblemDetail handleDuplicateMedicalHistoryException(DuplicateMedicalHistoryException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problem.setProperty("errorType", "DUPLICATE_MEDICAL_HISTORY");
        return problem;
    }

    // ===== GENERIC HANDLER (LAST RESORT) =====

    /**
     * Generic handler - should only catch unexpected server errors
     * Returns 500 Internal Server Error
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        // Log the full exception for debugging
        log.error("Unexpected error occurred", ex);

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put("error", "Internal Server Error");
        response.put("message", "An unexpected error occurred. Please try again later.");

        // In development, you can include more details:
        if (log.isDebugEnabled()) {
            response.put("details", ex.getMessage());
            response.put("exception", ex.getClass().getSimpleName());
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}