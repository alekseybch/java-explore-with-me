package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.RequestHitDto;
import ru.practicum.ewm.dto.ResponseStatsDto;
import ru.practicum.ewm.service.StatsService;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping(path = "/")
@RequiredArgsConstructor
@Slf4j
public class StatsController {

    private final StatsService statsService;

    @PostMapping(value = "/hit")
    public void createHit(@RequestBody RequestHitDto hitDto) {
        log.info("request to create hit {}", hitDto);
        statsService.createHit(hitDto);
    }

    @GetMapping(value = "/stats")
    public List<ResponseStatsDto> findStats(@RequestParam String start,
                                            @RequestParam String end,
                                            @RequestParam String uris,
                                            @RequestParam(defaultValue = "false") Boolean unique) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        var startDate = LocalDateTime.parse(URLDecoder.decode(start, StandardCharsets.UTF_8), formatter);
        var endDate = LocalDateTime.parse(URLDecoder.decode(end, StandardCharsets.UTF_8), formatter);
        var urisSplit  = uris.split(",");
        log.info("request to find stats from date = {} to date = {} for uris = {}, unique = {}",
                startDate, endDate, urisSplit, unique);
        return statsService.findStats(startDate, endDate, urisSplit, unique);
    }

}
