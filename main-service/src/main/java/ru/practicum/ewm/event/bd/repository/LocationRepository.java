package ru.practicum.ewm.event.bd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.event.bd.model.Location;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {

    Optional<Location> getLocationsByLatAndLon(Float lat, Float lon);

}