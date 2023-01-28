package ru.practicum.ewm.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.db.model.Hit;
import ru.practicum.ewm.dto.ResponseStatsDto;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<Hit, Long> {
    @Query("select new ru.practicum.ewm.dto.ResponseStatsDto(h.app, h.uri, count(distinct h.ip)) " +
            "from Hit h " +
            "where h.timestamp between :startDate and :endDate " +
            "and h.uri in (:uris) " +
            "group by h.app, h.uri " +
            "order by count(distinct h.ip) desc")
    List<ResponseStatsDto> findUniqueIpStats(LocalDateTime startDate, LocalDateTime endDate, String[] uris);

    @Query("select new ru.practicum.ewm.dto.ResponseStatsDto(h.app, h.uri, count(h.ip)) " +
            "from Hit h " +
            "where h.timestamp between :startDate and :endDate " +
            "and h.uri in (:uris) " +
            "group by h.app, h.uri " +
            "order by count(h.ip) desc")
    List<ResponseStatsDto> findAllIpStats(LocalDateTime startDate, LocalDateTime endDate, String[] uris);
}
