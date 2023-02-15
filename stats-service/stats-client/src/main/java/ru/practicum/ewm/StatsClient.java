package ru.practicum.ewm;

import ru.practicum.ewm.dto.StatsParameterDto;
import ru.practicum.ewm.dto.ViewStats;

import java.util.List;

public interface StatsClient {

    void createHit(String uri, String ip);

    List<ViewStats> findUniqueIpStats(StatsParameterDto paramDto);

    List<ViewStats> findAllIpStats(StatsParameterDto paramDto);

}
