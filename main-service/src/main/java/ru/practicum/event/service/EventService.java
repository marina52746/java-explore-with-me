package ru.practicum.event.service;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.event.filter.AdminFilter;
import ru.practicum.event.filter.UserFilter;
import ru.practicum.eventRequest.dto.EventRequestStatusUpdateRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventService {
    List<EventShortDto> getUserEvents(Long userId, Pageable pageable);

    ResponseEntity<Object> createUserEvent(Long userId, NewEventDto newEventDto);

    ResponseEntity<Object> getUserEvent(Long userId, Long eventId);

    ResponseEntity<Object> updateUserEvent(Long userId, Long eventId, UpdateEventUserRequest updateEvent);

    ResponseEntity<Object> getUserRequestsOnEvent(Long userId, Long eventId);

    ResponseEntity<Object> changeStatusParticipationRequest(Long userId, Long eventId,
                                                            EventRequestStatusUpdateRequest sur);

    ResponseEntity<Object> getAdminEvents(AdminFilter searchEntity, Pageable pageable);

    ResponseEntity<Object> updateAdminEvent(Long eventId, UpdateEventUserRequest updateEvent);

    @Transactional(readOnly = true)
    ResponseEntity<Object> getUserEvents(UserFilter uf, Pageable pageable, HttpServletRequest request);

    ResponseEntity<Object> getEventById(Long id, HttpServletRequest request);
}
