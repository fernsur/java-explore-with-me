package ru.practicum.controller.publicApi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import ru.practicum.dto.comment.CommentDto;
import ru.practicum.exception.ValidationException;
import ru.practicum.service.comment.CommentService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/comments")
@Validated
public class PublicCommentController {
    public static final LocalDateTime MAX_DATE = LocalDateTime.parse("2025-12-12T23:59:59");

    private final CommentService service;

    @Autowired
    public PublicCommentController(CommentService service) {
        this.service = service;
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentDto> getCommentById(@Positive @PathVariable long commentId) {
        log.info("Получен GET-запрос к эндпоинту /comments/{commentId} на получение комментария по id.");
        return new ResponseEntity<>(service.getCommentById(commentId), HttpStatus.OK);
    }

    @GetMapping("/events/{eventId}")
    public ResponseEntity<List<CommentDto>> getAllCommentsPublic(
                                                            @Positive @PathVariable long eventId,
                                                            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                            @RequestParam(required = false) LocalDateTime rangeStart,
                                                            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                            @RequestParam(required = false) LocalDateTime rangeEnd,
                                                            @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                            @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("Получен GET-запрос к эндпоинту /comments/events/{eventId} на получение списка комментариев.");

        if (rangeStart != null && rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            throw new ValidationException("Дата начала сортировки не может быть позже даты конца.");
        }

        if (rangeStart == null) rangeStart = LocalDateTime.now();
        if (rangeEnd == null) rangeEnd = MAX_DATE;

        return new ResponseEntity<>(service.getAllCommentsPublic(eventId, rangeStart, rangeEnd, from, size),
                HttpStatus.OK);
    }
}
