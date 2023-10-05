package ru.practicum.exception;

import org.springframework.http.HttpStatus;

import java.util.List;

public class ErrorApi {
    private final HttpStatus status;
    private final String reason;
    private final String message;
    private final List<String> errors;

    public ErrorApi(HttpStatus status, String reason, String message, List<String> errors) {
        this.status = status;
        this.reason = reason;
        this.message = message;
        this.errors = errors;
    }
}
