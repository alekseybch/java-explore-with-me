package ru.practicum.ewm.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EventParametersAdminDto {

    private Set<Long> users;
    private Set<String> states;
    private Set<Long> categories;
    private String rangeStart;
    private String rangeEnd;

}
