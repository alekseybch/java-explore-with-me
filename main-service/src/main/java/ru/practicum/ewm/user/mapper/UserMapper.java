package ru.practicum.ewm.user.mapper;

import org.mapstruct.Mapper;
import ru.practicum.ewm.user.db.model.User;
import ru.practicum.ewm.user.dto.UserRequestDto;
import ru.practicum.ewm.user.dto.UserResponseDto;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(UserRequestDto dto);

    UserResponseDto toResponseDto(User user);

}
