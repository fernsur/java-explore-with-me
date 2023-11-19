package ru.practicum.service;

import ru.practicum.dto.EndpointHit;
import ru.practicum.dto.ViewStats;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface StatsService {
    EndpointHit createHit(EndpointHit dto);

    List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, Set<String> uris, boolean unique);
}
