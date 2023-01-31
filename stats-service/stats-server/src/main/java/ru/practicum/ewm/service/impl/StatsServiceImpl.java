package ru.practicum.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.db.repository.StatsRepository;
import ru.practicum.ewm.dto.HitRequestDto;
import ru.practicum.ewm.dto.ParamRequestDto;
import ru.practicum.ewm.dto.StatsResponseDto;
import ru.practicum.ewm.service.StatsService;
import ru.practicum.ewm.mapper.StatsMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;
    private final StatsMapper statsMapper;

    @Override
    public void createHit(HitRequestDto hitDto) {
        var hit = statsRepository.save(statsMapper.toHit(hitDto));
        log.info("hit with id = {} is saved {}", hit.getId(), hit);
    }

    @Override
    public List<StatsResponseDto> findStats(ParamRequestDto paramDto, Boolean unique) {
        List<StatsResponseDto> stats;
        if (unique) {
            stats = statsRepository.findUniqueIpStats(paramDto.getStartDate(), paramDto.getEndDate(), paramDto.getUris());
        } else {
            stats = statsRepository.findAllIpStats(paramDto.getStartDate(), paramDto.getEndDate(), paramDto.getUris());
        }
        return stats;
    }

}
