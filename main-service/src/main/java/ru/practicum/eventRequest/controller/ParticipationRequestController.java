package ru.practicum.eventRequest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.eventRequest.service.ParticipationRequestService;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/requests")
public class ParticipationRequestController {
    private final ParticipationRequestService participationRequestService;

    @PostMapping
    public ResponseEntity<Object> createRequest(@PathVariable Long userId,
                                                @RequestParam Long eventId) {
        return participationRequestService.createRequest(userId, eventId);
    }

    @PatchMapping(path = "/{requestId}/cancel")
    public ResponseEntity<Object> cancelRequest(@PathVariable Long userId,
                                                @PathVariable Long requestId) {
        return participationRequestService.cancelRequest(userId, requestId);
    }

    @GetMapping
    public ResponseEntity<Object> getUserRequests(@PathVariable Long userId) {
        return participationRequestService.getUserRequests(userId);
    }
}
