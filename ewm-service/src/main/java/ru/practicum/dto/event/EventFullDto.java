package ru.practicum.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.dto.CategoryDto;
import ru.practicum.dto.Location;
import ru.practicum.dto.user.UserShortDto;
import ru.practicum.enums.State;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventFullDto {
    private Long id;

    private String title;

    private String annotation;

    private CategoryDto category;

    private boolean paid;

    private LocalDateTime eventDate;

    private UserShortDto initiator;

    private String description;

    private Integer participantLimit;

    private State state;

    private LocalDateTime createdOn;

    private Location location;

    private boolean requestModeration;
}
