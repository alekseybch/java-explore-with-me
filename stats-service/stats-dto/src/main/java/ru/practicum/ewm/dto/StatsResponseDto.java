package ru.practicum.ewm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StatsResponseDto {

    private String app;
    private String uri;
    private Long hits;

}
