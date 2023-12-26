package ru.practicum.controller.adminApi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.UpdateEventRequest;
import ru.practicum.service.event.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping(path = "/admin/events")
@Validated
public class AdminEventController {
    private final EventService service;

    @Autowired
    public AdminEventController(EventService service) {
        this.service = service;
    }

    @GetMapping()
    public ResponseEntity<List<EventFullDto>> searchEvents(@RequestParam(required = false) Set<Long> users,
                                                           @RequestParam(required = false) Set<String> states,
                                                           @RequestParam(required = false) Set<Long> categories,
                                                           @RequestParam(required = false) LocalDateTime rangeStart,
                                                           @RequestParam(required = false) LocalDateTime rangeEnd,
                                                           @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                           @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("Получен GET-запрос к эндпоинту /admin/events на поиск событий.");
        return new ResponseEntity<>(service.searchEvents(users, states, categories, rangeStart,
                rangeEnd, from, size), HttpStatus.OK);
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> updateEvent(@Positive @PathVariable long eventId,
                                                    @RequestBody @Valid UpdateEventRequest dto) {
        log.info("Получен PATCH-запрос к эндпоинту /admin/events/{eventId} на обновление события.");
        return new ResponseEntity<>(service.updateEvent(0, eventId, dto), HttpStatus.OK);
    }
}
