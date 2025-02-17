package ru.practicum.ewm.controller.pub;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.category.CategoryDto;
import ru.practicum.ewm.service.category.CategoryService;

import java.util.List;

@RestController
@RequestMapping(path = "/categories")
@RequiredArgsConstructor
@Validated
public class PubCategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryDto> getCategoryList(@RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
                                             @RequestParam(required = false, defaultValue = "10") @Positive Integer size) {
        return categoryService.getCategoryList(PageRequest.of(from, size));
    }

    @GetMapping("/{catId}")
    public CategoryDto getCategory(@PathVariable Long catId) {
        return categoryService.getCategory(catId);
    }

}
