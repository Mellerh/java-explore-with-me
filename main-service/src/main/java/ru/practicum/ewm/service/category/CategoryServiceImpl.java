package ru.practicum.ewm.service.category;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.category.CategoryDto;
import ru.practicum.ewm.dto.category.NewCategoryDto;
import ru.practicum.ewm.exception.exceptions.BadRequestException;
import ru.practicum.ewm.exception.exceptions.NotFoundException;
import ru.practicum.ewm.mapper.CategoryMapper;
import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.repostirory.CategoryRepository;
import ru.practicum.ewm.repostirory.EventRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final EventRepository eventRepository;

    // !!ADMIN
    @Override
    public CategoryDto addCategory(NewCategoryDto newCategoryDto) {
        if (categoryRepository.existByName(newCategoryDto.getName())) {
            throw new BadRequestException("Эта категория уже существует " + newCategoryDto.getName());
        }
        Category categoryToSave = categoryMapper.toCategory(newCategoryDto);
        categoryRepository.save(categoryToSave);
        return categoryMapper.toCategoryDto(categoryToSave);
    }

    @Override
    public void deleteCategory(Long categoryId) {
        if (eventRepository.existsByCategoryId(categoryId)) {
            throw new BadRequestException("Не пустая категория");
        }
        categoryRepository.deleteById(categoryId);
    }

    @Override
    public CategoryDto updateCategory(Long categoryId, NewCategoryDto categoryDto) {
        if (categoryDto.getName() == null || categoryDto.getName().isEmpty()) {
            throw new RuntimeException("Категория не может быть пустой");
        }
        Category savedCategory = categoryRepository.findById(categoryId).orElseThrow(() ->
                new NotFoundException("Категория не найдена " + categoryId));

        if (!savedCategory.getName().equals(categoryDto.getName()) && categoryRepository.existByName(categoryDto.getName())) {
            throw new BadRequestException("Эта категория уже существует " + categoryDto.getName());
        } else {
            savedCategory.setName(categoryDto.getName());
            return categoryMapper.toCategoryDto(categoryRepository.save(savedCategory));
        }
    }

    // !!PUBLIC
    @Override
    public List<CategoryDto> getCategoryList(Pageable pageable) {
        return categoryMapper.toCategoryDtoList(categoryRepository.findAll(pageable).toList());
    }

    @Override
    public CategoryDto getCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Категория с id " + categoryId + " не найдена"));
        return categoryMapper.toCategoryDto(category);
    }

}
