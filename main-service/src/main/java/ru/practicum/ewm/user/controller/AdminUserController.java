package ru.practicum.ewm.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.user.dto.UserRequestDto;
import ru.practicum.ewm.user.dto.UserResponseDto;
import ru.practicum.ewm.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@Validated
@Slf4j
public class AdminUserController {

    private final UserService userService;

    @GetMapping
    public List<UserResponseDto> getUsers(@RequestParam(value = "ids") Set<Long> ids,
                                          @PositiveOrZero @RequestParam(value = "from",
                                                  defaultValue = "0") Integer from,
                                          @Positive @RequestParam(value = "size",
                                                  defaultValue = "10") Integer size) {
        log.info("Request to get users, usersId = {}, from = {}, size = {}.", ids, from, size);
        return userService.getUsers(ids, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto createUser(@NotNull @Valid @RequestBody UserRequestDto userDto) {
        log.info("Request to create user {}.", userDto);
        return userService.createUser(userDto);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@Positive @PathVariable Long userId) {
        log.info("Request to delete user with id = {}.", userId);
        userService.deleteUser(userId);
    }

}
