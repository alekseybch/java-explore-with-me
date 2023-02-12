package ru.practicum.ewm.event.bd.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.event.bd.model.Event;
import ru.practicum.ewm.event.bd.model.enums.EventStatus;
import ru.practicum.ewm.global.mapper.EntityMapper;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("select e " +
            "from Event e " +
            "where e.initiator.id = :userId")
    List<Event> getAllEventsByUserId(Long userId, PageRequest pageable);

    @Query("select e " +
            "from Event e " +
            "where e.id = :eventId " +
            "and e.initiator.id = :userId")
    Event getEventByIdAndUserId(Long userId, Long eventId);

    Optional<Event> getEventByIdAndStatus(Long id, EventStatus status);

    @EntityMapper
    @Query("select e " +
            "from Event e " +
            "where e.id in (:ids)")
    Set<Event> getEventByIds(Set<Long> ids);

}
