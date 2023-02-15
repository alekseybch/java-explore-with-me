package ru.practicum.ewm.comment.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.comment.bd.model.Comment;
import ru.practicum.ewm.comment.bd.repository.CommentRepository;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.CommentParametersDto;
import ru.practicum.ewm.comment.dto.NewCommentDto;
import ru.practicum.ewm.comment.dto.UpdateAdminCommentDto;
import ru.practicum.ewm.comment.mapper.CommentMapper;
import ru.practicum.ewm.comment.service.CommentService;
import ru.practicum.ewm.event.bd.model.enums.EventStatus;
import ru.practicum.ewm.event.bd.repository.EventRepository;
import ru.practicum.ewm.global.exception.BadRequestException;
import ru.practicum.ewm.global.exception.ConflictException;
import ru.practicum.ewm.global.exception.NotFoundException;
import ru.practicum.ewm.user.bd.repository.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.ewm.global.utility.PageableConverter.getPageable;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;
    private final EntityManager entityManager;

    @Override
    public List<CommentDto> getComments(CommentParametersDto paramDto, Integer from, Integer size) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Comment> criteriaQuery = cb.createQuery(Comment.class);
        Root<Comment> commentRoot = criteriaQuery.from(Comment.class);
        criteriaQuery.select(commentRoot);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(commentRoot.get("event").get("id"), paramDto.getEventId()));

        if (paramDto.getText() != null) {
            predicates.add(cb.like(cb.lower(commentRoot.get("text").as(String.class)),
                            cb.literal('%' + paramDto.getText().toLowerCase() + '%')));
        }

        if (paramDto.getUser() != null) {
            predicates.add(cb.equal(commentRoot.get("user").get("id"), paramDto.getUser()));
        }

        criteriaQuery.where(predicates.toArray(new Predicate[0]));

        if (paramDto.getSort() != null) {
            switch (paramDto.getSort().toLowerCase()) {
                case "asc":
                    criteriaQuery.orderBy(cb.asc(commentRoot.get("created")));
                    break;
                case "desc":
                    criteriaQuery.orderBy(cb.desc(commentRoot.get("created")));
            }
        }

        TypedQuery<Comment> typedQuery = entityManager.createQuery(criteriaQuery);
        PageRequest pageable = getPageable(from, size);
        typedQuery.setFirstResult(pageable.getPageNumber());
        typedQuery.setMaxResults(pageable.getPageSize());
        List<Comment> comments = typedQuery.getResultList();

        return comments.stream()
                .map(commentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto getComment(Long commentId) {
        return commentMapper.toCommentDto(commentRepository.getReferenceById(commentId));
    }

    @Override
    @Transactional
    public CommentDto createComment(Long userId, Long eventId, NewCommentDto commentDto) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id = %d not found", userId)));
        var event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id = %d not found", eventId)));
        if (!event.getStatus().equals(EventStatus.PUBLISHED))
            throw new ConflictException(String.format("Event with id = %d not published", eventId));
        var comment = commentMapper.toComment(commentDto);
        comment.setUser(user);
        comment.setEvent(event);
        comment.setCreated(LocalDateTime.now().withNano(0));
        var savedComment = commentRepository.save(comment);
        log.info("Comment with id = {} is saved {}.", savedComment.getId(), savedComment);
        return commentMapper.toCommentDto(savedComment);
    }

    @Override
    @Transactional
    public CommentDto updateComment(Long userId, Long commentId, NewCommentDto commentDto) {
        var comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(String.format("Comment with id = %d not found", commentId)));
        if (!comment.getUser().getId().equals(userId))
            throw new BadRequestException(String.format("User with id = %d not initiator of comment with id = %d", userId, commentId));
        if (commentDto.getText() != null) {
            comment.setText(commentDto.getText());
            comment.setEdited(LocalDateTime.now().withNano(0));
        } else {
            throw new ConflictException("To edit a comment, the request must contain the text");
        }
        var savedComment = commentRepository.save(comment);
        log.info("Comment with id = {} is changed {}.", savedComment.getId(), savedComment);
        return commentMapper.toCommentDto(savedComment);
    }

    @Override
    @Transactional
    public void deleteComment(Long userId, Long commentId) {
        var comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(String.format("Comment with id = %d not found", commentId)));
        if (!comment.getUser().getId().equals(userId))
            throw new BadRequestException(String.format("User with id = %d not initiator of comment with id = %d", userId, commentId));
        commentRepository.deleteById(commentId);
        log.info("Comment with id = {} is deleted.", commentId);
    }

    @Override
    @Transactional
    public CommentDto updateCommentByAdmin(Long commentId, UpdateAdminCommentDto commentDto) {
        var comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(String.format("Comment with id = %d not found", commentId)));
        comment.setText(commentDto.getText());
        comment.setEdited(LocalDateTime.now().withNano(0));
        var savedComment = commentRepository.save(comment);
        log.info("[ADMIN] Comment with id = {} is changed {}.", savedComment.getId(), savedComment);
        return commentMapper.toCommentDto(savedComment);
    }

    @Override
    @Transactional
    public void deleteCommentByAdmin(Long commentId) {
        commentRepository.deleteById(commentId);
        log.info("[ADMIN] Comment with id = {} is deleted.", commentId);
    }

}
