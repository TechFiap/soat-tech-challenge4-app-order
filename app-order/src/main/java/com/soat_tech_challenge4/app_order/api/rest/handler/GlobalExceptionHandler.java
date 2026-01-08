package com.soat_tech_challenge4.app_order.api.rest.handler;

import com.soat_tech_challenge4.app_order.api.rest.dto.response.ErrorResponseDto;
import com.soat_tech_challenge4.app_order.application.exceptions.ErrorException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationExceptions(MethodArgumentNotValidException ex) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                HttpStatus.BAD_REQUEST.value(),
                String.format("%s, %s", ex.getBindingResult().getFieldErrors().getFirst().getField(),
                        ex.getBindingResult().getFieldErrors().getFirst().getDefaultMessage()
                ));

        return ResponseEntity.badRequest().body(errorResponseDto);
    }

//    @ExceptionHandler(InvalidCpfException.class)
//    public ResponseEntity<Map<String, Object>> handleInvalidCpfException(InvalidCpfException ex) {
//        Map<String, Object> body = new HashMap<>();
//        body.put("timestamp", LocalDateTime.now());
//        body.put("status", HttpStatus.BAD_REQUEST.value());
//        body.put("error", "Invalid CPF");
//        body.put("message", ex.getMessage());
//
//        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
//    }

    @ExceptionHandler(ErrorException.class)
    public ResponseEntity<ErrorResponseDto> handleErrorExceptions(ErrorException ex) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                HttpStatus.BAD_REQUEST.value(),
                String.format("%s", ex.getMessage()));

        return ResponseEntity.badRequest().body(errorResponseDto);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalArgumentExceptions(IllegalArgumentException ex) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                HttpStatus.BAD_REQUEST.value(),
                String.format("%s", ex.getMessage()));

        return ResponseEntity.badRequest().body(errorResponseDto);
    }
}