package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.client.StatClient;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.constants.MyConst;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.event.enums.EventState;
import ru.practicum.event.enums.StateAction;
import ru.practicum.event.filter.AdminFilter;
import ru.practicum.event.filter.AdminFilterBuilder;
import ru.practicum.event.filter.UserFilter;
import ru.practicum.event.filter.UserFilterBuilder;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.eventRequest.dto.EventRequestStatusUpdateRequest;
import ru.practicum.eventRequest.dto.EventRequestStatusUpdateResult;
import ru.practicum.eventRequest.dto.ParticipationRequestDto;
import ru.practicum.eventRequest.enums.ParticipationStatus;
import ru.practicum.eventRequest.mapper.ParticipationRequestMapper;
import ru.practicum.eventRequest.model.ParticipationRequest;
import ru.practicum.eventRequest.repository.ParticipationRequestRepository;
import ru.practicum.exception.IntegrityViolationException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidationException;
import ru.practicum.hit.HitDto;
import ru.practicum.location.Location;
import ru.practicum.location.LocationRepository;
import ru.practicum.stat.StatDto;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
@ComponentScan(basePackageClasses = StatClient.class)
@ComponentScan(basePackages = "ru.practicum")
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final ParticipationRequestRepository participationRequestRepository;
    private final AdminFilterBuilder adminFilterBuilder;
    private final UserFilterBuilder userFilterBuilder;
    private final StatClient statClient;

    @Override
    public List<EventShortDto> getUserEvents(Long userId, Pageable pageable) {
        List<Event> events = eventRepository.findAllByUserId(userId, pageable).getContent();
        return events.stream()
                .map(EventMapper::fromEventToEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<Object> createUserEvent(Long userId, NewEventDto newEventDto) {
        String ldtn = LocalDateTime.now().format(MyConst.formatter);
        LocalDateTime dtnow = LocalDateTime.parse(ldtn, MyConst.formatter);
        if (newEventDto.getEventDate().minusHours(2).isBefore(dtnow))
            throw new ValidationException("For the requested operation the conditions are not met",
                    "Field: eventDate. Error: must contain date after (now + 2 hours). EventDate: "
                            + newEventDto.getEventDate() + ", Time now: " + ldtn);
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("Event initiator not found", "User not found, id=" + userId)
        );
        Category category = categoryRepository.findById(newEventDto.getCategory()).orElseThrow(
                () -> new NotFoundException("Event category not found",
                        "Category not found, id=" + newEventDto.getCategory())
        );
        Location location = locationRepository.findFirstByLatAndLon(newEventDto.getLocation().getLat(),
                newEventDto.getLocation().getLon());
        if (location == null)
            location = locationRepository.save(new Location(newEventDto.getLocation().getLat(),
                    newEventDto.getLocation().getLon()));
        Event event = EventMapper.fromNewEventDtoToEvent(user, category, location, newEventDto);
        return new ResponseEntity<>(EventMapper.fromEventToEventFullDto(eventRepository.save(event)), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Object> getUserEvent(Long userId, Long eventId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User not found", "User not found, id=" + userId)
        );
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Event not found", "Event not found, id=" + eventId));
        if (!Objects.equals(event.getUser().getId(), userId))
            throw new NotFoundException("Event not user's", "Event not user's, id=" + eventId + " userId =" + userId);
        return new ResponseEntity<>(EventMapper.fromEventToEventFullDto(event), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> updateUserEvent(Long userId, Long eventId, UpdateEventUserRequest updateEvent) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User not found", "User not found, id=" + userId));
        var event = eventRepository.findById(eventId);
        if (event.isEmpty() || !Objects.equals(event.get().getUser().getId(), userId))
            throw new NotFoundException("Event not found", "Event not found, id=" + eventId);
        if (event.get().getState() == EventState.PUBLISHED)
            throw new IntegrityViolationException("Can't change PUBLISHED events",
                    "Event must be PENDING or CANCELED");
        var ldtn = LocalDateTime.now();
        if (updateEvent.getEventDate() != null && (updateEvent.getEventDate()).minusHours(2).isBefore(ldtn))
            throw new ValidationException(HttpStatus.FORBIDDEN,
                    "For the requested operation the conditions are not met",
                    "Field: eventDate. Error: must contain date after (now + 2 hours). EventDate: "
                            + updateEvent.getEventDate() + ", Time now: " + ldtn);
        Event newEvent = event.get();
        if (updateEvent.getAnnotation() != null) {
            if (updateEvent.getAnnotation().isBlank())
                throw new ValidationException("Annotation is blank","Annotation mustn't be blank");
            if (updateEvent.getAnnotation().length() < 20 || updateEvent.getAnnotation().length() > 2000)
                throw new ValidationException("Wrong annotation length","Annotation length must be between 20-2000");
            newEvent.setAnnotation(updateEvent.getAnnotation());
        }
        if (updateEvent.getCategory() != null) {
            Category category = categoryRepository.findById(updateEvent.getCategory())
                            .orElseThrow(() -> new NotFoundException("Event category not found",
                                    "Category not found, id=" + updateEvent.getCategory()));
            newEvent.setCategory(category);
        }
        if (updateEvent.getDescription() != null) {
            if (updateEvent.getDescription().isBlank())
                throw new ValidationException("Description is blank","Description mustn't be blank");
            if (updateEvent.getDescription().length() < 20 || updateEvent.getDescription().length() > 7000)
                throw new ValidationException("Wrong description length","Description length must be between 20-7000");
            newEvent.setDescription(updateEvent.getDescription());
        }
        if (updateEvent.getEventDate() != null) {
            newEvent.setEventDate(updateEvent.getEventDate());
        }
        if (updateEvent.getLocation() != null) {
            Location location = locationRepository.findFirstByLatAndLon(updateEvent.getLocation().getLat(),
                    updateEvent.getLocation().getLon());
            if (location == null)
                location = locationRepository.save(new Location(updateEvent.getLocation().getLat(),
                        updateEvent.getLocation().getLon()));
            newEvent.setLocation(location);
        }
        if (updateEvent.getPaid() != null) {
            newEvent.setPaid(updateEvent.getPaid());
        }
        if (updateEvent.getParticipantLimit() != null) {
            newEvent.setParticipantLimit(updateEvent.getParticipantLimit());
        }
        if (updateEvent.getRequestModeration() != null) {
            newEvent.setRequestModeration(updateEvent.getRequestModeration());
        }
        if (updateEvent.getStateAction() != null) {
            if (StateAction.fromStringToStateActionUser(updateEvent.getStateAction()) == StateAction.SEND_TO_REVIEW)
                newEvent.setState(EventState.PENDING);
            else if (StateAction.fromStringToStateActionUser(updateEvent.getStateAction()) == StateAction.CANCEL_REVIEW)
                newEvent.setState(EventState.CANCELED);
        }
        if (updateEvent.getTitle() != null) {
            if (updateEvent.getTitle().isBlank())
                throw new ValidationException("Title is blank","Title mustn't be blank");
            if (updateEvent.getTitle().length() < 3 || updateEvent.getTitle().length() > 120)
                throw new ValidationException("Wrong title length","Title length must be between 3-120");
            newEvent.setTitle(updateEvent.getTitle());
        }
        return new ResponseEntity<>(EventMapper.fromEventToEventFullDto(newEvent), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> getUserRequestsOnEvent(Long userId, Long eventId) {
        List<ParticipationRequestDto> userRequests = participationRequestRepository
                .findAllByEvent(eventId).stream().map(ParticipationRequestMapper::fromRequestToRequestDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(userRequests, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> changeStatusParticipationRequest(Long userId, Long eventId,
                                                                   EventRequestStatusUpdateRequest sur) {
        userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User not found", "User not found, id=" + userId));
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Event not found", "Event not found, id=" + eventId));

        if (event.getParticipantLimit() != 0
            && event.getConfirmedRequests() >= event.getParticipantLimit())
            throw new IntegrityViolationException("Reached limit of participants", "Reached limit of participants");

        List<ParticipationRequest> requests = participationRequestRepository
                .findByIdIn(sur.getRequestIds());
        for (var req : requests) {
            if (req.getStatus() != ParticipationStatus.PENDING)
                throw new IntegrityViolationException("Can't change request state, id=" + req.getId(),
                        "Request must be in status PENDING");
        }
        List<ParticipationRequestDto> confirmed = new ArrayList<>();
        List<ParticipationRequestDto> rejected = new ArrayList<>();
        if (sur.getStatus() == ParticipationStatus.CONFIRMED
                && event.getParticipantLimit() != null && event.getParticipantLimit() != 0) {
            if (event.getConfirmedRequests() >= event.getParticipantLimit())
                throw new IntegrityViolationException("Reached limit of participants",
                        "Reached limit of participants: limit=" + event.getParticipantLimit()
                                + " confirmed=" + event.getConfirmedRequests());
            if (event.getConfirmedRequests() + sur.getRequestIds().size() > event.getParticipantLimit()) {
                //chasti otkloniti, chasti utverditi
                List<Long> reqIdToConfirm = sur.getRequestIds().stream()
                        .limit(event.getParticipantLimit() - event.getConfirmedRequests())
                        .collect(Collectors.toList());
                var requestsToConfirm = participationRequestRepository
                        .findByIdIn(reqIdToConfirm);
                for (var req: requestsToConfirm) {
                    req.setStatus(ParticipationStatus.CONFIRMED);
                }
                event.setConfirmedRequests(event.getConfirmedRequests() + reqIdToConfirm.size());
                eventRepository.save(event);
                participationRequestRepository.saveAll(requestsToConfirm);
                requestsToConfirm.stream().forEach(
                        x -> confirmed.add(ParticipationRequestMapper.fromRequestToRequestDto(x)));
                //Otkloniti ostalnie
                List<ParticipationRequest> requestsToReject = participationRequestRepository
                        .findAllByEventAndStatus(eventId, ParticipationStatus.PENDING);
                for (var req: requestsToReject) {
                    req.setStatus(ParticipationStatus.REJECTED);
                }
                participationRequestRepository.saveAll(requestsToReject);
                requestsToReject.stream().forEach(
                        x -> rejected.add(ParticipationRequestMapper.fromRequestToRequestDto(x)));
            } else {
                List<ParticipationRequest> requestsToConfirm = new ArrayList<>(participationRequestRepository
                        .findByIdIn(sur.getRequestIds()));
                for (var req : requestsToConfirm) {
                    req.setStatus(ParticipationStatus.CONFIRMED);
                }
                event.setConfirmedRequests(event.getConfirmedRequests() + requestsToConfirm.size());
                eventRepository.save(event);
                participationRequestRepository.saveAll(requestsToConfirm);
                requestsToConfirm.stream().forEach(
                        x -> confirmed.add(ParticipationRequestMapper.fromRequestToRequestDto(x)));
            }
        } else if (sur.getStatus() == ParticipationStatus.REJECTED) {
            List<ParticipationRequest> requestsToReject = new ArrayList<>(participationRequestRepository
                    .findByIdIn(sur.getRequestIds()));
            for (var req : requestsToReject) {
                req.setStatus(ParticipationStatus.REJECTED);
            }
            participationRequestRepository.saveAll(requestsToReject);
            requestsToReject.stream().forEach(
                    x -> rejected.add(ParticipationRequestMapper.fromRequestToRequestDto(x)));
        }
        EventRequestStatusUpdateResult ersur = new EventRequestStatusUpdateResult(confirmed, rejected);
        return new ResponseEntity<>(ersur, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> getAdminEvents(AdminFilter af, Pageable pageable) {
        List<Event> events = eventRepository.findAll(adminFilterBuilder.build(af), pageable).getContent();
        return new ResponseEntity<>(
                                events.stream()
                                .map(EventMapper::fromEventToEventFullDto)
                                .collect(Collectors.toList()),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> updateAdminEvent(Long eventId, UpdateEventUserRequest updateEvent) {
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Event not found", "Event not found, id=" + eventId));
        if (updateEvent.getEventDate() != null && (updateEvent.getEventDate().minusHours(1).isBefore(LocalDateTime.now())))
            throw new ValidationException("Too late to publish", "Publish date must be < (event date + 1 hour)");
        if (updateEvent.getStateAction() != null) {
            if (StateAction.fromStringToStateActionAdmin(updateEvent.getStateAction()) == StateAction.PUBLISH_EVENT
                    && event.getState() != EventState.PENDING)
                throw new IntegrityViolationException("Wrong event state", "Can't publish event with state != PENDING");
            if (StateAction.fromStringToStateActionAdmin(updateEvent.getStateAction()) == StateAction.REJECT_EVENT
                    && event.getState() == EventState.PUBLISHED)
                throw new IntegrityViolationException("Wrong event state", "Can't reject event when it is PUBLISHED");
        }
        if (updateEvent.getAnnotation() != null) {
            event.setAnnotation(updateEvent.getAnnotation());
        }
        if (updateEvent.getCategory() != null) {
            Category category = categoryRepository.findById(updateEvent.getCategory())
                    .orElseThrow(() -> new NotFoundException("Event category not found",
                            "Category not found, id=" + updateEvent.getCategory()));
            event.setCategory(category);
        }
        if (updateEvent.getDescription() != null) {
            event.setDescription(updateEvent.getDescription());
        }
        if (updateEvent.getEventDate() != null) {
            event.setEventDate(updateEvent.getEventDate());
        }
        if (updateEvent.getLocation() != null) {
            Location location = locationRepository.findFirstByLatAndLon(updateEvent.getLocation().getLat(),
                    updateEvent.getLocation().getLon());
            if (location == null)
                location = locationRepository.save(new Location(updateEvent.getLocation().getLat(),
                        updateEvent.getLocation().getLon()));
            event.setLocation(location);
        }
        if (updateEvent.getPaid() != null) {
            event.setPaid(updateEvent.getPaid());
        }
        if (updateEvent.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEvent.getParticipantLimit());
        }
        if (updateEvent.getRequestModeration() != null) {
            event.setRequestModeration(updateEvent.getRequestModeration());
        }
        if (updateEvent.getStateAction() != null) {
            if (StateAction.fromStringToStateActionAdmin(updateEvent.getStateAction()) == StateAction.PUBLISH_EVENT) {
                event.setState(EventState.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
            } else if (StateAction.fromStringToStateActionAdmin(updateEvent.getStateAction()) == StateAction.REJECT_EVENT)
                event.setState(EventState.CANCELED);
        }
        if (updateEvent.getTitle() != null) {
            event.setTitle(updateEvent.getTitle());
        }
        event = eventRepository.save(event);
        return new ResponseEntity<>(EventMapper.fromEventToEventFullDto(event), HttpStatus.OK);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<Object> getUserEvents(UserFilter uf, Pageable pageable, HttpServletRequest request) {
        var ldtn = LocalDateTime.now();
        if (uf.getRangeStart() == null) uf.setRangeStart(ldtn);
        if (uf.getRangeEnd() == null) uf.setRangeEnd(ldtn.plusYears(100));
        if (uf.getRangeStart().isAfter(uf.getRangeEnd()))
            throw new ValidationException("Wrong range end", "Range end must be after range start");
        List<Event> events = eventRepository.findAll(userFilterBuilder.build(uf), pageable).getContent();
        statClient.createHit(new HitDto("ewm-main-service", request.getRequestURI(),
                request.getRemoteAddr(), LocalDateTime.now()));
        return new ResponseEntity<>(
                events
                        .stream()
                        .filter(x -> x.getState() == EventState.PUBLISHED)
                        .map(EventMapper::fromEventToEventShortDto)
                        .collect(Collectors.toList()),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> getEventById(Long id, HttpServletRequest request) {
        Event event = eventRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Event not found", "Event not found, id=" + id));
        if (event.getState() != EventState.PUBLISHED)
            throw new NotFoundException("Event is not published", "Event is not published, id=" + id);
        statClient.createHit(new HitDto("ewm-main-service", request.getRequestURI(),
                request.getRemoteAddr(), LocalDateTime.now()));
        List<StatDto> stats = statClient.getStats(
                "2000-01-01 00:00:00",
                "2100-01-01 00:00:00",
                new String[] {request.getRequestURI()},
                true).getBody();
        Long views = 0L;
        if (stats != null) {
            views = stats.stream().filter(e -> e.getUri().equals(request.getRequestURI())).findFirst().get().getHits();
        }
        event.setViews(views);
        return new ResponseEntity<>(EventMapper.fromEventToEventFullDto(event), HttpStatus.OK);
    }

}
