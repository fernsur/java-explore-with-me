package ru.practicum.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import ru.practicum.dto.user.UserDto;
import ru.practicum.mapper.UserMapper;
import ru.practicum.model.User;
import ru.practicum.repository.UserRepository;
import ru.practicum.exception.AlreadyExistsException;
import ru.practicum.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Autowired
    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<UserDto> getAllUsers(List<Long> ids, int from, int size) {
        Page<User> users;
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size, sort);

        if (ids == null) {
            users = repository.findAll(page);
        } else {
            users = repository.findAllByIdIn(ids, page);
        }

        return users.stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto createUser(UserDto dto) {
        try {
            User user = UserMapper.toEntity(dto);
            return UserMapper.toDto(repository.save(user));
        } catch (DataIntegrityViolationException e) {
            String warning = "Пользователь с email = " + dto.getEmail() + " уже существует";
            log.warn(warning);
            throw new AlreadyExistsException(warning);
        }
    }

    @Override
    public void deleteUser(long userId) {
        User user = findUserById(userId);
        repository.deleteById(userId);
    }

    private User findUserById(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Невозможно найти. Такого пользователя нет."));
    }
}
