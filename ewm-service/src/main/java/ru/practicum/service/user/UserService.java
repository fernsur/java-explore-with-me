package ru.practicum.service.user;

import ru.practicum.dto.user.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers(List<Long> ids, int from, int size);

    UserDto createUser(UserDto dto);

    void deleteUser(long userId);
}
