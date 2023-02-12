package ru.practicum.ewm.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.service.RequestService;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
@Validated
@Slf4j
public class PrivateRequestController {

    private final RequestService requestService;

    @GetMapping
    public List<ParticipationRequestDto> getEventRequests(@Positive @PathVariable Long userId) {
        log.info("Request to get event requests from user with id = {}.", userId);
        return requestService.getEventRequests(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto createEventRequest(@Positive @PathVariable Long userId,
                                                      @Positive @RequestParam Long eventId) {
        log.info("Request to create event request for event with id = {} from user with id = {}.", eventId, userId);
        return requestService.createEventRequest(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancelEventRequest(@Positive @PathVariable Long userId,
                                                      @Positive @PathVariable Long requestId) {
        log.info("Request to cancel event request with id = {} from user with id = {}.", requestId, userId);
        return requestService.cancelEventRequest(userId, requestId);
    }

}
