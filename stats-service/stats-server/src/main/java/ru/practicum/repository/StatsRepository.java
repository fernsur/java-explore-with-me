package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.dto.ViewStats;
import ru.practicum.model.StatRecord;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface StatsRepository extends JpaRepository<StatRecord, Long> {

    @Query(" SELECT new ru.practicum.dto.ViewStats(s.app, s.uri, COUNT(DISTINCT s.ip)) " +
            "FROM StatRecord s " +
            "WHERE s.timestamp BETWEEN :start AND :end " +
            "  AND (s.uri IN :uris OR :uris = NULL) " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY COUNT(DISTINCT s.ip) DESC ")
    List<ViewStats> findUniqueStats(@Param("start") LocalDateTime start,
                                    @Param("end") LocalDateTime end,
                                    @Param("uris") Set<String> uris);

    @Query(" SELECT new ru.practicum.dto.ViewStats(s.app, s.uri, COUNT(s.ip)) " +
            "FROM StatRecord s " +
            "WHERE s.timestamp BETWEEN :start AND :end " +
            "  AND (s.uri IN :uris OR :uris = NULL) " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY COUNT(s.ip) DESC ")
    List<ViewStats> findStats(@Param("start") LocalDateTime start,
                              @Param("end") LocalDateTime end,
                              @Param("uris") Set<String> uris);
}
