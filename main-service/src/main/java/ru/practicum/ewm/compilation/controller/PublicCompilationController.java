package ru.practicum.ewm.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.service.CompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
@Validated
@Slf4j
public class PublicCompilationController {

    private final CompilationService compilationService;

    @GetMapping
    public List<CompilationDto> getCompilations(@RequestParam(defaultValue = "false") Boolean pinned,
                                                @PositiveOrZero @RequestParam(value = "from",
                                                        defaultValue = "0") Integer from,
                                                @Positive @RequestParam(value = "size",
                                                        defaultValue = "10") Integer size) {
        log.info("Request to get compilations, pinned = {}, from = {}, size = {}.", pinned, from, size);
        return compilationService.findCompilations(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationDto getCompilation(@Positive @PathVariable Long compId) {
        log.info("Request to get compilation with id = {}.", compId);
        return compilationService.getCompilation(compId);
    }

}
