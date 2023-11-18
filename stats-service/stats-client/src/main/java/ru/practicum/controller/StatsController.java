package ru.practicum.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.extern.slf4j.Slf4j;


import ru.practicum.dto.EndpointHitDto;
import ru.practicum.client.StatsClient;
import ru.practicum.exception.DataValidationException;

import java.time.LocalDateTime;
import java.util.Set;

@Slf4j
@Controller
@Validated
public class StatsController {
    private final StatsClient statsClient;

    @Autowired
    public StatsController(StatsClient statsClient) {
        this.statsClient = statsClient;
    }

    @PostMapping("/hit")
    public ResponseEntity<Object> createHit(@RequestBody EndpointHitDto dto) {
        log.info("Получен POST-запрос к эндпоинту /hit на сохранение информации о запросе.");
        return statsClient.createHit(dto);
    }

    @GetMapping("/stats")
    public ResponseEntity<Object> getStats(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
            @RequestParam(required = false) Set<String> uris,
            @RequestParam(required = false, defaultValue = "false") boolean unique) {
        if (start.isAfter(end)) {
            throw new DataValidationException("Дата старта не может быть после даты конца");
        }
        log.info("Получен GET-запрос к эндпоинту /stats получение статистики.");
        return statsClient.getStats(start.toString(), end.toString(), uris, unique);
    }
}
