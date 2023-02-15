package ru.practicum.ewm.event.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LocationDto {

    @NotNull(message = "can't be null.")
    @Positive(message = "cannot be negative or zero.")
    private Float lat;

    @NotNull(message = "can't be null.")
    @Positive(message = "cannot be negative or zero.")
    private Float lon;

}
