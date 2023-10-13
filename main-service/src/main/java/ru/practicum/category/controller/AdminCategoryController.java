package ru.practicum.category.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.service.CategoryService;
import ru.practicum.category.dto.NewCategoryDto;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/categories")
public class AdminCategoryController {
    private final CategoryService categoryService;

    @PatchMapping("/{catId}")
    public ResponseEntity<Object> updateCategory(@PathVariable Long catId,
                                  @Valid @RequestBody NewCategoryDto newCategory) {
        return categoryService.updateCategory(catId, newCategory);
    }

    @PostMapping
    public ResponseEntity<Object> createCategory(@Valid @RequestBody NewCategoryDto newCategory) {
        return categoryService.createCategory(newCategory);
    }

    @DeleteMapping("/{catId}")
    public ResponseEntity<Object> delete(@PathVariable Long catId) {
        return categoryService.deleteCategory(catId);
    }
}
