package ru.practicum.ewm.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.ToString;
import ru.practicum.ewm.utility.TimestampDeserializer;

import java.time.LocalDateTime;

@Getter
@ToString
public class RequestHitDto {

    private String app;
    private String uri;
    private String ip;
    @JsonDeserialize(using = TimestampDeserializer.class)
    private LocalDateTime timestamp;

}
