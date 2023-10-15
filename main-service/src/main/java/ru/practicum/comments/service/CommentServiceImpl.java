package ru.practicum.comments.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.comments.dto.InputCommentDto;
import ru.practicum.comments.mapper.CommentMapper;
import ru.practicum.comments.model.Comment;
import ru.practicum.comments.repository.CommentRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidationException;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    public ResponseEntity<Object> getCommentById(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment not found", "Comment not found, id=" + commentId));
        return new ResponseEntity<>(CommentMapper.fromCommentToOutputCommentDto(comment), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> getComments(Long eventId, Pageable pageable) {
        List<Comment> comments = commentRepository.findByEventId(eventId, pageable).getContent();
        return new ResponseEntity<>(comments.stream().map(CommentMapper::fromCommentToOutputCommentDto)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> createComment(InputCommentDto newComment, Long userId, Long eventId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found",
                "User not found, id=" + userId));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found",
                "Event not found, id=" + eventId));
        Comment comment = CommentMapper.fromInputCommentDtoToComment(newComment, user, event);
        comment = commentRepository.save(comment);
        return new ResponseEntity<>(CommentMapper.fromCommentToOutputCommentDto(comment), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Object> updateComment(InputCommentDto updatedComment, Long userId,
                                                Long eventId, Long commentId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found",
                "User not found, id=" + userId));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found",
                "Event not found, id=" + eventId));
        Comment comment = commentRepository.findById(commentId).orElseThrow(()
                -> new NotFoundException("Comment not found", "Comment not found, id=" + commentId));
        if (!comment.getUser().getId().equals(userId))
            throw new ValidationException("Comment not user's",
                    "Comment with id=" + commentId + " can't be updated by user with id=" + userId + " he isn't author");
        if (!comment.getEvent().getId().equals(eventId))
            throw new ValidationException("Comment doesn't occur to event",
                    "Comment with id=" + commentId + " doesn't occur to event with id=" + eventId);
        comment.setText(updatedComment.getText());
        commentRepository.save(comment);
        return new ResponseEntity<>(CommentMapper.fromCommentToOutputCommentDto(comment), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> deleteComment(Long id, Long userId, Long eventId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found",
                "User not found, id=" + userId));
        eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found",
                "Event not found, id=" + eventId));
        Comment comment = commentRepository.findById(id).orElseThrow(()
                -> new NotFoundException("Comment not found", "Comment not found, id=" + id));
        if (comment.getEvent().getId().equals(eventId) && comment.getUser().getId().equals(userId))
            commentRepository.deleteById(id);
        else throw new ValidationException("Comment can't be deleted", "Comment can't be deleted");
        return new ResponseEntity<>("", HttpStatus.NO_CONTENT);
    }
}
