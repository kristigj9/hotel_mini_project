package com.lhind.hotel.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(
            ResourceNotFoundException exception,
            HttpServletRequest request
    ) {
        log.warn(
                "Resource not found. Path: {}, message: {}",
                request.getRequestURI(),
                exception.getMessage()
        );

        return createErrorResponse(
                HttpStatus.NOT_FOUND,
                exception.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler({
            RoomNotAvailableException.class,
            RoomCapacityExceededException.class,
            IllegalArgumentException.class
    })
    public ResponseEntity<Map<String, Object>> handleBadRequest(
            RuntimeException exception,
            HttpServletRequest request
    ) {
        log.warn(
                "Bad request. Path: {}, exception: {}, message: {}",
                request.getRequestURI(),
                exception.getClass().getSimpleName(),
                exception.getMessage()
        );

        return createErrorResponse(
                HttpStatus.BAD_REQUEST,
                exception.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler({
            DuplicateEmailException.class,
            DuplicateRoomNumberException.class
    })
    public ResponseEntity<Map<String, Object>> handleConflict(
            RuntimeException exception,
            HttpServletRequest request
    ) {
        log.warn(
                "Conflict detected. Path: {}, exception: {}, message: {}",
                request.getRequestURI(),
                exception.getClass().getSimpleName(),
                exception.getMessage()
        );

        return createErrorResponse(
                HttpStatus.CONFLICT,
                exception.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {
        String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error ->
                        error.getField()
                                + ": "
                                + error.getDefaultMessage()
                )
                .findFirst()
                .orElse("Validation failed");

        log.warn(
                "Validation failed. Path: {}, message: {}",
                request.getRequestURI(),
                message
        );

        return createErrorResponse(
                HttpStatus.BAD_REQUEST,
                message,
                request.getRequestURI()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(
            Exception exception,
            HttpServletRequest request
    ) {
        log.error(
                "Unexpected error occurred. Path: {}, exception: {}",
                request.getRequestURI(),
                exception.getClass().getSimpleName(),
                exception
        );

        return createErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred",
                request.getRequestURI()
        );
    }

    private ResponseEntity<Map<String, Object>> createErrorResponse(
            HttpStatus status,
            String message,
            String path
    ) {
        Map<String, Object> errorBody = new LinkedHashMap<>();

        errorBody.put("timestamp", LocalDateTime.now());
        errorBody.put("status", status.value());
        errorBody.put("error", status.getReasonPhrase());
        errorBody.put("message", message);
        errorBody.put("path", path);

        return new ResponseEntity<>(errorBody, status);
    }
}