package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.filter.AdminFilter;
import ru.practicum.event.service.EventService;
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.pagination.FromSizeRequest;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/events")
public class AdminEventController {
    private final EventService eventService;

    @GetMapping
    public ResponseEntity<Object> getEvents(
            @RequestParam(required = false) List<Long> users,
            @RequestParam(required = false) List<String> states,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(required = false, defaultValue = "0") Integer from,
            @RequestParam(required = false, defaultValue = "10") Integer size) {
        final PageRequest pageRequest = FromSizeRequest.of(from, size, Sort.by(DESC, "views"));
        var searchEntity = new AdminFilter(users, states, categories, rangeStart, rangeEnd);
        return eventService.getAdminEvents(searchEntity, pageRequest);
    }

    @PatchMapping(path = "/{eventId}")
    public ResponseEntity<Object> updateAdminEvent(@PathVariable Long eventId,
                                              @RequestBody @Valid UpdateEventUserRequest updateEvent) {
        return eventService.updateAdminEvent(eventId, updateEvent);
    }
}
