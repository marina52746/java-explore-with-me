package ru.practicum.eventRequest.service;

import org.springframework.http.ResponseEntity;

public interface ParticipationRequestService {
    ResponseEntity<Object> createRequest(Long userId, Long eventId);

    ResponseEntity<Object> cancelRequest(Long userId, Long requestId);

    ResponseEntity<Object> getUserRequests(Long userId);
}
