package ru.practicum.exception;

public class ConflictDataException extends IllegalArgumentException {
    public ConflictDataException(String message) {
        super(message);
    }
}
