package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.practicum.ewm.event.bd.model.enums.EventAction;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpdateEventUserRequest {

    @Size(min = 20, max = 2000, message = "must not be less than 20 or more than 200 characters.")
    private String annotation;

    @Positive(message = "cannot be negative or zero.")
    private Long category;

    @Size(min = 20, max = 7000, message = "must not be less than 20 or more than 7000 characters.")
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private LocationDto location;
    private Boolean paid;
    private Long participantLimit;
    private Boolean requestModeration;
    private EventAction stateAction;

    @Size(min = 3, max = 120, message = "must not be less than 3 or more than 120 characters.")
    private String title;

}
