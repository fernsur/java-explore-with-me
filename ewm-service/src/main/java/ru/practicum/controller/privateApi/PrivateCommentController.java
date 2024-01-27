package ru.practicum.controller.privateApi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.NewCommentDto;
import ru.practicum.dto.comment.UpdateCommentDto;
import ru.practicum.exception.ValidationException;
import ru.practicum.service.comment.CommentService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users/{userId}/comments")
@Validated
public class PrivateCommentController {
    public static final LocalDateTime MAX_DATE = LocalDateTime.parse("2025-12-12T23:59:59");

    private final CommentService service;

    @Autowired
    public PrivateCommentController(CommentService service) {
        this.service = service;
    }

    @PostMapping()
    public ResponseEntity<CommentDto> createComment(@RequestBody @Valid NewCommentDto dto,
                                                    @Positive @PathVariable long userId) {
        log.info("Получен POST-запрос к эндпоинту /users/{userId}/comments на добавление комментария.");
        return new ResponseEntity<>(service.createComment(dto, userId), HttpStatus.CREATED);
    }

    @PatchMapping()
    public ResponseEntity<CommentDto> updateComment(@RequestBody @Valid UpdateCommentDto dto,
                                                    @Positive @PathVariable long userId) {
        log.info("Получен PATCH-запрос к эндпоинту /users/{userId}/comments на обновление комментария.");
        return new ResponseEntity<>(service.updateComment(dto, userId), HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@Positive @PathVariable long userId,
                                              @Positive @PathVariable long commentId) {
        log.info("Получен DELETE-запрос к эндпоинту /users/{userId}/comments/{commentId} на удаление комментария.");
        service.deleteCommentPrivate(userId, commentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/events/{eventId}")
    public ResponseEntity<List<CommentDto>> getCommentsByEventId(
                                                    @Positive @PathVariable long eventId,
                                                    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                    @RequestParam(required = false) LocalDateTime rangeStart,
                                                    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                    @RequestParam(required = false) LocalDateTime rangeEnd,
                                                    @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                    @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("Получен GET-запрос к эндпоинту /users/{userId}/comments/events/{eventId}" +
                " на получение списка комментариев к событию.");

        if (rangeStart != null && rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            throw new ValidationException("Дата начала сортировки не может быть позже даты конца.");
        }

        if (rangeStart == null) rangeStart = LocalDateTime.now();
        if (rangeEnd == null) rangeEnd = MAX_DATE;

        return new ResponseEntity<>(service.getCommentsByEventId(eventId, rangeStart, rangeEnd,
                from, size), HttpStatus.OK);
    }
}
