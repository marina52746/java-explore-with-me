package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.service.EventService;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.eventRequest.dto.EventRequestStatusUpdateRequest;
import ru.practicum.pagination.FromSizeRequest;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/events")
public class PrivateEventController {

    private final EventService eventService;

    @GetMapping
    public List<EventShortDto> getUserEvents(@PathVariable Long userId,
                                             @RequestParam(required = false, defaultValue = "0") Integer from,
                                             @RequestParam(required = false, defaultValue = "10") Integer size) {
        final PageRequest pageRequest = FromSizeRequest.of(from, size, Sort.by(DESC, "id"));
        return eventService.getUserEvents(userId, pageRequest);
    }

    @PostMapping
    public ResponseEntity<Object> createUserEvent(@PathVariable Long userId,
                                                  @Valid @RequestBody NewEventDto newEventDto) {
        return eventService.createUserEvent(userId, newEventDto);
    }

    @GetMapping(path = "/{eventId}")
    public ResponseEntity<Object> getUserEvent(@PathVariable Long userId,
                                            @PathVariable Long eventId) {
        return eventService.getUserEvent(userId, eventId);
    }

    @PatchMapping(path = "/{eventId}")
    public ResponseEntity<Object> updateUserEvent(@PathVariable("userId") Long userId,
                                                  @PathVariable("eventId") Long eventId,
                                                  @RequestBody @Valid UpdateEventUserRequest updateEvent) {
        return eventService.updateUserEvent(userId, eventId, updateEvent);
    }

    @GetMapping(path = "/{eventId}/requests")
    public ResponseEntity<Object> getUserRequestsOnEvent(@PathVariable Long userId,
                                                         @PathVariable Long eventId) {
        return eventService.getUserRequestsOnEvent(userId, eventId);
    }

    @PatchMapping(path = "/{eventId}/requests")
    public ResponseEntity<Object> changeStatusParticipationRequest(@PathVariable Long userId,
                                                                   @PathVariable Long eventId,
                                                                   @RequestBody(required = false) EventRequestStatusUpdateRequest sur) {
        return eventService.changeStatusParticipationRequest(userId, eventId, sur);
    }
}
