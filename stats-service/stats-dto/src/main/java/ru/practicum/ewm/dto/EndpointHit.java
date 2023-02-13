package ru.practicum.ewm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.ewm.config.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EndpointHit {

    private String app;
    private String uri;
    private String ip;
    @JsonFormat(pattern = DateTimeFormat.DATE_TIME_FORMAT)
    private LocalDateTime timestamp;

}
