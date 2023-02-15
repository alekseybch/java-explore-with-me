package ru.practicum.ewm.global.utility;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.StatsClient;
import ru.practicum.ewm.dto.StatsParameterDto;
import ru.practicum.ewm.event.dto.EventShortDto;

import java.time.LocalDateTime;
import java.util.*;

@Component
@RequiredArgsConstructor
public class ViewsCreator {

    private final StatsClient statsClient;

    public void addViewsToEvents(Collection<? extends EventShortDto> eventsDto) {
        Set<String> uriSet = new HashSet<>();
        var minEventDate = LocalDateTime.now();
        for (EventShortDto event : eventsDto) {
            if (event.getEventDate().isBefore(minEventDate))
                minEventDate = event.getEventDate();
            uriSet.add("/events/" + event.getId());
        }
        var statsParameterDto = new StatsParameterDto(minEventDate, LocalDateTime.now(), uriSet);
        var viewStats = statsClient.findAllIpStats(statsParameterDto);

        Map<Long, Long> views = new HashMap<>();
        for (ru.practicum.ewm.dto.ViewStats stat : viewStats) {
            views.put(
                    Long.parseLong(stat.getUri().replace("/events/", "")),
                    stat.getHits());
        }

        eventsDto.forEach(o -> o.setViews(views.get(o.getId())));
    }

    public void addViewsToEvent(EventShortDto eventDto) {
        Set<String> uriSet = new HashSet<>();
        uriSet.add("/events/" + eventDto.getId());
        var statsParameterDto = new StatsParameterDto(eventDto.getEventDate(), LocalDateTime.now(), uriSet);
        var viewStats = statsClient.findAllIpStats(statsParameterDto);

        if (!viewStats.isEmpty()) {
            eventDto.setViews(viewStats.get(0).getHits());
        }
    }

}
