package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.HitRequestDto;
import ru.practicum.ewm.dto.ParamRequestDto;
import ru.practicum.ewm.dto.StatsResponseDto;

import java.util.List;

public interface StatsService {

    void createHit(HitRequestDto hitDto);

    List<StatsResponseDto> findStats(ParamRequestDto paramDto, Boolean unique);

}
