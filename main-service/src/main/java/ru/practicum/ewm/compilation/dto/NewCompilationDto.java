package ru.practicum.ewm.compilation.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NewCompilationDto {

    private Set<Long> events;

    @JsonSetter(nulls = Nulls.SKIP)
    private Boolean pinned = false;

    @NotBlank(message = "can't be null or blank.")
    @Size(max = 120, message = "must not be more than 120 characters.")
    private String title;

}
