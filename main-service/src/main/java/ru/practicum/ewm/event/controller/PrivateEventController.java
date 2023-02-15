package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
@Validated
@Slf4j
public class PrivateEventController {

    private final EventService eventService;

    @GetMapping
    public List<EventShortDto> getEvents(@Positive @PathVariable Long userId,
                                         @PositiveOrZero @RequestParam(value = "from",
                                                    defaultValue = "0") Integer from,
                                         @Positive @RequestParam(value = "size",
                                                    defaultValue = "10") Integer size) {
        log.info("Request to get events from user with id = {}, from = {}, size = {}.", userId, from, size);
        return eventService.getEvents(userId, from, size);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventByUser(@Positive @PathVariable Long userId,
                                       @Valid @Positive @PathVariable Long eventId) {
        log.info("Request to get event with id = {} from user with id = {}.", eventId, userId);
        return eventService.getEventByUser(userId, eventId);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getEventRequests(@Positive @PathVariable Long userId,
                                                          @Positive @PathVariable Long eventId) {
        log.info("Request to get requests for event with id = {} from user with id = {}.", eventId, userId);
        return eventService.getEventRequests(userId, eventId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@Positive @PathVariable Long userId,
                                    @NotNull @Valid @RequestBody NewEventDto eventDto) {
        log.info("Request to create event {} from user with id = {}.", eventDto, userId);
        return eventService.createEvent(userId, eventDto);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@Positive @PathVariable Long userId,
                                    @Positive @PathVariable Long eventId,
                                    @NotNull @Valid @RequestBody UpdateEventUserRequest eventDto) {
        log.info("Request to change event with id = {} from user with id = {}, {}.", eventId, userId, eventDto);
        return eventService.updateEvent(userId, eventId, eventDto);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult updateEventRequestStatus(@Positive @PathVariable Long userId,
                                                                   @Positive @PathVariable Long eventId,
                                                                   @NotNull @Valid @RequestBody
                                                                   EventRequestStatusUpdateRequest eventDto) {
        log.info("Request to change event request status for event with id = {} from user with id = {}, {}.",
                eventId, userId, eventDto);
        return eventService.updateEventRequestStatus(userId, eventId, eventDto);
    }

}
