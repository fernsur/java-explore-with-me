package ru.practicum.controller.adminApi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
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
import ru.practicum.dto.event.ParamEvents;
import ru.practicum.dto.event.UpdateEventRequest;
import ru.practicum.enums.EventState;
import ru.practicum.service.event.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/admin/events")
@Validated
public class AdminEventController {
    public static final LocalDateTime MAX_DATE = LocalDateTime.parse("2025-12-12T23:59:59");

    private final EventService service;

    @Autowired
    public AdminEventController(EventService service) {
        this.service = service;
    }

    @GetMapping()
    public ResponseEntity<List<EventFullDto>> searchEvents(@RequestParam(required = false) List<Long> users,
                                                           @RequestParam(required = false) List<EventState> states,
                                                           @RequestParam(required = false) List<Long> categories,
                                                           @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                           @RequestParam(required = false) LocalDateTime rangeStart,
                                                           @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                           @RequestParam(required = false) LocalDateTime rangeEnd,
                                                           @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                           @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("Получен GET-запрос к эндпоинту /admin/events на поиск событий.");

        if (rangeStart == null) rangeStart = LocalDateTime.now();
        if (rangeEnd == null) rangeEnd = MAX_DATE;
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);

        ParamEvents paramEvents = ParamEvents.builder()
                .users(users)
                .states(states)
                .categories(categories)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .page(page)
                .build();

        return new ResponseEntity<>(service.getEventsAdmin(paramEvents), HttpStatus.OK);
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> updateEventAdmin(@Positive @PathVariable long eventId,
                                                         @RequestBody @Valid UpdateEventRequest dto) {
        log.info("Получен PATCH-запрос к эндпоинту /admin/events/{eventId} на обновление события.");
        return new ResponseEntity<>(service.updateEventAdmin(eventId, dto), HttpStatus.OK);
    }
}
