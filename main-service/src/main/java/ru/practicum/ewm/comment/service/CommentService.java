package ru.practicum.ewm.comment.service;

import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.CommentParametersDto;
import ru.practicum.ewm.comment.dto.NewCommentDto;
import ru.practicum.ewm.comment.dto.UpdateAdminCommentDto;

import java.util.List;

public interface CommentService {

    List<CommentDto> getComments(CommentParametersDto paramDto, Integer from, Integer size);

    CommentDto getComment(Long commentId);

    CommentDto createComment(Long userId, Long eventId, NewCommentDto commentDto);

    CommentDto updateComment(Long userId, Long commentId, NewCommentDto commentDto);

    void deleteComment(Long userId, Long commentId);

    CommentDto updateCommentByAdmin(Long commentId, UpdateAdminCommentDto commentDto);

    void deleteCommentByAdmin(Long commentId);

}
