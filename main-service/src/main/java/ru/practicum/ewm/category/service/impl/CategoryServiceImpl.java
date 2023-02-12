package ru.practicum.ewm.category.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.category.bd.repository.CategoryRepository;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.NewCategoryDto;
import ru.practicum.ewm.category.mapper.CategoryMapper;
import ru.practicum.ewm.category.service.CategoryService;
import ru.practicum.ewm.global.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.ewm.global.utility.PageableConverter.getPageable;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public CategoryDto createCategory(NewCategoryDto categoryDto) {
        var category = categoryRepository.save(categoryMapper.toCategory(categoryDto));
        log.info("Category with id = {} is saved {}.", category.getId(), category);
        return categoryMapper.toResponseDto(category);
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(Long catId, NewCategoryDto categoryDto) {
        var category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException(String.format("Category with id = %d not found", catId)));
        category.setName(categoryDto.getName());
        var updatedCategory = categoryRepository.save(category);
        log.info("Category with id = {} is changed {}.", updatedCategory.getId(), updatedCategory);
        return categoryMapper.toResponseDto(updatedCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(Long catId) {
        categoryRepository.deleteById(catId);
        log.info("Category with id = {} is deleted.", catId);
    }

    @Override
    public List<CategoryDto> getCategories(Integer from, Integer size) {
        return categoryRepository.getAllCategories(getPageable(from, size)).stream()
                .map(categoryMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategory(Long catId) {
        var category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException(String.format("Category with id = %d not found", catId)));
        return categoryMapper.toResponseDto(category);
    }

}
