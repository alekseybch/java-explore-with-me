package ru.practicum.ewm.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.service.CategoryService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Validated
@Slf4j
public class PublicCategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryDto> getCategories(@PositiveOrZero @RequestParam(value = "from",
                                                  defaultValue = "0") Integer from,
                                           @Positive @RequestParam(value = "size",
                                                  defaultValue = "10") Integer size) {
        log.info("Request to get categories, from = {}, size = {}.", from, size);
        return categoryService.getCategories(from, size);
    }

    @GetMapping("/{catId}")
    public CategoryDto getCategory(@Positive @PathVariable Long catId) {
        log.info("Request to get category with id = {}.", catId);
        return categoryService.getCategory(catId);
    }

}
