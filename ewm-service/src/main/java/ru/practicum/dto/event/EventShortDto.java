package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.practicum.dto.CategoryDto;
import ru.practicum.dto.user.UserShortDto;

import java.time.LocalDateTime;

public class EventShortDto {
    private Long id;

    private String annotation;

    private CategoryDto category;

    private long confirmedRequests;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private UserShortDto initiator;

    private boolean paid;

    private String title;

    private long views;
}
