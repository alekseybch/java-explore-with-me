package ru.practicum.ewm.mapper;

import org.mapstruct.Mapper;
import ru.practicum.ewm.db.model.Hit;
import ru.practicum.ewm.dto.RequestHitDto;

@Mapper(componentModel = "spring")
public interface StatsMapper {

    Hit toHit(RequestHitDto dto);

}
