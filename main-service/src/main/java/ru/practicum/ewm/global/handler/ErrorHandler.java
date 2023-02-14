package ru.practicum.ewm.global.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.practicum.ewm.config.DateTimeFormat;
import ru.practicum.ewm.global.exception.BadRequestException;
import ru.practicum.ewm.global.exception.ConflictException;
import ru.practicum.ewm.global.exception.ForbiddenException;
import ru.practicum.ewm.global.exception.NotFoundException;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

    private static final String TIMESTAMP = "timestamp";
    private static final String MESSAGE = "message";
    private static final String REASON = "reason";
    private static final String STATUS = "status";
    private static final String ERROR = "error";

    @ExceptionHandler({NumberFormatException.class, BadRequestException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected Map<String, Object> handleBadRequest(final RuntimeException ex) {
        log.error("Error: {}", ex.getMessage(), ex);
        return getGeneralErrorBody(HttpStatus.BAD_REQUEST, ex,
                "For the requested operation the conditions are not met.");
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    protected Map<String, Object> handleForbidden(final RuntimeException ex) {
        log.error("Error: {}", ex.getMessage(), ex);
        return getGeneralErrorBody(HttpStatus.FORBIDDEN, ex,
                "For the requested operation the conditions are not met.");
    }

    @ExceptionHandler({EmptyResultDataAccessException.class, EntityNotFoundException.class, NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected Map<String, Object> handleNotFound(final RuntimeException ex) {
        log.error("Error: {}", ex.getMessage(), ex);
        return getGeneralErrorBody(HttpStatus.NOT_FOUND, ex,
                "The required object was not found.");
    }

    @ExceptionHandler({ConstraintViolationException.class, DataIntegrityViolationException.class, ConflictException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    protected Map<String, Object> handleConflict(final RuntimeException ex) {
        log.error("Error: {}", ex.getMessage(), ex);
        return getGeneralErrorBody(HttpStatus.CONFLICT, ex,
                "Integrity constraint has been violated");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected Map<String, Object> handleAllException(final Exception ex) {
        log.error("Error: {}", ex.getMessage(), ex);
        return getGeneralErrorBody(HttpStatus.INTERNAL_SERVER_ERROR, ex,
                "Error occurred");
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        log.error("Error: {}", ex.getMessage(), ex);
        Map<String, Object> body = getGeneralErrorBody(HttpStatus.BAD_REQUEST, ex,
                "For the requested operation the conditions are not met.");
        return new ResponseEntity<>(body, headers, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
                                                                          HttpHeaders headers,
                                                                          HttpStatus status,
                                                                          WebRequest request) {
        log.error("Error: {}", ex.getMessage(), ex);
        Map<String, Object> body = getGeneralErrorBody(HttpStatus.BAD_REQUEST, ex,
                "For the requested operation the conditions are not met.");
        return new ResponseEntity<>(body, headers, HttpStatus.BAD_REQUEST);
    }

    private Map<String, Object> getGeneralErrorBody(HttpStatus status, Exception ex, String reason) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(ERROR, ex.getClass());
        body.put(REASON, reason);
        body.put(MESSAGE, ex.getMessage());
        body.put(STATUS, status);
        body.put(TIMESTAMP, LocalDateTime.now().format(DateTimeFormat.getDateTimeFormatter()));
        return body;
    }

}