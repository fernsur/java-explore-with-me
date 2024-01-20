package ru.practicum.controller.publicApi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import ru.practicum.dto.category.CategoryDto;
import ru.practicum.service.category.CategoryService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/categories")
@Validated
public class PublicCategoryController {
    private final CategoryService service;

    @Autowired
    public PublicCategoryController(CategoryService service) {
        this.service = service;
    }

    @GetMapping()
    public ResponseEntity<List<CategoryDto>> allCategories(@PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                           @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("Получен GET-запрос к эндпоинту /categories на получение всех категорий.");
        return new ResponseEntity<>(service.allCategories(from, size), HttpStatus.OK);
    }

    @GetMapping("/{catId}")
    public ResponseEntity<CategoryDto> categoryById(@Positive @PathVariable long catId) {
        log.info("Получен GET-запрос к эндпоинту /categories/{catId} на получение категории по id.");
        return new ResponseEntity<>(service.categoryById(catId), HttpStatus.OK);
    }
}
