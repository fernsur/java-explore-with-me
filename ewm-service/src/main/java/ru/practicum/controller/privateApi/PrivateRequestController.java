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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.service.request.RequestService;

import javax.validation.constraints.Positive;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users/{userId}/requests")
@Validated
public class PrivateRequestController {
    private final RequestService service;

    @Autowired
    public PrivateRequestController(RequestService service) {
        this.service = service;
    }

    @GetMapping()
    public ResponseEntity<List<ParticipationRequestDto>> allRequests(@Positive @PathVariable long userId) {
        log.info("Получен GET-запрос к эндпоинту /users/{userId}/requests " +
                "на получение заявок пользователя на участие в событиях.");
        return new ResponseEntity<>(service.allRequests(userId), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<ParticipationRequestDto> createRequest(@Positive @PathVariable long userId,
                                                                 @Positive @RequestParam long eventId) {
        log.info("Получен POST-запрос к эндпоинту /users/{userId}/requests " +
                "на добавление запроса пользователя на участие в событии.");
        return new ResponseEntity<>(service.createRequest(userId, eventId), HttpStatus.CREATED);
    }

    @PatchMapping("/{requestId}/cancel")
    public ResponseEntity<ParticipationRequestDto> cancelRequest(@Positive @PathVariable long userId,
                                                                 @Positive @PathVariable long requestId) {
        log.info("Получен PATCH-запрос к эндпоинту /users/{userId}/requests/{requestId}/cancel " +
                "на отмену запроса на участие в событии.");
        return new ResponseEntity<>(service.cancelRequest(userId, requestId), HttpStatus.OK);
    }
}
