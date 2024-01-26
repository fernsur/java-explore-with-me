package ru.practicum.service.comment;

import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.NewCommentDto;
import ru.practicum.dto.comment.UpdateCommentDto;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentService {
    CommentDto createComment(NewCommentDto dto, long userId);

    CommentDto updateComment(UpdateCommentDto dto, long userId);

    void deleteCommentPrivate(long userId, long commentId);

    void deleteCommentAdmin(long commentId);

    CommentDto getCommentById(long commentId);

    List<CommentDto> getAllCommentsPublic(long eventId, LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                          int from, int size);

    List<CommentDto> getAllCommentsAdmin(String text, LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                         int from, int size);

    List<CommentDto> getCommentsByEventId(long eventId, LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                         int from, int size);
}
