package ru.practicum.ewm.client.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseStatsDto {
    private String app;
    private String uri;
    private Long hits;
}
