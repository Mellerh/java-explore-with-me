package ru.practicum.ewm.service.event;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.dto.event.*;

import java.util.List;

public interface EventService {
    // private
    // получение событий текущего пользователя
    List<EventShortDto> getEventsByInitiator(Long userId, Pageable pageable);

    // добавление нового события
    EventFullDto addEvent(Long userId, NewEventDto newEventDto);

    // полная инфо о событии добавленное текущим пользователем
    EventFullDto getEventByInitiator(Long userId, Long eventId);

    // изменения события добавленного текущим пользователем
    EventFullDto updateEventByInitiator(Long userId,
                                        Long eventId,
                                        UpdateEventUserRequest updateEventUserRequest);

    // admin
    // поиск событий
    List<EventFullDto> getEventsByAdmin(List<Long> userIdList,
                                        List<String> states,
                                        List<Long> categories,
                                        String rangeStart,
                                        String rangeEnd,
                                        Integer from,
                                        Integer size);

    // редактирование данных события и его статуса
    EventFullDto updateEventByAdmin(Long eventId,
                                    UpdateEventAdminRequest updateEventAdminRequest);

    // public
    // получение событий с возможностью фильтрации
    List<EventShortDto> getEventList(String text,
                                     List<Long> categoryIdList,
                                     Boolean paid,
                                     String rangeStart,
                                     String rangeEnd,
                                     Boolean onlyAvailable,
                                     String sort,
                                     Integer from,
                                     Integer size,
                                     String userIp,
                                     String requestUri);

    // получение подробной инфо о событии по его id
    EventFullDto getEvent(Long eventId, String userIp, String requestUri);

}
