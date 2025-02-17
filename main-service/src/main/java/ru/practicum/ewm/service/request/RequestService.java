package ru.practicum.ewm.service.request;

import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.ewm.dto.request.ParticipationRequestDto;

import java.util.List;

@Service
public interface RequestService {

    List<ParticipationRequestDto> getRequestsByCurrentUserOfCurrentEvent(Long userId, Long eventId);

    EventRequestStatusUpdateResult updateRequest(Long userId, Long eventId,
                                                 EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest);

    List<ParticipationRequestDto> getRequestsByCurrentUser(Long userId);

    ParticipationRequestDto addRequest(Long userId, Long eventId);

    ParticipationRequestDto cancelRequest(Long userId, Long requestId);

}
