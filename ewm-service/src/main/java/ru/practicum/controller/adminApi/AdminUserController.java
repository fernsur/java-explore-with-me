package ru.practicum.controller.adminApi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import ru.practicum.dto.user.UserDto;
import ru.practicum.service.user.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/admin/users")
@Validated
public class AdminUserController {
    private final UserService service;

    @Autowired
    public AdminUserController(UserService service) {
        this.service = service;
    }

    @GetMapping()
    public ResponseEntity<List<UserDto>> getAllUsers(@RequestParam(required = false) List<Long> ids,
                                                     @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                     @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("Получен GET-запрос к эндпоинту /admin/users на получение списка пользователей.");
        return new ResponseEntity<>(service.getAllUsers(ids, from, size), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<UserDto> createUser(@RequestBody @Valid UserDto dto) {
        log.info("Получен POST-запрос к эндпоинту /admin/users на добавление пользователя.");
        return new ResponseEntity<>(service.createUser(dto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@Positive @PathVariable long userId) {
        log.info("Получен DELETE-запрос к эндпоинту /admin/users/{userId} на удаление пользователя.");
        service.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
