package ru.practicum.ewm.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.NewCommentDto;
import ru.practicum.ewm.comment.service.CommentService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/users/{userId}/comments")
@RequiredArgsConstructor
@Validated
@Slf4j
public class PrivateCommentController {

    private final CommentService commentService;

    @PostMapping("/{eventId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createComment(@Positive @PathVariable Long userId,
                                    @Positive @PathVariable Long eventId,
                                    @NotNull @Valid @RequestBody NewCommentDto commentDto) {
        log.info("Request to create comment on an event with id = {} from user with id = {}, comment = {}.",
                eventId, userId, commentDto);
        return commentService.createComment(userId, eventId, commentDto);
    }

    @PatchMapping("/{commentId}")
    public CommentDto updateComment(@Positive @PathVariable Long userId,
                                    @Positive @PathVariable Long commentId,
                                    @NotNull @Valid @RequestBody NewCommentDto commentDto) {
        log.info("Request to update comment with id = {} from user with id = {}, comment = {}.",
                commentId, userId, commentDto);
        return commentService.updateComment(userId, commentId, commentDto);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@Positive @PathVariable Long userId,
                              @Positive @PathVariable Long commentId) {
        log.info("Request to delete comment with id = {} from user with id = {}.", commentId, userId);
        commentService.deleteComment(userId, commentId);
    }

}
