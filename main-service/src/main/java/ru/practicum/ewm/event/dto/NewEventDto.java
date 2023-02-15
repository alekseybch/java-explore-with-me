package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.*;
import ru.practicum.ewm.config.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NewEventDto {

    @NotBlank(message = "can't be null or blank.")
    @Size(min = 20, max = 2000, message = "must not be less than 20 or more than 200 characters.")
    private String annotation;

    @NotNull(message = "can't be null.")
    @Positive(message = "cannot be negative or zero.")
    private Long category;

    @NotBlank(message = "can't be null or blank.")
    @Size(min = 20, max = 7000, message = "must not be less than 20 or more than 7000 characters.")
    private String description;

    @JsonFormat(pattern = DateTimeFormat.DATE_TIME_FORMAT)
    private LocalDateTime eventDate;

    @NotNull(message = "can't be null.")
    private LocationDto location;

    @JsonSetter(nulls = Nulls.SKIP)
    private Boolean paid = false;

    @PositiveOrZero(message = "cannot be negative.")
    @JsonSetter(nulls = Nulls.SKIP)
    private Integer participantLimit = 0;

    @JsonSetter(nulls = Nulls.SKIP)
    private Boolean requestModeration = false;

    @NotBlank(message = "can't be null or blank.")
    @Size(min = 3, max = 120, message = "must not be less than 3 or more than 120 characters.")
    private String title;

}
