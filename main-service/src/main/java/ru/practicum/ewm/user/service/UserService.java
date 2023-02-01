package ru.practicum.ewm.user.service;

import ru.practicum.ewm.user.dto.UserRequestDto;
import ru.practicum.ewm.user.dto.UserResponseDto;

import java.util.List;
import java.util.Set;

public interface UserService {

    List<UserResponseDto> getUsers(Set<Long> ids, Integer from, Integer size);

    UserResponseDto createUser(UserRequestDto userDto);

    void deleteUser(Long userId);

}
