package ru.practicum.ewm.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm.config.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentDto {

    private Long id;
    private Long eventId;
    private String authorName;
    private String text;

    @JsonFormat(pattern = DateTimeFormat.DATE_TIME_FORMAT)
    private LocalDateTime created;

    @JsonFormat(pattern = DateTimeFormat.DATE_TIME_FORMAT)
    private LocalDateTime edited;

}
