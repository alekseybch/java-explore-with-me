package ru.practicum.ewm.compilation.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm.event.dto.EventShortDto;

import java.util.Set;

@Getter
@Setter
public class CompilationDto {

    private Set<EventShortDto> events;
    private Long id;
    private Boolean pinned;
    private String title;

}
