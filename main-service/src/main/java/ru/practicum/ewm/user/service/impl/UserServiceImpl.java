package ru.practicum.ewm.user.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.user.db.repository.UserRepository;
import ru.practicum.ewm.user.dto.UserRequestDto;
import ru.practicum.ewm.user.dto.UserResponseDto;
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
    public List<UserResponseDto> getUsers(Set<Long> ids, Integer from, Integer size) {
        if (ids.isEmpty()) {
            return userRepository.getAllUsers(getPageable(from, size, Sort.Direction.ASC, "id")).stream()
                    .map(userMapper::toResponseDto)
                    .collect(Collectors.toList());
        }
        return userRepository.getUsersById(ids, getPageable(from, size, Sort.Direction.ASC, "id")).stream()
                .map(userMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public UserResponseDto createUser(UserRequestDto userDto) {
        var user = userRepository.save(userMapper.toUser(userDto));
        log.info("User with id = {} is saved {}.", user.getId(), user);
        return userMapper.toResponseDto(user);
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
        log.info("User with id = {} is deleted.", userId);
    }

}
