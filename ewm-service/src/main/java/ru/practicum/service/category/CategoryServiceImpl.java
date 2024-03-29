package ru.practicum.service.category;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.exception.AlreadyExistsException;
import ru.practicum.exception.ConflictDataException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.CategoryMapper;
import ru.practicum.model.Category;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    private final EventRepository eventRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, EventRepository eventRepository) {
        this.categoryRepository = categoryRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public CategoryDto createCategory(NewCategoryDto dto) {
        try {
            Category category = CategoryMapper.toEntity(dto);
            return CategoryMapper.toDto(categoryRepository.save(category));
        } catch (DataIntegrityViolationException e) {
            String warning = "Категория " + dto.getName() + " уже существует";
            log.warn(warning);
            throw new AlreadyExistsException(warning);
        }
    }

    @Override
    public CategoryDto updateCategory(CategoryDto dto) {
        Category category;

        try {
            category = CategoryMapper.toEntity(getCategoryById(dto.getId()));
            if (dto.getName() != null) {
                String name = dto.getName();
                category.setName(name);
            }
            category = categoryRepository.save(category);
        } catch (DataIntegrityViolationException e) {
            String warning = "Категория " + dto.getName() + " уже существует";
            log.warn(warning);
            throw new AlreadyExistsException(warning);
        }

        return CategoryMapper.toDto(category);
    }

    @Override
    public void deleteCategory(long catId) {
        Category category = findCategoryById(catId);
        if (eventRepository.existsByCategory(category)) {
            throw new ConflictDataException("Нельзя удалить категорию. Есть связанные события.");
        }
        categoryRepository.deleteById(catId);
    }

    @Override
    public List<CategoryDto> getAllCategories(int from, int size) {
        Page<Category> categories;
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size, sort);
        categories = categoryRepository.findAll(page);

        return categories.stream()
                .map(CategoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoryById(long catId) {
        Category category = findCategoryById(catId);
        return CategoryMapper.toDto(category);
    }

    public Category findCategoryById(long catId) {
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Невозможно найти. Такой категории нет."));
    }
}
