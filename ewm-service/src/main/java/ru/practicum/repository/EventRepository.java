package ru.practicum.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.enums.State;
import ru.practicum.model.Event;

import java.time.LocalDateTime;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("SELECT e FROM Event e " +
            "WHERE e.initiator.id IN (:users) " +
            "AND e.state IN (:states) " +
            "AND e.category.id IN (:categories) " +
            "AND e.date BETWEEN :rangeStart AND :rangeEnd")
    Page<Event> findEventsByParams(@Param("users") Set<Long> users,
                                   @Param("states") Set<State> states,
                                   @Param("categories") Set<Long> categories,
                                   @Param("rangeStart") LocalDateTime rangeStart,
                                   @Param("rangeEnd") LocalDateTime rangeEnd,
                                   Pageable page);
}
