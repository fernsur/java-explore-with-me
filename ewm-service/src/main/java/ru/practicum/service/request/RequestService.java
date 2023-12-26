package ru.practicum.service.request;

import ru.practicum.dto.event.request.ParticipationRequestDto;

import java.util.List;

public interface RequestService {
    List<ParticipationRequestDto>  allRequests(long userId);

    ParticipationRequestDto createRequest(long userId, long eventId);

    ParticipationRequestDto cancelRequest(long userId, long eventId);
}
