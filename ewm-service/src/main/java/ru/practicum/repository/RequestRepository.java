package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.Request;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request,Long> {
    List<Request> findAllByRequesterId(long requesterId);

    List<Request> findAllByEventId(long eventId);

    List<Request> findAllByIdIn(List<Long> ids);

    Optional<Request> findByIdAndRequesterId(long requestId, long requesterId);

    Optional<Request> findByRequesterIdAndEventId(long requesterId, long eventId);
}
