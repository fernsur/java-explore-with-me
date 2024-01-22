package ru.practicum.mapper;

import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.LocationDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.enums.EventState;
import ru.practicum.model.Category;
import ru.practicum.model.Event;
import ru.practicum.model.Location;
import ru.practicum.model.User;

import java.time.LocalDateTime;

public class EventMapper {
    public static Event toEntity(NewEventDto dto, User user, Category category, Location location) {
        return Event.builder()
                .title(dto.getTitle())
                .annotation(dto.getAnnotation())
                .description(dto.getDescription())
                .initiator(user)
                .category(category)
                .location(location)
                .eventDate(dto.getEventDate())
                .participantLimit(dto.getParticipantLimit() == null ? 0 : dto.getParticipantLimit())
                .paid(dto.getPaid() != null && dto.getPaid())
                .requestModeration(dto.getRequestModeration() == null || dto.getRequestModeration())
                .state(EventState.PENDING)
                .confirmedRequests(0L)
                .created(LocalDateTime.now())
                .views(0L)
                .build();
    }

    public static EventFullDto toFullDto(Event event) {
        return EventFullDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .description(event.getDescription())
                .initiator(UserMapper.toShortDto(event.getInitiator()))
                .category(CategoryMapper.toDto(event.getCategory()))
                .location(locationDto(event.getLocation()))
                .eventDate(event.getEventDate())
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .createdOn(event.getCreated())
                .publishedOn(event.getPublished())
                .confirmedRequests(event.getConfirmedRequests())
                .views(event.getViews())
                .build();
    }

    public static EventShortDto toShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .initiator(UserMapper.toShortDto(event.getInitiator()))
                .category(CategoryMapper.toDto(event.getCategory()))
                .eventDate(event.getEventDate())
                .paid(event.getPaid())
                .views(event.getViews())
                .build();
    }

    public static LocationDto locationDto(Location location) {
        return new LocationDto(location.getLat(),
                               location.getLon());
    }

    public static Location toLocation(LocationDto location) {
        return Location.builder()
                .lat(location.getLat())
                .lon(location.getLon())
                .build();
    }
}
