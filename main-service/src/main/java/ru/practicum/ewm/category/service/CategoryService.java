package ru.practicum.ewm.category.service;

import ru.practicum.ewm.category.dto.CategoryRequestDto;
import ru.practicum.ewm.category.dto.CategoryResponseDto;

import java.util.List;

public interface CategoryService {

    CategoryResponseDto createCategory(CategoryRequestDto categoryDto);

    CategoryResponseDto updateCategory(Long catId, CategoryRequestDto categoryDto);

    void deleteCategory(Long catId);

    List<CategoryResponseDto> getCategories(Integer from, Integer size);

    CategoryResponseDto getCategory(Long catId);

}
