package ru.practicum.ewm.event.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.StatsClient;
import ru.practicum.ewm.category.bd.repository.CategoryRepository;
import ru.practicum.ewm.event.bd.model.Event;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.global.mapper.EntityMapper;

@Mapper(componentModel = "spring", uses = {CategoryRepository.class, StatsClient.class})
public interface EventMapper {

    @Mapping(target = "category", source = "category", qualifiedBy = EntityMapper.class)
    Event toEvent(NewEventDto dto);

    @Mapping(target = "state", source = "status")
    @Mapping(target = "confirmedRequests", expression =  "java(event.getConfirmedRequestsCount())")
    EventFullDto toEventFullDto(Event event);

    @Mapping(target = "confirmedRequests", expression =  "java(event.getConfirmedRequestsCount())")
    EventShortDto toEventShortDto(Event event);

}
