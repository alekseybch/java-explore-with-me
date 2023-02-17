package ru.practicum.ewm.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.CommentParametersDto;
import ru.practicum.ewm.comment.service.CommentService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
@Validated
@Slf4j
public class PublicCommentController {

    private final CommentService commentService;

    @GetMapping("/{eventId}")
    public List<CommentDto> getComments(@Positive @PathVariable Long eventId,
                                        @RequestParam(required = false) Long user,
                                        @RequestParam(required = false) String text,
                                        @RequestParam(required = false) String sort,
                                        @PositiveOrZero @RequestParam(value = "from",
                                                defaultValue = "0") Integer from,
                                        @Positive @RequestParam(value = "size",
                                                defaultValue = "10") Integer size) {
        var paramDto = new CommentParametersDto(eventId, user, text, sort);
        log.info("Request to get comments with parameters = {}, from = {}, size = {}.", paramDto, from, size);
        return commentService.getComments(paramDto, from, size);
    }

    @GetMapping
    public CommentDto getComment(@Positive @RequestParam Long commentId) {
        log.info("Request to get comment with id = {}.", commentId);
        return commentService.getComment(commentId);
    }

}
