package ru.practicum.comments.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.service.CommentService;
import ru.practicum.pagination.FromSizeRequest;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/events/{eventId}/comments")
public class PublicCommentController {
    private final CommentService commentService;

    @GetMapping(path = "/{commentId}")
    public ResponseEntity<Object> getCommentById(@PathVariable Long eventId, @PathVariable Long commentId) {
        return commentService.getCommentById(commentId);
    }

    @GetMapping
    public ResponseEntity<Object> getComments(@PathVariable Long eventId,
                                              @RequestParam(required = false, defaultValue = "0") Integer from,
                                              @RequestParam(required = false, defaultValue = "10") Integer size) {
        final PageRequest pageRequest = FromSizeRequest.of(from, size, Sort.by(DESC, "createdOn"));
        return commentService.getComments(eventId, pageRequest);
    }
}
