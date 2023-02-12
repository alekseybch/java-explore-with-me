package ru.practicum.ewm.request.bd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.request.bd.model.Request;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> getRequestsByRequesterId(Long userId);

    List<Request> getRequestsByEventId(Long eventId);

    Boolean existsRequestByEventIdAndRequesterId(Long eventId, Long userId);

}
