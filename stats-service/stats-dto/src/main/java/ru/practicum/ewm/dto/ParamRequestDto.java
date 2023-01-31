package ru.practicum.ewm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ParamRequestDto {

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String[] uris;

}
