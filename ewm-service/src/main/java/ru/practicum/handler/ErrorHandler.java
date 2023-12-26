package ru.practicum.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import ru.practicum.exception.AlreadyExistsException;
import ru.practicum.exception.NotFoundException;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(final NotFoundException e) {
        return new ApiError(
                HttpStatus.NOT_FOUND.toString(),
                "The required object was not found.",
                e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleAlreadyExistsException(final AlreadyExistsException e) {
        return new ApiError(
                HttpStatus.CONFLICT.toString(),
                "Integrity constraint has been violated.",
                e.getMessage());
    }
}
