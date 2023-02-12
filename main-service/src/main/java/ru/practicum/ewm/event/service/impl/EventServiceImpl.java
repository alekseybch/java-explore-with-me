package ru.practicum.ewm.event.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.StatsClient;
import ru.practicum.ewm.category.bd.repository.CategoryRepository;
import ru.practicum.ewm.event.bd.model.Event;
import ru.practicum.ewm.event.bd.model.enums.EventStatus;
import ru.practicum.ewm.event.bd.repository.EventRepository;
import ru.practicum.ewm.event.bd.repository.LocationRepository;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.event.mapper.LocationMapper;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.global.exception.BadRequestException;
import ru.practicum.ewm.global.exception.ConflictException;
import ru.practicum.ewm.global.exception.ForbiddenException;
import ru.practicum.ewm.global.exception.NotFoundException;
import ru.practicum.ewm.global.utility.ViewsCreator;
import ru.practicum.ewm.request.bd.model.Request;
import ru.practicum.ewm.request.bd.model.enums.RequestStatus;
import ru.practicum.ewm.request.bd.repository.RequestRepository;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.mapper.RequestMapper;
import ru.practicum.ewm.user.bd.repository.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static ru.practicum.ewm.global.utility.PageableConverter.getPageable;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;
    private final LocationRepository locationRepository;
    private final EventMapper eventMapper;
    private final RequestMapper requestMapper;
    private final LocationMapper locationMapper;
    private final ViewsCreator viewsCreator;
    private final StatsClient statsClient;
    private final EntityManager entityManager;

    @Override
    public List<EventShortDto> getEvents(Long userId, Integer from, Integer size) {
        var eventsDto = eventRepository.getAllEventsByUserId(userId, getPageable(from, size)).stream()
                .map(eventMapper::toEventShortDto)
                .collect(Collectors.toList());
        viewsCreator.addViewsToEvents(eventsDto);
        return eventsDto;
    }

    @Override
    public EventFullDto getEventByUser(Long userId, Long eventId) {
        var eventDto = eventMapper.toEventFullDto(eventRepository.getEventByIdAndUserId(userId, eventId));
        viewsCreator.addViewsToEvent(eventDto);
        return eventDto;
    }

    @Override
    public List<ParticipationRequestDto> getEventRequests(Long userId, Long eventId) {
        var event = getEvent(eventId);
        if (!event.getInitiator().getId().equals(userId))
            throw new BadRequestException(String.format("User with id = %d not initiator of event with id = %d", userId, eventId));
        return requestRepository.getRequestsByEventId(eventId).stream()
                .map(requestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventFullDto createEvent(Long userId, NewEventDto eventDto) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id = %d not found", userId)));
        if (eventDto.getEventDate() != null && eventDto.getEventDate().isBefore(LocalDateTime.now().withNano(0).plusHours(2)))
            throw new ConflictException("Cannot be publish the event because the date " +
                    "of the event will be earlier than in an 2 hours: " + eventDto.getEventDate());
        var event = eventMapper.toEvent(eventDto);
        if (event.getCategory() == null)
            throw new NotFoundException(String.format("Category with id = %d not found", eventDto.getCategory()));
        event.setInitiator(user);
        event.setCreatedOn(LocalDateTime.now().withNano(0));
        var savedLocation = locationRepository.getLocationsByLatAndLon(event.getLocation().getLat(), event.getLocation().getLon())
                .orElse(locationRepository.save(event.getLocation()));
        event.setLocation(savedLocation);
        event.setStatus(EventStatus.PENDING);
        var savedEvent = eventRepository.save(event);
        log.info("Event with id = {} is saved {}.", savedEvent.getId(), savedEvent);
        return eventMapper.toEventFullDto(savedEvent);
    }

    @Override
    @Transactional
    public EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequest eventDto) {
        var event = getEvent(eventId);
        if (!event.getInitiator().getId().equals(userId))
            throw new BadRequestException(String.format("User with id = %d not initiator of event with id = %d", userId, eventId));
        if (event.getStatus().equals(EventStatus.PUBLISHED))
            throw new ConflictException("The event cannot be updated because it has already been published");
        if (eventDto.getEventDate() != null && eventDto.getEventDate().isBefore(LocalDateTime.now().withNano(0).plusHours(2))) {
            throw new ConflictException("Cannot be publish the event because the date " +
                    "of the event will be earlier than in an 2 hours: " + eventDto.getEventDate());
        }
        if (eventDto.getEventDate() != null)
            event.setEventDate(eventDto.getEventDate());
        switch (eventDto.getStateAction()) {
            case SEND_TO_REVIEW:
                event.setStatus(EventStatus.PENDING);
                break;
            case CANCEL_REVIEW:
                event.setStatus(EventStatus.CANCELED);
                break;
            default:
                throw new ForbiddenException("stateAction is specified incorrectly in the request: "
                        + eventDto.getStateAction());
        }
        if (eventDto.getAnnotation() != null) event.setAnnotation(eventDto.getAnnotation());
        if (eventDto.getCategory() != null)
            event.setCategory(categoryRepository.findById(eventDto.getCategory())
                    .orElseThrow(() -> new NotFoundException(String.format("Category with id = %d not found", eventDto.getCategory()))));
        if (eventDto.getDescription() != null) event.setDescription(eventDto.getDescription());
        if (eventDto.getLocation() != null) {
            var location = locationMapper.toLocation(eventDto.getLocation());
            var savedLocation = locationRepository.getLocationsByLatAndLon(location.getLat(), location.getLon())
                    .orElse(locationRepository.save(location));
            event.setLocation(savedLocation);
        }
        if (eventDto.getPaid() != null) event.setPaid(eventDto.getPaid());
        if (eventDto.getParticipantLimit() != null) event.setParticipantLimit(eventDto.getParticipantLimit());
        if (eventDto.getRequestModeration() != null) event.setRequestModeration(eventDto.getRequestModeration());
        if (eventDto.getTitle() != null) event.setTitle(eventDto.getTitle());
        var savedEvent = eventRepository.save(event);
        log.info("Event with id = {} is changed {}.", savedEvent.getId(), savedEvent);
        return eventMapper.toEventFullDto(savedEvent);
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult updateEventRequestStatus(Long userId, Long eventId,
                                                                   EventRequestStatusUpdateRequest eventDto) {
        var event = getEvent(eventId);
        if (!event.getInitiator().getId().equals(userId))
            throw new BadRequestException(String.format("User with id = %d not initiator of event with id = %d", userId, eventId));
        if (event.getParticipantLimit().equals(0L) || !event.getRequestModeration()) {
            return new EventRequestStatusUpdateResult();
        }

        var countConfirmedReq = event.getConfirmedRequestsCount();
        if (eventDto.getStatus().equals(RequestStatus.CONFIRMED) && countConfirmedReq >= event.getParticipantLimit())
                throw new ConflictException("The participant limit has been reached");

        List<Request> confirmedRequests = new ArrayList<>();
        List<Request> rejectedRequests = new ArrayList<>();

        List<Request> requests = event.getRequests().stream()
                .filter(o -> eventDto.getRequestIds().contains(o.getId()))
                .collect(Collectors.toList());
        Set<Long> requestsId = requests.stream()
                .map(Request::getId)
                .collect(Collectors.toSet());
        eventDto.getRequestIds().forEach(o -> {
            if (!requestsId.contains(o)) {
                throw new NotFoundException(String.format("Request with id = %d not found", o));
            }
        });

        switch (eventDto.getStatus()) {
            case CONFIRMED:
                for (Request request : requests) {
                    checkRequestStatus(request);
                    if (countConfirmedReq < event.getParticipantLimit()) {
                        request.setStatus(RequestStatus.CONFIRMED);
                        confirmedRequests.add(request);
                        countConfirmedReq++;
                    } else {
                        request.setStatus(RequestStatus.REJECTED);
                        rejectedRequests.add(request);
                    }
                }
                break;
            case REJECTED:
                for (Request request : requests) {
                    checkRequestStatus(request);
                    request.setStatus(RequestStatus.REJECTED);
                    rejectedRequests.add(request);
                }
        }

        requestRepository.saveAll(confirmedRequests);
        requestRepository.saveAll(rejectedRequests);
        var confirmedRequestsDto = requestMapper.toListParticipationRequestDto(confirmedRequests);
        var rejectedRequestsDto = requestMapper.toListParticipationRequestDto(rejectedRequests);
        return new EventRequestStatusUpdateResult(confirmedRequestsDto, rejectedRequestsDto);
    }

    @Override
    public List<EventFullDto> findEventsByAdmin(EventParametersAdminDto paramDto, Integer from, Integer size) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> criteriaQuery = cb.createQuery(Event.class);
        Root<Event> eventRoot = criteriaQuery.from(Event.class);
        criteriaQuery.select(eventRoot);
        List<Predicate> predicates = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        if (paramDto.getRangeStart() != null && paramDto.getRangeEnd() != null)
            predicates.add(cb.between(eventRoot.get("eventDate"),
                    LocalDateTime.parse(paramDto.getRangeStart(), formatter),
                    LocalDateTime.parse(paramDto.getRangeEnd(), formatter)));

        if (paramDto.getStates() != null && !paramDto.getStates().isEmpty()) {
            Set<EventStatus> eventStatus = paramDto.getStates().stream()
                    .map(EventStatus::valueOf)
                    .collect(Collectors.toSet());
            Expression<EventStatus> stateExp = eventRoot.get("status");
            predicates.add(stateExp.in(eventStatus));
        }

        if (paramDto.getCategories() != null && !paramDto.getCategories().isEmpty()) {
            Expression<Long> categoryExp = eventRoot.get("category").get("id");
            predicates.add(categoryExp.in(paramDto.getCategories()));
        }

        if (paramDto.getUsers() != null && !paramDto.getUsers().isEmpty()) {
            Expression<Long> userExp = eventRoot.get("initiator").get("id");
            predicates.add(userExp.in(paramDto.getUsers()));
        }

        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        TypedQuery<Event> typedQuery = entityManager.createQuery(criteriaQuery);
        PageRequest pageable = getPageable(from, size);
        typedQuery.setFirstResult(pageable.getPageNumber());
        typedQuery.setMaxResults(pageable.getPageSize());
        List<Event> events = typedQuery.getResultList();

        List<EventFullDto> eventsDto = events.stream()
                .map(eventMapper::toEventFullDto)
                .collect(Collectors.toList());

        viewsCreator.addViewsToEvents(eventsDto);

        return eventsDto;
    }

    @Override
    @Transactional
    public EventFullDto updateEventByAdmin(Long eventId, UpdateEventAdminRequest eventDto) {
        var publishedOn = LocalDateTime.now();
        var event = getEvent(eventId);
        if (event.getStatus() != EventStatus.PENDING)
            throw new ConflictException("Cannot publish the event because it's not in the right state: " + event.getStatus());
        if (eventDto.getEventDate() != null && eventDto.getEventDate().isBefore(publishedOn.plusHours(1)))
            throw new ConflictException("Cannot be publish the event because the date of the event will be earlier than in an hour: " + event.getEventDate());
        event.setPublishedOn(publishedOn);
        if (eventDto.getEventDate() != null) event.setEventDate(eventDto.getEventDate());
        switch (eventDto.getStateAction()) {
            case PUBLISH_EVENT:
                event.setStatus(EventStatus.PUBLISHED);
                break;
            case REJECT_EVENT:
                event.setStatus(EventStatus.REJECTED);
                break;
            default:
                throw new BadRequestException("stateAction is specified incorrectly in the request: "
                        + eventDto.getStateAction());
        }
        if (eventDto.getAnnotation() != null) event.setAnnotation(eventDto.getAnnotation());
        if (eventDto.getCategory() != null)
            event.setCategory(categoryRepository.findById(eventDto.getCategory())
                    .orElseThrow(() -> new NotFoundException(String.format("Category with id = %d not found", eventDto.getCategory()))));
        if (eventDto.getDescription() != null) event.setDescription(eventDto.getDescription());
        if (eventDto.getLocation() != null) {
            var location = locationMapper.toLocation(eventDto.getLocation());
            var savedLocation = locationRepository.getLocationsByLatAndLon(location.getLat(), location.getLon())
                    .orElse(locationRepository.save(location));
            event.setLocation(savedLocation);
        }
        if (eventDto.getPaid() != null) event.setPaid(eventDto.getPaid());
        if (eventDto.getParticipantLimit() != null) event.setParticipantLimit(eventDto.getParticipantLimit());
        if (eventDto.getRequestModeration() != null) event.setRequestModeration(eventDto.getRequestModeration());
        if (eventDto.getTitle() != null) event.setTitle(eventDto.getTitle());
        var savedEvent = eventRepository.save(event);
        log.info("[ADMIN] Event with id = {} is changed {}.", savedEvent.getId(), savedEvent);
        return eventMapper.toEventFullDto(savedEvent);
    }

    @Override
    public List<EventShortDto> findEvents(EventParametersDto paramDto, Integer from, Integer size, HttpServletRequest request) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> criteriaQuery = cb.createQuery(Event.class);
        Root<Event> eventRoot = criteriaQuery.from(Event.class);
        criteriaQuery.select(eventRoot);
        List<Predicate> predicates = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        if (paramDto.getRangeStart() != null && paramDto.getRangeEnd() != null) {
            predicates.add(cb.between(eventRoot.get("eventDate"),
                    LocalDateTime.parse(paramDto.getRangeStart(), formatter),
                    LocalDateTime.parse(paramDto.getRangeEnd(), formatter)));
        } else {
            predicates.add(cb.greaterThanOrEqualTo(eventRoot.get("eventDate"), LocalDateTime.now().withNano(0)));
        }

        if (paramDto.getPaid() != null)
            predicates.add(cb.equal(eventRoot.get("paid"), paramDto.getPaid()));

        if (paramDto.getOnlyAvailable()) {
            Subquery<Long> subQuery = criteriaQuery.subquery(Long.class);
            Root<Request> requestRoot = subQuery.from(Request.class);
            List<Predicate> subPredicates = new ArrayList<>();
            subPredicates.add(cb.equal(requestRoot.get("event").get("id"), eventRoot.get("id")));
            subPredicates.add(cb.equal(requestRoot.get("status"), RequestStatus.CONFIRMED));
            subQuery.select(cb.count(requestRoot.get("id"))).where(subPredicates.toArray(new Predicate[0]));
            predicates.add(cb.lessThan(eventRoot.get("participantLimit"), subQuery));
        }

        if (paramDto.getCategories() != null && !paramDto.getCategories().isEmpty()) {
            Expression<Long> categoryExp = eventRoot.get("category").get("id");
            predicates.add(categoryExp.in(paramDto.getCategories()));
        }

        if (paramDto.getText() != null) {
            predicates.add(cb.or(cb.like(cb.lower(eventRoot.get("annotation").as(String.class)),
                            cb.literal('%' + paramDto.getText().toLowerCase() + '%')),
                    cb.like(cb.lower(eventRoot.get("description").as(String.class)),
                            cb.literal('%' + paramDto.getText().toLowerCase() + '%'))));
        }

        criteriaQuery.where(predicates.toArray(new Predicate[0]));

        switch (paramDto.getSort()) {
            case EVENT_DATE:
                criteriaQuery.orderBy(cb.asc(eventRoot.get("eventDate")));
                break;
            case EVENT_COUNT:
                SetJoin<Event, Request> requests = eventRoot.joinSet("requests", JoinType.LEFT);
                criteriaQuery.orderBy(cb.desc(cb.count(requests)));
                break;
            default:
                criteriaQuery.orderBy(cb.asc(eventRoot.get("id")));
        }

        TypedQuery<Event> typedQuery = entityManager.createQuery(criteriaQuery);
        PageRequest pageable = getPageable(from, size);
        typedQuery.setFirstResult(pageable.getPageNumber());
        typedQuery.setMaxResults(pageable.getPageSize());
        List<Event> events = typedQuery.getResultList();

        statsClient.createHit(request.getRequestURI(), request.getRemoteAddr());

        List<EventShortDto> eventsDto = events.stream()
                .map(eventMapper::toEventShortDto)
                .collect(Collectors.toList());

        viewsCreator.addViewsToEvents(eventsDto);

        return eventsDto;
    }

    @Override
    public EventFullDto getEvent(Long id, HttpServletRequest request) {
        var event = eventRepository.getEventByIdAndStatus(id, EventStatus.PUBLISHED)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id = %d not found", id)));
        statsClient.createHit(request.getRequestURI(), request.getRemoteAddr());
        var eventDto = eventMapper.toEventFullDto(event);
        viewsCreator.addViewsToEvent(eventDto);
        return eventDto;
    }

    private Event getEvent(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id = %d not found", eventId)));
    }

    private void checkRequestStatus(Request request) {
        if (request.getStatus().equals(RequestStatus.CONFIRMED))
            throw new ConflictException(String.format(
                    "Request with id = %d has already been published", request.getId()));
        if (request.getStatus().equals(RequestStatus.REJECTED))
            throw new ConflictException(String.format(
                    "Request with id = %d has been rejected and cannot be published", request.getId()));
        if (request.getStatus().equals(RequestStatus.CANCELED))
            throw new ConflictException(String.format(
                    "Request with id = %d has been canceled and cannot be published", request.getId()));
    }

}
