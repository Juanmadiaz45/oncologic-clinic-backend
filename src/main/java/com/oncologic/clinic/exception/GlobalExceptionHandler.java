package com.oncologic.clinic.exception;

import com.oncologic.clinic.exception.runtime.*;
import com.oncologic.clinic.exception.runtime.patient.AppointmentResultUpdateException;
import com.oncologic.clinic.exception.runtime.patient.DuplicateMedicalHistoryException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

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

    @ExceptionHandler(DomainNotFoundException.class)
    public ResponseEntity<?> handleNotFound(DomainNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body("Error: " + ex.getMessage());
    }

}