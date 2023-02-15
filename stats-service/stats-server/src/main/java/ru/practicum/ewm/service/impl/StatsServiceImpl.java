package ru.practicum.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.bd.repository.StatsRepository;
import ru.practicum.ewm.dto.EndpointHit;
import ru.practicum.ewm.dto.StatsParameterDto;
import ru.practicum.ewm.dto.ViewStats;
import ru.practicum.ewm.service.StatsService;
import ru.practicum.ewm.mapper.StatsMapper;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;
    private final StatsMapper statsMapper;

    @Override
    @Transactional
    public void createHit(EndpointHit hitDto) {
        var hit = statsRepository.save(statsMapper.toHit(hitDto));
        log.info("Hit with id = {} is saved {}", hit.getId(), hit);
    }

    @Override
    public List<ViewStats> findStats(StatsParameterDto paramDto, Boolean unique) {
        List<ViewStats> stats;
        if (unique) {
            stats = statsRepository.findUniqueIpStats(paramDto.getStartDate(), paramDto.getEndDate(), paramDto.getUris());
        } else {
            stats = statsRepository.findAllIpStats(paramDto.getStartDate(), paramDto.getEndDate(), paramDto.getUris());
        }
        return stats;
    }

}
