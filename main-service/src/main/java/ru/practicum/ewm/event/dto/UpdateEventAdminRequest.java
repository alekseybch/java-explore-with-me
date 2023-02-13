package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.practicum.ewm.config.DateTimeFormat;
import ru.practicum.ewm.event.bd.model.enums.EventAction;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpdateEventAdminRequest {

    private String annotation;
    private Long category;
    private String description;

    @JsonFormat(pattern = DateTimeFormat.DATE_TIME_FORMAT)
    private LocalDateTime eventDate;

    private LocationDto location;
    private Boolean paid;
    private Long participantLimit;
    private Boolean requestModeration;
    private EventAction stateAction;
    private String title;

}
