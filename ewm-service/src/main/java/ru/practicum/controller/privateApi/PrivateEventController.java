package ru.practicum.controller.privateApi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import ru.practicum.dto.event.request.ParticipationRequestDto;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.UpdateEventRequest;
import ru.practicum.dto.event.request.EventRequestStatusUpdateRequest;
import ru.practicum.dto.event.request.EventRequestStatusUpdateResult;
import ru.practicum.enums.Status;
import ru.practicum.exception.NotFoundException;
import ru.practicum.service.event.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users/{userId}/events")
@Validated
public class PrivateEventController {
    private final EventService service;

    @Autowired
    public PrivateEventController(EventService service) {
        this.service = service;
    }

    @GetMapping()
    public ResponseEntity<List<EventShortDto>> allEvents(@Positive @PathVariable long userId,
                                                         @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                         @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("Получен GET-запрос к эндпоинту /users/{userId}/events на получение событий пользователя.");
        return new ResponseEntity<>(service.allEvents(userId, from, size), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<EventFullDto> createEvent(@Positive @PathVariable long userId,
                                                    @RequestBody @Valid EventFullDto dto) {
        log.info("Получен POST-запрос к эндпоинту /users/{userId}/events на добавление нового события.");
        return new ResponseEntity<>(service.createEvent(userId, dto), HttpStatus.CREATED);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventFullDto> eventById(@Positive @PathVariable long userId,
                                                  @Positive @PathVariable long eventId) {
        log.info("Получен GET-запрос к эндпоинту /users/{userId}/events/{eventId} на получение информации о событии.");
        return new ResponseEntity<>(service.eventById(userId, eventId), HttpStatus.OK);
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> updateEvent(@Positive @PathVariable long userId,
                                                    @Positive @PathVariable long eventId,
                                                    @RequestBody @Valid UpdateEventRequest dto) {
        log.info("Получен PATCH-запрос к эндпоинту /users/{userId}/events/{eventId} на обновление информации о событии.");
        return new ResponseEntity<>(service.updateEvent(userId, eventId, dto), HttpStatus.OK);
    }

    @GetMapping("/{eventId}/requests")
    public ResponseEntity<List<ParticipationRequestDto>> allRequests(@Positive @PathVariable long userId,
                                                                     @Positive @PathVariable long eventId) {
        log.info("Получен GET-запрос к эндпоинту /users/{userId}/events/{eventId}/requests " +
                "на получение всех запросов на участие в событии.");
        return new ResponseEntity<>(service.allRequests(userId, eventId), HttpStatus.OK);
    }

    @PatchMapping("/{eventId}/requests")
    public ResponseEntity<EventRequestStatusUpdateResult> updateRequestStatus(
                                                            @PathVariable long userId,
                                                            @PathVariable long eventId,
                                                            @RequestBody EventRequestStatusUpdateRequest dto) {
        log.info("Получен PATCH-запрос к эндпоинту /users/{userId}/events/{eventId}/requests " +
                "на изменение статуса запроса на участие.");
        if (Status.from(dto.getStatus()) == null) {
            throw new NotFoundException("Incorrectly made request.");
        }
        return new ResponseEntity<>(service.updateRequestStatus(userId, eventId, dto), HttpStatus.OK);
    }
}
