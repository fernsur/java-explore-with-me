package ru.practicum.service.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.enums.EventState;
import ru.practicum.enums.RequestStatus;
import ru.practicum.exception.ConflictDataException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.RequestMapper;
import ru.practicum.model.Event;
import ru.practicum.model.Request;
import ru.practicum.model.User;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.RequestRepository;
import ru.practicum.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;

    private final EventRepository eventRepository;

    private final UserRepository userRepository;

    @Autowired
    public RequestServiceImpl(RequestRepository requestRepository, EventRepository eventRepository,
                              UserRepository userRepository) {
        this.requestRepository = requestRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<ParticipationRequestDto> getAllRequests(long userId) {
        findUserById(userId);
        return requestRepository.findAllByRequesterId(userId).stream()
                .map(RequestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto createRequest(long userId, long eventId) {
        User requester = findUserById(userId);
        Event event = findEventById(eventId);
        Request request = new Request();

        if (event.getInitiator().getId() == userId) {
            throw new ConflictDataException("Создатель события не может добавить запрос на участие в своем событии.");
        }

        if (event.getState() != EventState.PUBLISHED) {
            throw new ConflictDataException("Нельзя участвовать в неопубликованном событии.");
        }

        if (requestRepository.findByRequesterIdAndEventId(userId, eventId).isPresent()) {
            throw new ConflictDataException("Запрос уже существует.");
        }

        if (event.getParticipantLimit() > 0 &&
                event.getParticipantLimit() == event.getConfirmedRequests().longValue()) {
            throw new ConflictDataException("Достигнут лимит запросов на участие.");
        }

        request.setRequester(requester);
        request.setEvent(event);
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            request.setStatus(RequestStatus.CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        } else {
            request.setStatus(RequestStatus.PENDING);
        }
        request.setCreated(LocalDateTime.now());

        return RequestMapper.toDto(requestRepository.save(request));
    }

    @Override
    public ParticipationRequestDto cancelRequest(long userId, long requestId) {
        findUserById(userId);
        Request request = requestRepository.findByIdAndRequesterId(requestId, userId)
                .orElseThrow(() -> new NotFoundException("Невозможно найти. Такого запроса нет."));

        request.setStatus(RequestStatus.CANCELED);
        return RequestMapper.toDto(requestRepository.save(request));
    }

    private User findUserById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Невозможно найти. Такого пользователя нет."));
    }

    private Event findEventById(long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Невозможно найти. Такого события нет."));
    }
}
