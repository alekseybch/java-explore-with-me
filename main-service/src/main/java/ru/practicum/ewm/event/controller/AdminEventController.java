package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventParametersAdminDto;
import ru.practicum.ewm.event.dto.UpdateEventAdminRequest;
import ru.practicum.ewm.event.service.EventService;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
@Validated
@Slf4j
public class AdminEventController {

    private final EventService eventService;

    @GetMapping
    public List<EventFullDto> findEventsByAdmin(@RequestParam(required = false) Set<Long> users,
                                                @RequestParam(required = false) Set<String> states,
                                                @RequestParam(required = false) Set<Long> categories,
                                                @RequestParam(required = false) String rangeStart,
                                                @RequestParam(required = false) String rangeEnd,
                                                @PositiveOrZero @RequestParam(value = "from",
                                                 defaultValue = "0") Integer from,
                                                @Positive @RequestParam(value = "size",
                                                 defaultValue = "10") Integer size) {
        var paramDto = new EventParametersAdminDto(
                users,
                states,
                categories,
                rangeStart,
                rangeEnd);
        log.info("[ADMIN] Request to find events, parameters = {}, from = {}, size = {}.", paramDto, from, size);
        return eventService.findEventsByAdmin(paramDto, from, size);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEventByAdmin(@Positive @PathVariable Long eventId,
                                           @NotNull @RequestBody UpdateEventAdminRequest eventDto) {
        log.info("[ADMIN] Request to change event with id = {}, {}.", eventId, eventDto);
        return eventService.updateEventByAdmin(eventId, eventDto);
    }

}
