package ru.practicum.ewm.global.exception;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String massage) {
        super(massage);
    }
}
