package ru.practicum.controller.publicApi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CategoryDto;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.service.event.EventService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping(path = "/events")
@Validated
public class PublicEventController {
    private final EventService service;

    @Autowired
    public PublicEventController(EventService service) {
        this.service = service;
    }


    @GetMapping("/{eventId}")
    public ResponseEntity<EventFullDto> eventById(@Positive @PathVariable long eventId) {
        log.info("Получен GET-запрос к эндпоинту /events/{eventId} на получение информации о событии.");
        return new ResponseEntity<>(service.eventById(0, eventId), HttpStatus.OK);
    }
}
