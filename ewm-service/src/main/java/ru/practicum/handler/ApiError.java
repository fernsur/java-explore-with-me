package ru.practicum.handler;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ApiError {
    private String status;
    private String reason;
    private String message;
    private String timestamp;

    public ApiError(String status, String reason, String message) {
        this.status = status;
        this.reason = reason;
        this.message = message;
        this.timestamp = LocalDateTime.now().toString();
    }
}
