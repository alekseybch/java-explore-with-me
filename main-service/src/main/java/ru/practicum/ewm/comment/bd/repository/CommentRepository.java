package ru.practicum.ewm.comment.bd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.comment.bd.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
