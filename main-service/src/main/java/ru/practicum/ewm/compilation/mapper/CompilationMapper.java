package ru.practicum.ewm.compilation.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.compilation.bd.model.Compilation;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.event.bd.repository.EventRepository;
import ru.practicum.ewm.global.mapper.EntityMapper;

@Mapper(componentModel = "spring", uses = {EventRepository.class})
public interface CompilationMapper {

    @Mapping(target = "events", source = "events", qualifiedBy = EntityMapper.class)
    Compilation toCompilation(NewCompilationDto dto);

    CompilationDto toCompilationDto(Compilation compilation);

}
