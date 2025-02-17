package ru.practicum.ewm.service.category;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.category.CategoryDto;
import ru.practicum.ewm.dto.category.NewCategoryDto;

import java.util.List;

@Service
public interface CategoryService {

    CategoryDto addCategory(NewCategoryDto newCategoryDto);

    void deleteCategory(Long categoryId);

    CategoryDto updateCategory(Long categoryId, NewCategoryDto newCategoryDto);

    List<CategoryDto> getCategoryList(Pageable pageable);

    CategoryDto getCategory(Long categoryId);

}
