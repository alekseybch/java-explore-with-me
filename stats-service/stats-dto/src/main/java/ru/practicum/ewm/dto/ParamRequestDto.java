package ru.practicum.ewm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ParamRequestDto {

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Set<String> uris;

}
