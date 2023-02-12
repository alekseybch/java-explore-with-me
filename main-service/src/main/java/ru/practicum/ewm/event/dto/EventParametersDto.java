package ru.practicum.ewm.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.event.bd.model.enums.EventSort;

import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EventParametersDto {

    private String text;
    private Set<Long> categories;
    private Boolean paid;
    private String rangeStart;
    private String rangeEnd;
    private Boolean onlyAvailable;
    private EventSort sort;

}
