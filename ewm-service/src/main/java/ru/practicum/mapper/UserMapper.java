package ru.practicum.mapper;

import ru.practicum.dto.user.UserDto;
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
}
