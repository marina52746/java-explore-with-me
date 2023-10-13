package ru.practicum.eventRequest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.eventRequest.enums.ParticipationStatus;
import ru.practicum.eventRequest.model.ParticipationRequest;

import java.util.List;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {

    ParticipationRequest findFirstByRequesterAndEvent(Long requester, Long event);

    List<ParticipationRequest> findAllByEvent(Long requester);

    List<ParticipationRequest> findAllByRequester(Long requester);

    List<ParticipationRequest> findByIdIn(List<Long> requesterIds);

    List<ParticipationRequest> findAllByEventAndStatus(Long eventId, ParticipationStatus status);

}
