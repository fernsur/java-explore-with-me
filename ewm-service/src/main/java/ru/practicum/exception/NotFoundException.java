package ru.practicum.exception;

public class NotFoundException extends IllegalArgumentException {
    public NotFoundException(String message) {
        super(message);
    }
}
