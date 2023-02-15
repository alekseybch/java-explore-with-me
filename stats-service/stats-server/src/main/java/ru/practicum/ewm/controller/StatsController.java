package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.config.DateTimeFormat;
import ru.practicum.ewm.dto.EndpointHit;
import ru.practicum.ewm.dto.StatsParameterDto;
import ru.practicum.ewm.dto.ViewStats;
import ru.practicum.ewm.service.StatsService;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(path = "/")
@RequiredArgsConstructor
@Slf4j
public class StatsController {

    private final StatsService statsService;

    @PostMapping(value = "/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void createHit(@RequestBody EndpointHit hitDto) {
        log.info("request to create hit {}", hitDto);
        statsService.createHit(hitDto);
    }

    @GetMapping(value = "/stats")
    public List<ViewStats> findStats(@RequestParam String start,
                                     @RequestParam String end,
                                     @RequestParam Set<String> uris,
                                     @RequestParam(defaultValue = "false") Boolean unique) {
        var formatter = DateTimeFormat.getDateTimeFormatter();
        var paramDto = new StatsParameterDto(
                LocalDateTime.parse(URLDecoder.decode(start, StandardCharsets.UTF_8), formatter),
                LocalDateTime.parse(URLDecoder.decode(end, StandardCharsets.UTF_8), formatter),
                uris
        );
        log.info("request to find stats from date = {} to date = {} for uris = {}, unique = {}",
                paramDto.getStartDate(), paramDto.getEndDate(), paramDto.getUris(), unique);
        return statsService.findStats(paramDto, unique);
    }

}
