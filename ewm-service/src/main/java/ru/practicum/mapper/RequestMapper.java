package ru.practicum.mapper;

import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.model.Request;

public class RequestMapper {
    public static ParticipationRequestDto toDto(Request request) {
        return new ParticipationRequestDto(request.getId(),
                                           request.getRequester().getId(),
                                           request.getEvent().getId(),
                                           request.getStatus(),
                                           request.getCreated());
    }
}
