package ru.practicum.ewm.category.dto;

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
public class NewCategoryDto {

    @NotBlank(message = "can't be null or blank.")
    @Size(max = 70, message = "must not be more than 70 characters.")
    private String name;

}
