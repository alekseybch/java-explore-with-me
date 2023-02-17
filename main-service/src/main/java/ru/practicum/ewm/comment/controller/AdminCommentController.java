package ru.practicum.ewm.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.UpdateAdminCommentDto;
import ru.practicum.ewm.comment.service.CommentService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/admin/comments")
@RequiredArgsConstructor
@Validated
@Slf4j
public class AdminCommentController {

    private final CommentService commentService;

    @PatchMapping("/{commentId}")
    public CommentDto updateCommentByAdmin(@Positive @PathVariable Long commentId,
                                           @NotNull @Valid @RequestBody UpdateAdminCommentDto commentDto) {
        log.info("[ADMIN] Request to update comment with id = {}, , comment = {}.", commentId, commentDto);
        return commentService.updateCommentByAdmin(commentId, commentDto);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentByAdmin(@Positive @PathVariable Long commentId) {
        log.info("[ADMIN] Request to delete comment with id = {}.", commentId);
        commentService.deleteCommentByAdmin(commentId);
    }

}
