package ru.practicum.category.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.service.CategoryService;
import ru.practicum.pagination.FromSizeRequest;

import java.util.List;

import static org.springframework.data.domain.Sort.Direction.ASC;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/categories")
public class PublicCategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryDto> getCategories(@RequestParam(required = false, defaultValue = "0") Integer from,
                                           @RequestParam(required = false, defaultValue = "10") Integer size) {
        final PageRequest pageRequest = FromSizeRequest.of(from, size, Sort.by(ASC, "id"));
        return categoryService.getCategories(pageRequest);
    }

    @GetMapping("/{catId}")
    public ResponseEntity<Object> getCategoryById(@PathVariable Long catId) {
        return categoryService.getCategoryById(catId);
    }
}
