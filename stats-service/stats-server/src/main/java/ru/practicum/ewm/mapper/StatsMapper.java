package ru.practicum.ewm.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.dto.EndpointHit;
import ru.practicum.ewm.bd.model.Hit;

@Mapper(componentModel = "spring", uses = {AppMapper.class})
public interface StatsMapper {

    @Mapping(target = "app", source = "dto.app", qualifiedBy = StringToApp.class)
    Hit toHit(EndpointHit dto);

}
