package ru.practicum.ewm;

import ru.practicum.ewm.dto.ParamRequestDto;
import ru.practicum.ewm.dto.StatsResponseDto;

import java.util.List;

public interface StatsClient {

    void createHit(String uri, String ip);

    List<StatsResponseDto> findUniqueIpStats(ParamRequestDto paramDto);

    List<StatsResponseDto> findAllIpStats(ParamRequestDto paramDto);

}
