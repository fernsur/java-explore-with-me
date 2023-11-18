package ru.practicum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStats;
import ru.practicum.mapper.EndpointHitMapper;
import ru.practicum.model.EndpointHit;
import ru.practicum.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class StatsServiceImpl implements StatsService {
    private final StatsRepository repository;

    @Autowired
    public StatsServiceImpl(StatsRepository repository) {
        this.repository = repository;
    }

    @Override
    public EndpointHitDto createHit(EndpointHitDto dto) {
        EndpointHit hit = EndpointHitMapper.fromDto(dto);
        return EndpointHitMapper.toDto(repository.save(hit));
    }

    @Override
    public List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, Set<String> uris, Boolean unique) {
        if (unique) {
            return repository.findUniqueViewStats(start, end, uris);
        } else {
            return repository.findViewStats(start, end, uris);
        }
    }
}
