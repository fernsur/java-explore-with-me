package ru.practicum.service.event;

import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.UpdateEventRequest;
import ru.practicum.dto.event.request.EventRequestStatusUpdateRequest;
import ru.practicum.dto.event.request.EventRequestStatusUpdateResult;
import ru.practicum.dto.event.request.ParticipationRequestDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface EventService {
    List<EventShortDto> allEvents(long userId, int from, int size);

    EventFullDto createEvent(long userId, EventFullDto dto);

    EventFullDto eventById(long userId, long eventId);

    EventFullDto updateEvent(long userId, long eventId, UpdateEventRequest dto);

    List<ParticipationRequestDto> allRequests(long userId, long eventId);

    EventRequestStatusUpdateResult updateRequestStatus(long userId, long eventId, EventRequestStatusUpdateRequest dto);

    List<EventFullDto> searchEvents(Set<Long> users, Set<String> states, Set<Long> categories,
                                    LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size);
}
