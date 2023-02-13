package ru.practicum.ewm.event.bd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.event.bd.model.Location;

import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    Optional<Location> getLocationsByLatAndLon(Float lat, Float lon);

}
