package ru.practicum.ewm.user.service;

import ru.practicum.ewm.user.dto.NewUserRequest;
import ru.practicum.ewm.user.dto.UserDto;

import java.util.List;
import java.util.Set;

public interface UserService {

    List<UserDto> getUsers(Set<Long> ids, Integer from, Integer size);

    UserDto createUser(NewUserRequest userDto);

    void deleteUser(Long userId);

}
