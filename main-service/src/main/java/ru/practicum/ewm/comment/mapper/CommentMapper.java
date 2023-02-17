package ru.practicum.ewm.comment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.comment.bd.model.Comment;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.NewCommentDto;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    Comment toComment(NewCommentDto dto);

    @Mapping(target = "eventId", source = "comment.event.id")
    @Mapping(target = "authorName", source = "comment.user.name")
    CommentDto toCommentDto(Comment comment);

}
