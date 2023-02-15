package ru.practicum.ewm.compilation.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
public class UpdateCompilationRequest {

    private Set<Long> events;
    private Boolean pinned;

    @Size(max = 120, message = "must not be more than 120 characters.")
    private String title;

}
