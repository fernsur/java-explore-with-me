package ru.practicum.mapper;

import ru.practicum.dto.EndpointHit;
import ru.practicum.model.StatRecord;

public class EndpointHitMapper {

    public static StatRecord fromDto(EndpointHit dto) {
        return new StatRecord(dto.getId(),
                               dto.getApp(),
                               dto.getUri(),
                               dto.getIp(),
                               dto.getTimestamp());
    }

    public static EndpointHit toDto(StatRecord hit) {
        return new EndpointHit(hit.getId(),
                                  hit.getApp(),
                                  hit.getUri(),
                                  hit.getIp(),
                                  hit.getTimestamp());
    }
}
