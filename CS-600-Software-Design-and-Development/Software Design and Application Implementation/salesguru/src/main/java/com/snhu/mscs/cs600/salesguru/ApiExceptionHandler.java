package com.snhu.mscs.cs600.salesguru;

import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice(
    assignableTypes = SalesGuruPlusApiController.class
)
public class ApiExceptionHandler {

    @ExceptionHandler({
        IllegalArgumentException.class,
        MethodArgumentNotValidException.class
    })
    public ResponseEntity<Map<String, String>> badRequest(
            Exception exception) {

        String message =
            exception instanceof MethodArgumentNotValidException
                ? "Request data failed validation."
                : exception.getMessage();

        return ResponseEntity.badRequest()
            .body(Map.of("error", message));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, String>> notFound(
            NoSuchElementException exception) {

        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(Map.of("error", exception.getMessage()));
    }
}