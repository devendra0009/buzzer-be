package com.davendra.buzzer.exceptions;

import com.davendra.buzzer.dto.response.GlobalApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalException {

    private static final Logger logger = LoggerFactory.getLogger(GlobalException.class);

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<GlobalApiResponse<String>> handleResourceNotFoundException(NoSuchElementException ex) {
        logger.error("Resource not found: ", ex);  // Log the error
        ex.printStackTrace();
        GlobalApiResponse<String> response = new GlobalApiResponse<>(null, ex.getMessage(), false);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<GlobalApiResponse<String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        logger.warn("Invalid request: " + ex);
        ex.printStackTrace();
        GlobalApiResponse<String> response = new GlobalApiResponse<>(null, ex.getMessage(), false);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GlobalApiResponse<String>> handleGeneralException(Exception ex) {
        logger.error("Internal server error:" + ex);
        ex.printStackTrace();
        GlobalApiResponse<String> response = new GlobalApiResponse<>(null, ex.getMessage(), false);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}