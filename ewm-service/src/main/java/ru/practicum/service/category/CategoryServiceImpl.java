package ru.practicum.service.category;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import ru.practicum.dto.CategoryDto;
import ru.practicum.exception.AlreadyExistsException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.CategoryMapper;
import ru.practicum.model.Category;
import ru.practicum.repository.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public CategoryDto createCategory(CategoryDto dto) {
        try {
            Category category = CategoryMapper.toEntity(dto);
            return CategoryMapper.toDto(repository.save(category));
        } catch (DataIntegrityViolationException e) {
            String warning = "Категория " + dto.getName() + " уже существует";
            log.warn(warning);
            throw new AlreadyExistsException(warning);
        }
    }

    @Override
    public CategoryDto updateCategory(CategoryDto dto, long catId) {
        Category category = CategoryMapper.toEntity(categoryById(catId));

        if (dto.getName() != null) {
            String name = dto.getName();
            category.setName(name);
        }

        return CategoryMapper.toDto(repository.save(category));
    }

    @Override
    public void deleteCategory(long catId) {
        CategoryDto category = categoryById(catId);
        repository.deleteById(catId);
    }

    @Override
    public List<CategoryDto> allCategories(int from, int size) {
        Page<Category> categories;
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size, sort);
        categories = repository.findAll(page);

        return categories.stream()
                .map(CategoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto categoryById(long catId) {
        Category category = repository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Невозможно найти. Такой категории нет."));
        return CategoryMapper.toDto(category);
    }
}
