package ru.practicum.ewm.user.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.user.bd.repository.UserRepository;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.dto.NewUserRequest;
import ru.practicum.ewm.user.mapper.UserMapper;
import ru.practicum.ewm.user.service.UserService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.practicum.ewm.global.utility.PageableConverter.getPageable;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserDto> getUsers(Set<Long> ids, Integer from, Integer size) {
        if (ids.isEmpty()) {
            return userRepository.getAllUsers(getPageable(from, size)).stream()
                    .map(userMapper::toUserDto)
                    .collect(Collectors.toList());
        }
        return userRepository.getUsersById(ids, getPageable(from, size)).stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDto createUser(NewUserRequest userDto) {
        var user = userRepository.save(userMapper.toUser(userDto));
        log.info("User with id = {} is saved {}.", user.getId(), user);
        return userMapper.toUserDto(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
        log.info("User with id = {} is deleted.", userId);
    }

}
