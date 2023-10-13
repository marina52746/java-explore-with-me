package ru.practicum.eventRequest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.enums.EventState;
import ru.practicum.eventRequest.enums.ParticipationStatus;
import ru.practicum.eventRequest.mapper.ParticipationRequestMapper;
import ru.practicum.eventRequest.model.ParticipationRequest;
import ru.practicum.eventRequest.repository.ParticipationRequestRepository;
import ru.practicum.exception.IntegrityViolationException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParticipationRequestServiceImpl implements ParticipationRequestService {
    private final ParticipationRequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public ResponseEntity<Object> createRequest(Long userId, Long eventId) {
        userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User not found", "User not found, id=" + userId));
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Event not found", "Event not found, id=" + eventId));
        if (requestRepository.findFirstByRequesterAndEvent(userId, eventId) != null)
            throw new IntegrityViolationException("Request already exists",
                    "Request already exists: userId=" + userId + " eventId=" + eventId);
        if (event.getUser().getId() == userId)
            throw new IntegrityViolationException("User can't send request to his own event",
                    "User with id=" + userId + " can't send request to his own event with id=" + eventId);
        if (event.getState() != EventState.PUBLISHED)
            throw new IntegrityViolationException("Event isn't published",
                    "User can't participate in not published event");
        if (event.getParticipantLimit() != null && event.getParticipantLimit() !=0
                && event.getParticipantLimit() == event.getConfirmedRequests())
            throw new IntegrityViolationException("Reached limit of participants",
                    "Reached limit of participants" + event.getParticipantLimit());
        ParticipationRequest request = new ParticipationRequest(
                LocalDateTime.now(),
                eventId,
                0L,
                userId,
                event.getRequestModeration() && (event.getParticipantLimit() != null && event.getParticipantLimit() !=0)
                        ? ParticipationStatus.PENDING : ParticipationStatus.CONFIRMED
        );
        if (request.getStatus() == ParticipationStatus.CONFIRMED)
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        return new ResponseEntity<>(ParticipationRequestMapper.fromRequestToRequestDto(requestRepository.save(request)),
                HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Object> cancelRequest(Long userId, Long requestId) {
        userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User not found", "User not found, id=" + userId));
        ParticipationRequest request = requestRepository.findById(requestId).orElseThrow(
                () -> new NotFoundException("Request not found", "Request not found, id=" + requestId));
        request.setStatus(ParticipationStatus.CANCELED);
        return new ResponseEntity<>(ParticipationRequestMapper.fromRequestToRequestDto(requestRepository.save(request)),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> getUserRequests(Long userId) {
        userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User not found", "User not found, id=" + userId));
        List<ParticipationRequest> requests = requestRepository.findAllByRequester(userId);
        return new ResponseEntity<>(requests.stream().map(ParticipationRequestMapper::fromRequestToRequestDto)
                .collect(Collectors.toList()), HttpStatus.OK);
    }
}
