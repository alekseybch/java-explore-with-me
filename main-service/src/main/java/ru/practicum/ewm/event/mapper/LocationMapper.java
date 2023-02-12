package ru.practicum.ewm.event.mapper;

import org.mapstruct.Mapper;
import ru.practicum.ewm.event.bd.model.Location;
import ru.practicum.ewm.event.dto.LocationDto;

@Mapper(componentModel = "spring")
public interface LocationMapper {

    Location toLocation(LocationDto dto);

    LocationDto toLocationDto(Location location);

}
