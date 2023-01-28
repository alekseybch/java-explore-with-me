package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.RequestHitDto;
import ru.practicum.ewm.dto.ResponseStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    void createHit(RequestHitDto hitDto);

    List<ResponseStatsDto> findStats(LocalDateTime startDate, LocalDateTime endDate, String[] urisSplit, Boolean unique);
}
