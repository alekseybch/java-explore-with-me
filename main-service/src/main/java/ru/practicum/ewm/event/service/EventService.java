package ru.practicum.ewm.event.service;

import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventService {

    List<EventShortDto> getEvents(Long userId, Integer from, Integer size);

    EventFullDto getEventByUser(Long userId, Long eventId);

    List<ParticipationRequestDto> getEventRequests(Long userId, Long eventId);

    EventFullDto createEvent(Long userId, NewEventDto eventDto);

    EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequest eventDto);

    EventRequestStatusUpdateResult updateEventRequestStatus(Long userId, Long eventId, EventRequestStatusUpdateRequest eventDto);

    List<EventFullDto> findEventsByAdmin(EventParametersAdminDto paramDto, Integer from, Integer size);

    EventFullDto updateEventByAdmin(Long eventId, UpdateEventAdminRequest eventDto);

    List<EventShortDto> findEvents(EventParametersDto paramDto, Integer from, Integer size, HttpServletRequest request);

    EventFullDto getEvent(Long id, HttpServletRequest request);

}
