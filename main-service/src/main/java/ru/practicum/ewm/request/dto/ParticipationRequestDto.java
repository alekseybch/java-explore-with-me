package ru.practicum.ewm.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm.config.DateTimeFormat;
import ru.practicum.ewm.request.bd.model.enums.RequestStatus;

import java.time.LocalDateTime;

@Getter
@Setter
public class ParticipationRequestDto {

    @JsonFormat(pattern = DateTimeFormat.DATE_TIME_FORMAT)
    private LocalDateTime created;
    private Long event;
    private Long id;
    private Long requester;
    private RequestStatus status;

}
