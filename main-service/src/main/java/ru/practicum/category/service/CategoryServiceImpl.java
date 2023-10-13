package ru.practicum.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.IntegrityViolationException;
import ru.practicum.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    public ResponseEntity<Object> updateCategory(Long catId, NewCategoryDto newCategory) {
        if (!(categoryRepository.existsById(catId)))
            throw new NotFoundException("The required object was not found",
                    "Category with id=" + catId + " was not found");
        var category = categoryRepository.findById(catId);
        if (category.get().getName().equals(newCategory.getName()))
            return new ResponseEntity<>(CategoryMapper.fromCategoryToCategoryDto(category.get()), HttpStatus.OK);
        if (categoryRepository.findAllByName(newCategory.getName()).size() != 0)
            throw new IntegrityViolationException("Integrity constraint has been violated", "Category name not unique");
        return new ResponseEntity<>(CategoryMapper.fromCategoryToCategoryDto(
                categoryRepository.save(new Category(catId, newCategory.getName()))), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> createCategory(NewCategoryDto newCategory) {
        if (categoryRepository.findAllByName(newCategory.getName()).size() != 0)
            throw new IntegrityViolationException("Integrity constraint has been violated", "Category name not unique");
        return new ResponseEntity<>(CategoryMapper.fromCategoryToCategoryDto(
                categoryRepository.save(CategoryMapper.fromNewCategoryDtoToCategory(newCategory))), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Object> deleteCategory(Long catId) {
        if (categoryRepository.existsById(catId)) {
            if (eventRepository.findAllByCategoryId(catId).size() > 0)
                throw new IntegrityViolationException("Category can't be deleted",
                        "There are some events in category with id=" + catId);
            categoryRepository.deleteById(catId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        throw new NotFoundException("The required object was not found",
                    "Category with id=" + catId + " was not found");
    }

    @Override
    public List<CategoryDto> getCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable).getContent().stream()
                .map(CategoryMapper::fromCategoryToCategoryDto).collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<Object> getCategoryById(Long catId) {
        if (categoryRepository.existsById(catId))
            return new ResponseEntity<>(categoryRepository.findById(catId), HttpStatus.OK);
        throw new NotFoundException("The required object was not found",
                "Category with id=" + catId + " was not found");
    }
}
