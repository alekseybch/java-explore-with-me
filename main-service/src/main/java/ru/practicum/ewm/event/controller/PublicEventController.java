package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.bd.model.enums.EventSort;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventParametersDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Validated
@Slf4j
public class PublicEventController {

    private final EventService eventService;

    @GetMapping
    public List<EventShortDto> findEvents(@RequestParam(required = false) String text,
                                          @RequestParam(required = false) Set<Long> categories,
                                          @RequestParam(required = false) Boolean paid,
                                          @RequestParam(required = false) String rangeStart,
                                          @RequestParam(required = false) String rangeEnd,
                                          @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                          @RequestParam(defaultValue = "EVENT_ID") String sort,
                                          @PositiveOrZero @RequestParam(value = "from",
                                                 defaultValue = "0") Integer from,
                                          @Positive @RequestParam(value = "size",
                                                 defaultValue = "10") Integer size,
                                          HttpServletRequest request) {
        EventSort sortParam = EventSort.from(sort)
                .orElseThrow(() -> new IllegalArgumentException("Unknown sort: " + sort));
        var paramDto = new EventParametersDto(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sortParam);
        log.info("Request to find events, parameters = {}, from = {}, size = {}.", paramDto, from, size);
        return eventService.findEvents(paramDto, from, size, request);
    }

    @GetMapping("/{id}")
    public EventFullDto getEvent(@Positive @PathVariable Long id,
                                 HttpServletRequest request) {
        log.info("Request to get event with id = {} from ip = {}.", id, request.getRemoteAddr());
        return eventService.getEvent(id, request);
    }

}
