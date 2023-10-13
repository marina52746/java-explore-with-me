package ru.practicum.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ValidationException extends RuntimeException {
    private List<String> errors;
    private HttpStatus status;
    private String reason;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    public ValidationException(HttpStatus status, String reason, String message, List<String> errors) {
        super(message);
        this.status = status;
        this.reason = reason;
        this.errors = errors;
        this.timestamp = LocalDateTime.now();
    }

    public ValidationException(HttpStatus status, String reason, String message) {
        super(message);
        this.status = status;
        this.reason = reason;
        this.timestamp = LocalDateTime.now();
    }

    public ValidationException(String reason, String message) {
        super(message);
        this.reason = reason;
        this.timestamp = LocalDateTime.now();
    }
}