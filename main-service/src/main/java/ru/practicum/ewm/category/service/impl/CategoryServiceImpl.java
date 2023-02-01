package ru.practicum.ewm.category.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.category.db.repository.CategoryRepository;
import ru.practicum.ewm.category.dto.CategoryRequestDto;
import ru.practicum.ewm.category.dto.CategoryResponseDto;
import ru.practicum.ewm.category.mapper.CategoryMapper;
import ru.practicum.ewm.category.service.CategoryService;

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
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public CategoryResponseDto createCategory(CategoryRequestDto categoryDto) {
        var category = categoryRepository.save(categoryMapper.toCategory(categoryDto));
        log.info("Category with id = {} is saved {}.", category.getId(), category);
        return categoryMapper.toResponseDto(category);
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public CategoryResponseDto updateCategory(Long catId, CategoryRequestDto categoryDto) {
        var category = categoryRepository.getReferenceById(catId);
        category.setName(categoryDto.getName());
        var updatedCategory = categoryRepository.save(category);
        log.info("Category with id = {} is changed {}.", updatedCategory.getId(), updatedCategory);
        return categoryMapper.toResponseDto(updatedCategory);
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void deleteCategory(Long catId) {
        categoryRepository.deleteById(catId);
        log.info("Category with id = {} is deleted.", catId);
    }

    @Override
    public List<CategoryResponseDto> getCategories(Integer from, Integer size) {
        return categoryRepository.getAllCategories(getPageable(from, size, Sort.Direction.ASC, "id")).stream()
                .map(categoryMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponseDto getCategory(Long catId) {
        return categoryMapper.toResponseDto(categoryRepository.getReferenceById(catId));
    }

}
