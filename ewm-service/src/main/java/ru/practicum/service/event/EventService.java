package ru.practicum.service.event;

import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.event.UpdateEventRequest;
import ru.practicum.dto.event.ParamEvents;
import ru.practicum.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.dto.request.ParticipationRequestDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventService {
    List<EventShortDto> getAllEvents(long userId, int from, int size);

    EventFullDto createEvent(long userId, NewEventDto dto);

    EventFullDto getEventByIdPrivate(long userId, long eventId);

    EventFullDto updateEventPrivate(long userId, long eventId, UpdateEventRequest dto);

    List<ParticipationRequestDto> getAllUserRequests(long userId, long eventId);

    EventRequestStatusUpdateResult updateRequestStatus(long userId, long eventId, EventRequestStatusUpdateRequest dto);

    List<EventShortDto> searchEvents(ParamEvents paramEvents);

    EventFullDto eventByIdPublic(long eventId, HttpServletRequest request);

    List<EventFullDto> getEventsAdmin(ParamEvents paramEvents);

    EventFullDto updateEventAdmin(long eventId, UpdateEventRequest dto);
}
