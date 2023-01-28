package ru.practicum.ewm.client;

import ru.practicum.ewm.client.dto.ResponseStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsClient {
    void createHit(String uri, String ip);

    List<ResponseStatsDto> findUniqueIpStats(LocalDateTime start, LocalDateTime end, String uris);

    List<ResponseStatsDto> findAllIpStats(LocalDateTime start, LocalDateTime end, String uris);
}
