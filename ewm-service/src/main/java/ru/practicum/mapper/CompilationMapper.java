package ru.practicum.mapper;

import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.model.Compilation;
import ru.practicum.model.Event;

import java.util.List;
import java.util.stream.Collectors;

public class CompilationMapper {
    public static Compilation toEntity(CompilationDto dto, List<Event> events) {
        return new Compilation(dto.getId(),
                               dto.getTitle(),
                               dto.getPinned(),
                               events);
    }

    public static Compilation toEntity(NewCompilationDto dto, List<Event> events) {
        return Compilation.builder()
                .title(dto.getTitle())
                .pinned(dto.getPinned())
                .events(events)
                .build();
    }

    public static CompilationDto toDto(Compilation comp) {
        return CompilationDto.builder()
                .id(comp.getId())
                .title(comp.getTitle())
                .pinned(comp.getPinned())
                .events(comp.getEvents() == null ? null : comp.getEvents().stream()
                                                               .map(EventMapper::toShortDto)
                                                               .collect(Collectors.toList()))
                .build();
    }
}
