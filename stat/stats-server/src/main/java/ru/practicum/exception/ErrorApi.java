package ru.practicum.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class ErrorApi {
    private List<String> errors;
    private HttpStatus status;
    private String reason;
    private String message;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    public ErrorApi(HttpStatus status, String reason, String message, List<String> errors) {
        this.status = status;
        this.reason = reason;
        this.message = message;
        this.errors = errors;
        this.timestamp = LocalDateTime.now();
    }

    public ErrorApi(HttpStatus status, String reason, String message) {
        this.status = status;
        this.reason = reason;
        this.message = message;
        this.errors = Arrays.asList(message);
        this.timestamp = LocalDateTime.now();
    }

    public ErrorApi(String reason, String message) {
        this.reason = reason;
        this.message = message;
        this.errors = Arrays.asList(message);
        this.timestamp = LocalDateTime.now();
    }
}
