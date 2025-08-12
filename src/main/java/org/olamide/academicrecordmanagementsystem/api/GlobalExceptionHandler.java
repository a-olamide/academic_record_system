package org.olamide.academicrecordmanagementsystem.api;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.olamide.academicrecordmanagementsystem.enums.ErrorCode;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private String uriOf(WebRequest request) {
        if (request instanceof ServletWebRequest swr) {
            return swr.getRequest().getRequestURI();
        }
        return null;
    }

    private boolean isSwagger(String uri) {
        return uri != null && (uri.startsWith("/v3/api-docs") || uri.startsWith("/swagger-ui"));
    }

    // 400: Malformed JSON
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        var uri = uriOf(request);
        if (isSwagger(uri)) {
            // Let springdoc deal with its own exceptions
            return super.handleHttpMessageNotReadable(ex, headers, status, request);
        }
        var body = ApiResponse.error(HttpStatus.BAD_REQUEST, ErrorCode.MALFORMED_JSON, "Malformed JSON request", uri);
        return handleExceptionInternal(ex, body, headers, HttpStatus.BAD_REQUEST, request);
    }

    // 400: @Valid on @RequestBody
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        var uri = uriOf(request);
        if (isSwagger(uri)) {
            return super.handleMethodArgumentNotValid(ex, headers, status, request);
        }
        var errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.groupingBy(
                        fe -> fe.getField(),
                        Collectors.mapping(fe -> fe.getDefaultMessage() == null ? "invalid" : fe.getDefaultMessage(),
                                Collectors.toList())
                ));
        var body = ApiResponse.error(HttpStatus.BAD_REQUEST, ErrorCode.VALIDATION_FAILED,
                "Validation failed: " + errors, uri);
        return handleExceptionInternal(ex, body, headers, HttpStatus.BAD_REQUEST, request);
    }

    // 400: @Validated on params/path vars
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolation(ConstraintViolationException ex, ServletWebRequest req) {
        var uri = req.getRequest().getRequestURI();
        if (isSwagger(uri)) throw ex;
        var msg = ex.getConstraintViolations().stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .collect(Collectors.joining("; "));
        return ResponseEntity.badRequest().body(
                ApiResponse.error(HttpStatus.BAD_REQUEST, ErrorCode.VALIDATION_FAILED, msg, uri));
    }

    // 400: type mismatch
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleTypeMismatch(MethodArgumentTypeMismatchException ex, ServletWebRequest req) {
        var uri = req.getRequest().getRequestURI();
        if (isSwagger(uri)) throw ex;
        var msg = "Parameter '%s' has invalid value '%s'".formatted(ex.getName(), ex.getValue());
        return ResponseEntity.badRequest().body(
                ApiResponse.error(HttpStatus.BAD_REQUEST, ErrorCode.TYPE_MISMATCH, msg, uri));
    }

    // 404
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(EntityNotFoundException ex, ServletWebRequest req) {
        var uri = req.getRequest().getRequestURI();
        if (isSwagger(uri)) throw ex;
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ApiResponse.error(HttpStatus.NOT_FOUND, ErrorCode.STUDENT_NOT_FOUND, ex.getMessage(), uri));
    }

    // 409
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataIntegrity(DataIntegrityViolationException ex, ServletWebRequest req) {
        var uri = req.getRequest().getRequestURI();
        if (isSwagger(uri)) throw ex;
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                ApiResponse.error(HttpStatus.CONFLICT, ErrorCode.DATA_INTEGRITY_VIOLATION, "Data integrity violation", uri));
    }

    // 500 fallback
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleUnhandled(Exception ex, ServletWebRequest req) throws Exception {
        var uri = req.getRequest().getRequestURI();
        if (isSwagger(uri)) throw ex; // don’t wrap springdoc errors
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.UNEXPECTED_ERROR, "Unexpected error", uri));
    }
}
