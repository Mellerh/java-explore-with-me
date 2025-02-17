package ru.practicum.ewm.service.event;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.event.*;

import java.util.List;

@Service
public interface EventService {

    List<EventShortDto> getEventsByInitiator(Long userId, Pageable pageable);

    EventFullDto addEvent(Long userId, NewEventDto newEventDto);

    EventFullDto getEventByInitiator(Long userId, Long eventId);

    EventFullDto updateEventByInitiator(Long userId,
                                        Long eventId,
                                        UpdateEventUserRequest updateEventUserRequest);

    List<EventFullDto> getEventsByAdmin(List<Long> userIdList,
                                        List<String> states,
                                        List<Long> categories,
                                        String rangeStart,
                                        String rangeEnd,
                                        Integer from,
                                        Integer size);

    EventFullDto updateEventByAdmin(Long eventId,
                                    UpdateEventAdminRequest updateEventAdminRequest);

    List<EventShortDto> getEventList(String text, List<Long> categoryIdList, Boolean paid,
                                     String rangeStart, String rangeEnd,
                                     Boolean onlyAvailable, String sort,
                                     Integer from, Integer size,
                                     String userIp, String requestUri);

    EventFullDto getEvent(Long eventId, String userIp, String requestUri);


}
