package ru.practicum.mapper;

import ru.practicum.dto.EndpointHitDto;
import ru.practicum.model.EndpointHit;

public class EndpointHitMapper {

    public static EndpointHit fromDto(EndpointHitDto dto) {
        return new EndpointHit(dto.getId(),
                               dto.getApp(),
                               dto.getUri(),
                               dto.getIp(),
                               dto.getTimestamp());
    }

    public static EndpointHitDto toDto(EndpointHit hit) {
        return new EndpointHitDto(hit.getId(),
                                  hit.getApp(),
                                  hit.getUri(),
                                  hit.getIp(),
                                  hit.getTimestamp());
    }
}
