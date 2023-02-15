package ru.practicum.ewm.global.exception;

public class ConflictException extends RuntimeException {
    public ConflictException(String massage) {
        super(massage);
    }
}
