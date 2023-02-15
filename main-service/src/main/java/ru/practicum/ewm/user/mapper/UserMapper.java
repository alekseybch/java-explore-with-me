package ru.practicum.ewm.user.mapper;

import org.mapstruct.Mapper;
import ru.practicum.ewm.user.bd.model.User;
import ru.practicum.ewm.user.dto.NewUserRequest;
import ru.practicum.ewm.user.dto.UserDto;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(NewUserRequest dto);

    UserDto toUserDto(User user);

}
