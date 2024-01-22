package ru.practicum.mapper;

import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.model.Category;

public class CategoryMapper {
    public static Category toEntity(CategoryDto dto) {
        return new Category(dto.getId(),
                            dto.getName());

    }

    public static Category toEntity(NewCategoryDto dto) {
        return Category.builder()
                .name(dto.getName())
                .build();

    }

    public static CategoryDto toDto(Category cat) {
        return new CategoryDto(cat.getId(),
                               cat.getName());

    }
}
