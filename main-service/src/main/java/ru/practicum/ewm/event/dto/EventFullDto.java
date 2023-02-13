package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm.config.DateTimeFormat;
import ru.practicum.ewm.event.bd.model.enums.EventStatus;

import java.time.LocalDateTime;

@Getter
@Setter
public class EventFullDto extends EventShortDto {

    @JsonFormat(pattern = DateTimeFormat.DATE_TIME_FORMAT)
    private LocalDateTime createdOn;

    private String description;

    @JsonFormat(pattern = DateTimeFormat.DATE_TIME_FORMAT)
    private LocalDateTime eventDate;

    private LocationDto location;
    private Integer participantLimit;

    @JsonFormat(pattern = DateTimeFormat.DATE_TIME_FORMAT)
    private LocalDateTime publishedOn;

    private Boolean requestModeration;
    private EventStatus state;

}
