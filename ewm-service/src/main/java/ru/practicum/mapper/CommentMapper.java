package ru.practicum.mapper;

import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.NewCommentDto;
import ru.practicum.model.Comment;
import ru.practicum.model.Event;
import ru.practicum.model.User;

public class CommentMapper {
    public static Comment toEntity(NewCommentDto dto, User user, Event event) {
        return Comment.builder()
                .text(dto.getText())
                .commentator(user)
                .event(event)
                .build();
    }

    public static CommentDto toDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .created(comment.getCreated())
                .updated(comment.getUpdated())
                .commentator(UserMapper.toShortDto(comment.getCommentator()))
                .eventId(comment.getEvent().getId())
                .build();
    }
}
