package ru.practicum.ewm.request.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.practicum.ewm.request.bd.model.enums.RequestStatus;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EventRequestStatusUpdateRequest {

    @NotNull(message = "can't be null.")
    @NotEmpty(message = "ids must be specified.")
    private Set<Long> requestIds;

    @NotNull(message = "can't be null.")
    private RequestStatus status;

}
