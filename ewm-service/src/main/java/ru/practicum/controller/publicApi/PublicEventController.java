package ru.practicum.controller.publicApi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import ru.practicum.dto.EndpointHit;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.ParamEvents;
import ru.practicum.enums.EventSort;
import ru.practicum.exception.ValidationException;
import ru.practicum.service.event.EventService;
import ru.practicum.service.client.StatsClient;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/events")
@Validated
public class PublicEventController {
    public static final LocalDateTime MAX_DATE = LocalDateTime.parse("2025-12-12T23:59:59");

    private final EventService service;

//    private final StatsClient stats;

    @Autowired
    public PublicEventController(EventService service) {
        this.service = service;
    }

    @GetMapping()
    public ResponseEntity<List<EventShortDto>> getEvents(@RequestParam(required = false) String text,
                                                         @RequestParam(required = false) List<Long> categories,
                                                         @RequestParam(required = false) Boolean paid,
                                                         @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                         @RequestParam(required = false) LocalDateTime rangeStart,
                                                         @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                         @RequestParam(required = false) LocalDateTime rangeEnd,
                                                         @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                                         @RequestParam(defaultValue = "EVENT_DATE") EventSort sort,
                                                         @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                         @Positive @RequestParam(defaultValue = "10") int size,
                                                         HttpServletRequest request) {
        log.info("Получен GET-запрос к эндпоинту /events на получение событий.");

        if (rangeStart != null && rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            throw new ValidationException("Дата начала сортировки не может быть позже даты конца.");
        }

        if (rangeStart == null) rangeStart = LocalDateTime.now();
        if (rangeEnd == null) rangeEnd = MAX_DATE;

        ParamEvents paramEvents = ParamEvents.builder()
                .text(text)
                .categories(categories)
                .paid(paid)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .onlyAvailable(onlyAvailable)
                .sort(sort)
                .build();

//        stats.createHit(EndpointHit.builder()
//                .app("ewm")
//                .uri(request.getRequestURI())
//                .ip(request.getRemoteAddr())
//                .timestamp(LocalDateTime.now())
//                .build());

        return new ResponseEntity<>(service.searchEvents(paramEvents, from, size), HttpStatus.OK);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventFullDto> eventByIdPublic(@Positive @PathVariable long eventId,
                                                        HttpServletRequest request) {
        log.info("Получен GET-запрос к эндпоинту /events/{eventId} на получение информации о событии.");

//        stats.createHit(EndpointHit.builder()
//                .app("ewm")
//                .uri(request.getRequestURI())
//                .ip(request.getRemoteAddr())
//                .timestamp(LocalDateTime.now())
//                .build());

        return new ResponseEntity<>(service.eventByIdPublic(eventId, request), HttpStatus.OK);
    }
}
