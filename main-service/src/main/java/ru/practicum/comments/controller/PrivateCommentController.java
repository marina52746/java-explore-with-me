package ru.practicum.comments.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.dto.InputCommentDto;
import ru.practicum.comments.service.CommentService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/events/{eventId}/comments/user/{userId}")
public class PrivateCommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Object> createComment(@PathVariable Long eventId,
                                                @PathVariable Long userId,
                                                @Valid @RequestBody InputCommentDto inputCommentDto) {
        return commentService.createComment(inputCommentDto, userId, eventId);
    }

    @PatchMapping(path = "/comment/{commentId}")
    public ResponseEntity<Object> updateComment(@PathVariable Long eventId,
                                                @PathVariable Long userId,
                                                @PathVariable Long commentId,
                                                @Valid @RequestBody InputCommentDto inputCommentDto) {
        return commentService.updateComment(inputCommentDto, userId, eventId, commentId);
    }

    @DeleteMapping(path = "/comment/{commentId}")
    public ResponseEntity<Object> deleteComment(@PathVariable Long eventId,
                                                @PathVariable Long userId,
                                                @PathVariable Long commentId) {
        return commentService.deleteComment(commentId, userId, eventId);
    }
}
