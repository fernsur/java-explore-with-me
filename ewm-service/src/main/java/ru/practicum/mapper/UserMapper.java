package ru.practicum.mapper;

import ru.practicum.dto.user.UserDto;
import ru.practicum.dto.user.UserShortDto;
import ru.practicum.model.User;

public class UserMapper {
    public static User toEntity(UserDto userDto) {
        return new User(userDto.getId(),
                        userDto.getName(),
                        userDto.getEmail());
    }

    public static UserDto toDto(User user) {
        return new UserDto(user.getId(),
                           user.getName(),
                           user.getEmail());
    }

    public static UserShortDto toShortDto(User user) {
        return new UserShortDto(user.getId(),
                                user.getName());
    }
}
