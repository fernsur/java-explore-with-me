package ru.practicum.service;

import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStats;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface StatsService {
    EndpointHitDto createHit(EndpointHitDto dto);

    List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, Set<String> uris, boolean unique);
}
