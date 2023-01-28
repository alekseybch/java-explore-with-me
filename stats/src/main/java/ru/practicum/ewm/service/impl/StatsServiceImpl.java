package ru.practicum.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.db.repository.StatsRepository;
import ru.practicum.ewm.dto.RequestHitDto;
import ru.practicum.ewm.dto.ResponseStatsDto;
import ru.practicum.ewm.mapper.StatsMapper;
import ru.practicum.ewm.service.StatsService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;
    private final StatsMapper statsMapper;

    @Override
    public void createHit(RequestHitDto hitDto) {
        var hit = statsRepository.save(statsMapper.toHit(hitDto));
        log.info("hit with id = {} is saved {}", hit.getId(), hit);
    }

    @Override
    public List<ResponseStatsDto> findStats(LocalDateTime startDate, LocalDateTime endDate, String[] urisSplit, Boolean unique) {
        List<ResponseStatsDto> stats;
        if (unique) {
            stats = statsRepository.findUniqueIpStats(startDate, endDate, urisSplit);
        } else {
            stats = statsRepository.findAllIpStats(startDate, endDate, urisSplit);
        }
        return stats;
    }

}
