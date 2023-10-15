package ru.practicum.comments.service;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import ru.practicum.comments.dto.InputCommentDto;

public interface CommentService {

    ResponseEntity<Object> getCommentById(Long eventId);

    ResponseEntity<Object> getComments(Long eventId, Pageable pageable);

    ResponseEntity<Object> createComment(InputCommentDto newComment, Long userId, Long eventId);

    ResponseEntity<Object> updateComment(InputCommentDto updatedComment, Long userId, Long eventId, Long commentId);

    ResponseEntity<Object> deleteComment(Long id, Long userId, Long eventId);
}
