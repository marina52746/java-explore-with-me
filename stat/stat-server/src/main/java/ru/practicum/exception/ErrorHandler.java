package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler({ValidationException.class, MethodArgumentNotValidException.class, NumberFormatException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorApi handleBadRequestException(Exception ex) {
        log.info("400 {}", ex.getMessage());
        return new ErrorApi(HttpStatus.BAD_REQUEST, "Incorrectly made request.", ex.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorApi handleNotFoundException(NotFoundException ex) {
        log.info("404 {}", ex.getMessage());
        return new ErrorApi(HttpStatus.NOT_FOUND, ex.getReason(), ex.getMessage());
    }

    @ExceptionHandler({IntegrityViolationException.class, DataIntegrityViolationException.class,
            ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorApi handleIntegrityViolationException(Exception ex) {
        log.info("409 {}", ex.getMessage());
        return new ErrorApi(HttpStatus.CONFLICT, "Integrity Violation Exception", ex.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorApi handleException(Exception ex) {
        log.error("500 Error", ex);
        StringWriter out = new StringWriter();
        ex.printStackTrace(new PrintWriter(out));
        String stackTrace = out.toString();
        return new ErrorApi(HttpStatus.INTERNAL_SERVER_ERROR, "Server error", ex.getMessage(),
                Collections.singletonList(stackTrace));
    }
}
