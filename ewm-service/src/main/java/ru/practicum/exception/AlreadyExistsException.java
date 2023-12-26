package ru.practicum.exception;

public class AlreadyExistsException extends IllegalArgumentException {
    public AlreadyExistsException(String message) {
        super(message);
    }
}
