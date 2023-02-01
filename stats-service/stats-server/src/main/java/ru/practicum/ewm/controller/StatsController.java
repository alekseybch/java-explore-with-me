package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.HitRequestDto;
import ru.practicum.ewm.dto.ParamRequestDto;
import ru.practicum.ewm.dto.StatsResponseDto;
import ru.practicum.ewm.service.StatsService;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(path = "/")
@RequiredArgsConstructor
@Slf4j
public class StatsController {

    private final StatsService statsService;

    @PostMapping(value = "/hit")
    public void createHit(@RequestBody HitRequestDto hitDto) {
        log.info("request to create hit {}", hitDto);
        statsService.createHit(hitDto);
    }

    @GetMapping(value = "/stats")
    public List<StatsResponseDto> findStats(@RequestParam String start,
                                            @RequestParam String end,
                                            @RequestParam Set<String> uris,
                                            @RequestParam(defaultValue = "false") Boolean unique) {
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        var paramDto = new ParamRequestDto(
                LocalDateTime.parse(URLDecoder.decode(start, StandardCharsets.UTF_8), formatter),
                LocalDateTime.parse(URLDecoder.decode(end, StandardCharsets.UTF_8), formatter),
                uris
        );
        log.info("request to find stats from date = {} to date = {} for uris = {}, unique = {}",
                paramDto.getStartDate(), paramDto.getEndDate(), paramDto.getUris(), unique);
        return statsService.findStats(paramDto, unique);
    }

}
