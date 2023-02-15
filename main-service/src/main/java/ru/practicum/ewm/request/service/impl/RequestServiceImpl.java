package ru.practicum.ewm.request.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.event.bd.model.enums.EventStatus;
import ru.practicum.ewm.event.bd.repository.EventRepository;
import ru.practicum.ewm.global.exception.BadRequestException;
import ru.practicum.ewm.global.exception.ConflictException;
import ru.practicum.ewm.global.exception.NotFoundException;
import ru.practicum.ewm.request.bd.model.Request;
import ru.practicum.ewm.request.bd.model.enums.RequestStatus;
import ru.practicum.ewm.request.bd.repository.RequestRepository;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.mapper.RequestMapper;
import ru.practicum.ewm.request.service.RequestService;
import ru.practicum.ewm.user.bd.model.User;
import ru.practicum.ewm.user.bd.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequestServiceImpl implements RequestService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;

    @Override
    public List<ParticipationRequestDto> getEventRequests(Long userId) {
        getUser(userId);
        return requestRepository.getRequestsByRequesterId(userId).stream()
                .map(requestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ParticipationRequestDto createEventRequest(Long userId, Long eventId) {
        if (requestRepository.existsRequestByEventIdAndRequesterId(eventId, userId))
            throw new ConflictException(String.format("Request for event with id = %d to users with id = %d" +
                    " has already been created", eventId, userId));
        var user = getUser(userId);
        var event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id = %d not found", userId)));
        if (event.getInitiator().getId().equals(userId))
            throw new ConflictException(String.format("Initiator with id = %d cannot create a request", userId));
        if (!event.getStatus().equals(EventStatus.PUBLISHED))
            throw new ConflictException(String.format("Event has not been published yet, status = %s", event.getStatus()));
        var confirmedRequest = event.getConfirmedRequestsCount();
        if (confirmedRequest >= event.getParticipantLimit())
            throw new ConflictException(String.format("Request limit reached, confirmed requests = %d", confirmedRequest));
        var request = new Request();
        request.setRequester(user);
        request.setEvent(event);
        request.setCreated(LocalDateTime.now().withNano(0));
        if (event.getRequestModeration()) {
            request.setStatus(RequestStatus.PENDING);
        } else if (confirmedRequest < event.getParticipantLimit() ||
                event.getParticipantLimit().equals(0L)) {
            request.setStatus(RequestStatus.CONFIRMED);
        }
        var savedRequest = requestRepository.save(request);
        log.info("Request with id = {} is saved {}.", savedRequest.getId(), savedRequest);
        return requestMapper.toParticipationRequestDto(savedRequest);
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancelEventRequest(Long userId, Long requestId) {
        var request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(String.format("Request with id = %d not found", requestId)));
        if (!request.getRequester().getId().equals(userId))
            throw new BadRequestException(String.format("User with id = %d not requester", userId));
        request.setStatus(RequestStatus.CANCELED);
        var savedRequest = requestRepository.save(request);
        log.info("Request with id = {} status changed to = {}.", savedRequest.getId(), savedRequest.getStatus());
        return  requestMapper.toParticipationRequestDto(savedRequest);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id = %d not found", userId)));
    }

}
