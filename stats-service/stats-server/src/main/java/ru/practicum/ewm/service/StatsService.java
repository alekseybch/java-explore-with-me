package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.EndpointHit;
import ru.practicum.ewm.dto.StatsParameterDto;
import ru.practicum.ewm.dto.ViewStats;

import java.util.List;

public interface StatsService {

    void createHit(EndpointHit hitDto);

    List<ViewStats> findStats(StatsParameterDto paramDto, Boolean unique);

}
