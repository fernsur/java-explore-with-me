package ru.practicum.service.category;

import ru.practicum.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto createCategory(CategoryDto dto);

    CategoryDto updateCategory(CategoryDto dto, long catId);

    void deleteCategory(long catId);

    List<CategoryDto> allCategories(int from, int size);

    CategoryDto categoryById(long catId);
}
