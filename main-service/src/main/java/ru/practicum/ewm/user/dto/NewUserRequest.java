package ru.practicum.ewm.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NewUserRequest {

    @NotBlank(message = "can't be null or blank.")
    @Size(max = 70, message = "must not be more than 70 characters.")
    private String name;

    @NotNull(message = "can't be null.")
    @Size(max = 254, message = "must not be more than 254 characters.")
    @Email(message = "invalid format.")
    private String email;

}
