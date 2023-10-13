package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.service.EventService;
import ru.practicum.event.filter.UserFilter;
import ru.practicum.pagination.FromSizeRequest;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/events")
public class PublicEventController {
    private final EventService eventService;

    @GetMapping
    public ResponseEntity<Object> getEvents(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(required = false) Boolean onlyAvailable,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false, defaultValue = "0") Integer from,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            HttpServletRequest request) {
        final PageRequest pageRequest =
                sort == null ? FromSizeRequest.of(from, size)
                : (sort.equals("EVENT_DATE") ?  FromSizeRequest.of(from, size, Sort.by(DESC, "eventDate"))
                        : FromSizeRequest.of(from, size, Sort.by(DESC, "views")));
        var searchEntity = new UserFilter(text, categories, paid, rangeStart, rangeEnd, onlyAvailable);
        return eventService.getUserEvents(searchEntity, pageRequest, request);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Object> getEventById(@PathVariable Long id,
                                               HttpServletRequest request) {
        return eventService.getEventById(id, request);
    }
}
