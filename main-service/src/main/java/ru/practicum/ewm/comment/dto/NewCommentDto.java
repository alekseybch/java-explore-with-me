package ru.practicum.ewm.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NewCommentDto {

    @NotBlank(message = "can't be null or blank.")
    @Size(min = 20, max = 7000, message = "must not be less than 20 or more than 7000 characters.")
    private String text;

}
