package ru.practicum.enums;

public enum Status {
    CONFIRMED,
    PENDING,
    REJECTED,
    CANCELED;

    public static Status from(String status) {
        for (Status value: Status.values()) {
            if (value.name().equalsIgnoreCase(status)) {
                return value;
            }
        }
        return null;
    }
}
