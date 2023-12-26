package ru.practicum.service.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.UpdateEventRequest;
import ru.practicum.model.Event;
import ru.practicum.repository.EventRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class EventServiceImpl implements EventService {
    private final EventRepository repository;

    @Autowired
    public EventServiceImpl(EventRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<EventFullDto> searchEvents(Set<Long> users, Set<String> states, Set<Long> categories,
                                    LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {
        Page<Event> events;
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size, sort);

        events = repository.findEventsByParams(users, states, categories, rangeStart, rangeEnd, page);


    }

    @Override
    public EventFullDto updateEvent(long userId, long eventId, UpdateEventRequest) {

    }
}
