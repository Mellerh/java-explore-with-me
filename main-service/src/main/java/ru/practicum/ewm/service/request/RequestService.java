package ru.practicum.ewm.service.request;

import ru.practicum.ewm.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.ewm.dto.request.ParticipationRequestDto;

import java.util.List;

public interface RequestService {

    // private: events
    // Получение инфо о запросах на участие в событии текущего пользователя
    List<ParticipationRequestDto> getRequestsByCurrentUserOfCurrentEvent(Long userId, Long eventId);

    // Изменение статуса (подтверждена, отменена) заявок на участие в событии текущего пользователя
    EventRequestStatusUpdateResult updateRequest(Long userId,
                                                 Long eventId,
                                                 EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest);

    // private: requests
    // Получение инфо о заявках текущего пользователя на участие в чужих событиях
    List<ParticipationRequestDto> getRequestsByCurrentUser(Long userId);

    // Добавление запроса от текущего пользователя на участие в событии
    ParticipationRequestDto addRequest(Long userId, Long eventId);

    // Отмена своего запроса на участие в событии
    ParticipationRequestDto cancelRequest(Long userId, Long requestId);

}
