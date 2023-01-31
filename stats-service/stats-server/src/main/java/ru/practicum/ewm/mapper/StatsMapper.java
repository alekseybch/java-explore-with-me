package ru.practicum.ewm.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.dto.HitRequestDto;
import ru.practicum.ewm.db.model.Hit;

@Mapper(componentModel = "spring", uses = {AppMapper.class})
public interface StatsMapper {

    @Mapping(target = "app", source = "dto.app", qualifiedBy = StringToApp.class)
    Hit toHit(HitRequestDto dto);

}
