package ru.practicum.controller.adminApi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import ru.practicum.service.category.CategoryService;
import ru.practicum.dto.CategoryDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Slf4j
@RestController
@RequestMapping(path = "/admin/categories")
@Validated
public class AdminCategoryController {
    private final CategoryService service;

    @Autowired
    public AdminCategoryController(CategoryService service) {
        this.service = service;
    }

    @PostMapping()
    public ResponseEntity<CategoryDto> createCategory(@RequestBody @Valid CategoryDto dto) {
        log.info("Получен POST-запрос к эндпоинту /admin/categories на добавление категории.");
        return new ResponseEntity<>(service.createCategory(dto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{catId}")
    public ResponseEntity<Void> deleteCategory(@Positive @PathVariable long catId) {
        log.info("Получен DELETE-запрос к эндпоинту /admin/categories/{catId} на удаление категории.");
        service.deleteCategory(catId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{catId}")
    public ResponseEntity<CategoryDto> updateCategory(@RequestBody @Valid CategoryDto dto,
                                                      @Positive @PathVariable long catId) {
        log.info("Получен PATCH-запрос к эндпоинту /admin/categories/{catId} на обновление категории.");
        return new ResponseEntity<>(service.updateCategory(dto, catId), HttpStatus.OK);
    }
}
