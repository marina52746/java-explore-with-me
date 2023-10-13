package ru.practicum.category.service;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {

    ResponseEntity<Object> updateCategory(Long catId, NewCategoryDto newCategory);

    ResponseEntity<Object> createCategory(NewCategoryDto newCategory);

    ResponseEntity<Object> deleteCategory(Long catId);

    List<CategoryDto> getCategories(Pageable pageable);

    ResponseEntity<Object> getCategoryById(Long catId);

}
