package ru.practicum.ewm.service.category;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.dto.category.CategoryDto;
import ru.practicum.ewm.dto.category.NewCategoryDto;

import java.util.List;

public interface CategoryService {

    // admin
    // добавление новой категории
    CategoryDto addCategory(NewCategoryDto newCategoryDto);

    // удаление категории
    void deleteCategory(Long categoryId);

    // изменение категории
    CategoryDto updateCategory(Long categoryId, NewCategoryDto newCategoryDto);

    // public
    // получение категорий
    List<CategoryDto> getCategoryList(Pageable pageable);

    // получение инфо о категории по ее id
    CategoryDto getCategory(Long categoryId);
}
