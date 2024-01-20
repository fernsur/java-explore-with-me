package ru.practicum.service.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.event.UpdateEventRequest;
import ru.practicum.dto.event.ParamEvents;
import ru.practicum.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.enums.EventState;
import ru.practicum.enums.EventStateAction;
import ru.practicum.enums.RequestStatus;
import ru.practicum.exception.ConflictDataException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.EventMapper;
import ru.practicum.mapper.RequestMapper;
import ru.practicum.model.Event;
import ru.practicum.model.Category;
import ru.practicum.model.Location;
import ru.practicum.model.User;
import ru.practicum.model.Request;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.UserRepository;
import ru.practicum.repository.LocationRepository;
import ru.practicum.repository.RequestRepository;
import ru.practicum.repository.CategoryRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Comparator;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {
    public static final int ONE_HOURS = 1;

    public static final int TWO_HOURS = 2;

    private final EventRepository eventRepository;

    private final UserRepository userRepository;

    private final CategoryRepository categoryRepository;

    private final LocationRepository locationRepository;

    private final RequestRepository requestRepository;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository, UserRepository userRepository,
                            CategoryRepository categoryRepository, LocationRepository locationRepository,
                            RequestRepository requestRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.locationRepository = locationRepository;
        this.requestRepository = requestRepository;
    }

    @Override
    public List<EventShortDto> allEvents(long userId, int from, int size) {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size, sort);

        return eventRepository.findAllByInitiatorId(userId, page).stream()
                .map(EventMapper::toShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto createEvent(long userId, NewEventDto dto) {
        if (dto.getEventDate().isBefore(LocalDateTime.now().plusHours(TWO_HOURS))) {
            throw new ConflictDataException("Дата события не может быть раньше, " +
                    "чем через два часа от текущего момента.");
        }

        User user = findUserById(userId);
        Category category = findCategoryById(dto.getCategory());
        Location location = findLocation(dto.getLocation());

        Event event = EventMapper.toEntity(dto, user, category, location);

        return EventMapper.toFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto eventByIdPrivate(long userId, long eventId) {
        findUserById(userId);
        return EventMapper.toFullDto(findEventById(eventId));
    }

    @Override
    public EventFullDto updateEventPrivate(long userId, long eventId, UpdateEventRequest dto) {
        findUserById(userId);
        Event event = findEventById(eventId);

        if (dto.getEventDate() != null && LocalDateTime.now().plusHours(TWO_HOURS).isAfter(dto.getEventDate())) {
            throw new ConflictDataException("Дата события не может быть раньше, " +
                    "чем через два часа от текущего момента.");
        }

        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictDataException("Можно изменить только отмененные события " +
                    "или события в состоянии ожидания модерации.");
        }

        if (dto.getTitle() != null) event.setTitle(dto.getTitle());
        if (dto.getAnnotation() != null) event.setAnnotation(dto.getAnnotation());
        if (dto.getDescription() != null) event.setDescription(dto.getDescription());
        if (dto.getCategory() != null) event.setCategory(findCategoryById(dto.getCategory()));
        if (dto.getLocation() != null) event.setLocation(findLocation(dto.getLocation()));
        if (dto.getEventDate() != null) event.setEventDate(dto.getEventDate());
        if (dto.getPaid() != null) event.setPaid(dto.getPaid());
        if (dto.getParticipantLimit() != null) event.setParticipantLimit(dto.getParticipantLimit());
        if (dto.getRequestModeration() != null) event.setRequestModeration(dto.getRequestModeration());
        if (dto.getStateAction() != null) event.setState(dto.getStateAction().equals("SEND_TO_REVIEW")
                ? EventState.PENDING : EventState.CANCELED);

        event = eventRepository.save(event);
        return EventMapper.toFullDto(event);
    }

    @Override
    public List<ParticipationRequestDto> allRequests(long userId, long eventId) {
        findUserById(userId);
        findEventById(eventId);
        return requestRepository.findAllByEventId(eventId).stream()
                .map(RequestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventRequestStatusUpdateResult updateRequestStatus(long userId, long eventId,
                                                              EventRequestStatusUpdateRequest dto) {
        findUserById(userId);
        Event event = findEventById(eventId);
        long limit = event.getParticipantLimit() - event.getConfirmedRequests();

        if (limit == 0) {
            throw new ConflictDataException("Достигнут лимит запросов на участие.");
        }

        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult();
        List<Request> requestList = requestRepository.findAllByIdIn(dto.getRequestIds());

        for (Request request : requestList) {
            if (request.getStatus() != RequestStatus.PENDING) {
                continue;
            }

            if (dto.getStatus().equals(RequestStatus.CONFIRMED) && limit > 0) {
                request.setStatus(RequestStatus.CONFIRMED);
                result.getConfirmedRequests().add(RequestMapper.toDto(request));
                event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                limit -= 1;
            } else {
                request.setStatus(RequestStatus.REJECTED);
                result.getRejectedRequests().add(RequestMapper.toDto(request));
            }

        }

        eventRepository.save(event);
        requestRepository.saveAll(requestList);
        return result;
    }

    @Override
    public List<EventShortDto> searchEvents(ParamEvents paramEvents, int from, int size) {
        List<EventShortDto> events;
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);

        events = eventRepository.findAllByUser(paramEvents.getText(),
                                               paramEvents.getCategories(),
                                               paramEvents.getPaid(),
                                               paramEvents.getOnlyAvailable(),
                                               paramEvents.getRangeStart(),
                                               paramEvents.getRangeEnd(),
                                               page)
                .getContent().stream()
                .map(EventMapper::toShortDto)
                .collect(Collectors.toList());

        switch (paramEvents.getSort()) {
            case EVENT_DATE:
                events.sort(Comparator.comparing(EventShortDto::getEventDate));
                break;
            case VIEWS:
                events.sort(Comparator.comparing(EventShortDto::getViews).reversed());
                break;
        }

        return events;
    }

    @Override
    public EventFullDto eventByIdPublic(long eventId, HttpServletRequest request) {
        Event event = findEventById(eventId);

        if (event.getState() != EventState.PUBLISHED) {
            throw new NotFoundException("Событие не опубликовано!");
        }

        EventFullDto dto = EventMapper.toFullDto(event);
        dto.setViews(dto.getViews() + 1);
        return dto;
    }

    @Override
    public List<EventFullDto> getEventsAdmin(ParamEvents paramEvents, int from, int size) {
        Page<Event> events;
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size, sort);

        events = eventRepository.findAllByAdmin(paramEvents.getUsers(),
                                                paramEvents.getStates(),
                                                paramEvents.getCategories(),
                                                paramEvents.getRangeStart(),
                                                paramEvents.getRangeEnd(),
                                                page);

        return events.getContent().stream()
                .map(EventMapper::toFullDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto updateEventAdmin(long eventId, UpdateEventRequest dto) {
        Event event = findEventById(eventId);

        if (dto.getEventDate() != null && LocalDateTime.now().plusHours(ONE_HOURS).isAfter(dto.getEventDate())) {
            throw new ConflictDataException("Дата события не может быть раньше, " +
                    "чем через один час от текущего момента.");
        }

        if (dto.getStateAction() != null) {
            if (event.getState() == EventState.PUBLISHED || event.getState() == EventState.CANCELED) {
                throw new ConflictDataException("Событие можно публиковать, " +
                        "только если оно в состоянии ожидания публикации.");
            }
        }

        if (dto.getTitle() != null) event.setTitle(dto.getTitle());
        if (dto.getAnnotation() != null) event.setAnnotation(dto.getAnnotation());
        if (dto.getDescription() != null) event.setDescription(dto.getDescription());
        if (dto.getCategory() != null) event.setCategory(findCategoryById(dto.getCategory()));
        if (dto.getLocation() != null) event.setLocation(findLocation(dto.getLocation()));
        if (dto.getEventDate() != null) event.setEventDate(dto.getEventDate());
        if (dto.getPaid() != null) event.setPaid(dto.getPaid());
        if (dto.getParticipantLimit() != null) event.setParticipantLimit(dto.getParticipantLimit());
        if (dto.getRequestModeration() != null) event.setRequestModeration(dto.getRequestModeration());

        if (dto.getStateAction() != null) {
            switch (EventStateAction.valueOf(dto.getStateAction())) {
                case PUBLISH_EVENT:
                    event.setState(EventState.PUBLISHED);
                    event.setPublished(LocalDateTime.now());
                    break;
                case REJECT_EVENT:
                    event.setState(EventState.CANCELED);
                    break;
            }
        }

        event = eventRepository.save(event);
        return EventMapper.toFullDto(event);
    }

    private Event findEventById(long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Невозможно найти. Такого события нет."));
    }

    private User findUserById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Невозможно найти. Такого пользователя нет."));
    }

    public Category findCategoryById(long catId) {
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Невозможно найти. Такой категории нет."));
    }

    private Location findLocation(Location location) {
        Location loc = locationRepository.findByLatAndLon(location.getLat(), location.getLon());
        return loc == null ? locationRepository.save(location) : location;
    }
}
