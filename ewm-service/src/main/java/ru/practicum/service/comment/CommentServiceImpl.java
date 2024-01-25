package ru.practicum.service.comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.NewCommentDto;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.CommentMapper;
import ru.practicum.model.Comment;
import ru.practicum.model.Event;
import ru.practicum.model.User;
import ru.practicum.repository.CommentRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final UserRepository userRepository;

    private final EventRepository eventRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, UserRepository userRepository,
                              EventRepository eventRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public CommentDto createComment(NewCommentDto dto, long userId, long eventId) {
        User user = findUserById(userId);
        Event event = findEventById(eventId);
        Comment comment = CommentMapper.toEntity(dto, user, event);
        LocalDateTime time = LocalDateTime.now();

        comment.setCreated(time);
        comment.setUpdated(time);

        return CommentMapper.toDto(commentRepository.save(comment));
    }

    @Override
    public CommentDto updateComment(NewCommentDto dto, long userId, long commentId) {
        findUserById(userId);
        Comment comment = findCommentById(commentId);

        comment.setText(dto.getText());
        comment.setUpdated(LocalDateTime.now());

        return CommentMapper.toDto(commentRepository.save(comment));
    }

    @Override
    public void deleteCommentPrivate(long userId, long commentId) {
        findUserById(userId);
        findCommentById(commentId);
        commentRepository.deleteById(commentId);
    }

    @Override
    public void deleteCommentAdmin(long commentId) {
        findCommentById(commentId);
        commentRepository.deleteById(commentId);
    }

    @Override
    public CommentDto getCommentById(long commentId) {
        return CommentMapper.toDto(findCommentById(commentId));
    }

    @Override
    public List<CommentDto> getAllCommentsPublic(LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                 int from, int size) {
        return searchComments(null, null, rangeStart, rangeEnd, from, size);
    }

    @Override
    public List<CommentDto> getAllCommentsAdmin(String text, LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                int from, int size) {
        return searchComments(text, null, rangeStart, rangeEnd, from, size);
    }

    @Override
    public List<CommentDto> getCommentsByEventId(long eventId, LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                         int from, int size) {
        return searchComments(null, eventId, rangeStart, rangeEnd, from, size);
    }

    private List<CommentDto> searchComments(String text, Long eventId, LocalDateTime rangeStart,
                                            LocalDateTime rangeEnd, int from, int size) {
        Page<Comment> comments;
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size, sort);

        comments = commentRepository.findAllByParam(text, eventId, rangeStart, rangeEnd, page);

        return comments.stream()
                .map(CommentMapper::toDto)
                .collect(Collectors.toList());
    }

    private User findUserById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Невозможно найти. Такого пользователя нет."));
    }

    private Event findEventById(long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Невозможно найти. Такого события нет."));
    }

    private Comment findCommentById(long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Невозможно найти. Такого комментария нет."));
    }
}
