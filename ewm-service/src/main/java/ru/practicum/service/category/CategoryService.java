package ru.practicum.service.category;

import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.NewCategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto createCategory(NewCategoryDto dto);

    CategoryDto updateCategory(CategoryDto dto);

    void deleteCategory(long catId);

    List<CategoryDto> getAllCategories(int from, int size);

    CategoryDto getCategoryById(long catId);
}
