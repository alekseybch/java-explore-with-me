package ru.practicum.ewm.bd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.bd.model.Hit;
import ru.practicum.ewm.dto.ViewStats;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Repository
public interface StatsRepository extends JpaRepository<Hit, Long> {

    @Query("select new ru.practicum.ewm.dto.ViewStats(h.app.name, h.uri, count(distinct h.ip)) " +
            "from Hit h " +
            "where h.timestamp between :startDate and :endDate " +
            "and h.uri in (:uris) " +
            "group by h.uri, h.ip, h.app.name " +
            "order by count(distinct h.ip) desc")
    List<ViewStats> findUniqueIpStats(LocalDateTime startDate, LocalDateTime endDate, Set<String> uris);

    @Query("select new ru.practicum.ewm.dto.ViewStats(h.app.name, h.uri, count(h.ip)) " +
            "from Hit h " +
            "where h.timestamp between :startDate and :endDate " +
            "and h.uri in (:uris) " +
            "group by h.uri, h.ip, h.app.name " +
            "order by count(h.ip) desc")
    List<ViewStats> findAllIpStats(LocalDateTime startDate, LocalDateTime endDate, Set<String> uris);

}
