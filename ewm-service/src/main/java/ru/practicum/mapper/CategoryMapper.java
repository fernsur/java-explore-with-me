package ru.practicum.mapper;

import ru.practicum.dto.category.CategoryDto;
import ru.practicum.model.Category;

public class CategoryMapper {
    public static Category toEntity(CategoryDto dto) {
        return new Category(dto.getId(),
                            dto.getName());

    }

    public static CategoryDto toDto(Category cat) {
        return new CategoryDto(cat.getId(),
                               cat.getName());

    }
}
