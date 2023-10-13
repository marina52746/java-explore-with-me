package ru.practicum.category.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.category.model.Category;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;

@Component
public class CategoryMapper {
    public static CategoryDto fromCategoryToCategoryDto(Category category) {
        if (category == null) return null;
        return new CategoryDto(
                category.getId(),
                category.getName()
        );
    }

    public static Category fromCategoryDtoToCategory(CategoryDto categoryDto) {
        if (categoryDto == null) return null;
        return new Category(
                categoryDto.getId(),
                categoryDto.getName()
        );
    }

    public static Category fromNewCategoryDtoToCategory(NewCategoryDto newCategory) {
        if (newCategory == null) return null;
        return new Category(
                newCategory.getName()
        );
    }
}
