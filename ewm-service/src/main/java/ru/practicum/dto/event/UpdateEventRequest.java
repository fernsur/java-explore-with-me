package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.practicum.dto.Location;

import java.time.LocalDateTime;

public class UpdateEventRequest {
    private String annotation;

    @JsonProperty("category")
    private Long categoryId;

    private String description;

    private LocalDateTime eventDate;

    private Location location;

    private Boolean paid;

    private Integer participantLimit;

    private Boolean requestModeration;

    private String title;
}
