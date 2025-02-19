package ru.practicum.ewm.service.event;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.client.stats.StatsClient;
import ru.practicum.ewm.dto.event.*;
import ru.practicum.ewm.dto.stats.ViewStats;
import ru.practicum.ewm.dto.stats.ViewStatsRequest;
import ru.practicum.ewm.enums.*;
import ru.practicum.ewm.exception.exceptions.BadRequestException;
import ru.practicum.ewm.exception.exceptions.NotFoundException;
import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.Location;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.model.mapper.EventMapper;
import ru.practicum.ewm.repostirory.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final RequestRepository requestRepository;

    private final StatsClient statsClient;
    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // !!PRIVATE
    @Override
    public List<EventShortDto> getEventsByInitiator(Long userId, Pageable pageable) {
        List<Event> eventList = eventRepository.findAllByInitiatorId(userId, pageable).toList();
        List<EventShortDto> eventShortDtoList = new ArrayList<>();
        for (Event currentEvent : eventList) {
            EventShortDto eventShortDto = eventMapper.toEventShortDto(currentEvent);
            eventShortDtoList.add(addShortConfirmedRequestsAndViews(eventShortDto));
        }
        return eventShortDtoList;
    }
    // добавление нового события
    @Override
    public EventFullDto addEvent(Long userId, NewEventDto newEventDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не существует"));

        if (newEventDto.getEventDate().minusHours(2).isBefore(LocalDateTime.now())) {
            throw new ValidationException("Неккореткное время Event");
        }

        Event eventToSave = eventMapper.toEvent(newEventDto);
        eventToSave.setState(EventState.PENDING);
        eventToSave.setCreatedOn(LocalDateTime.now());

        Category category = categoryRepository.findById(newEventDto.getCategory())
                .orElseThrow(() -> new NotFoundException("Category не существует"));
        eventToSave.setCategory(category);
        eventToSave.setInitiator(user);
        eventRepository.save(eventToSave);

        return addConfirmedRequestsAndViews(eventMapper.toEventFullDto(eventToSave));
    }

    @Override
    public EventFullDto getEventByInitiator(Long userId, Long eventId) {
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Event с id " + eventId + " не существует"));
        return addConfirmedRequestsAndViews(eventMapper.toEventFullDto(event));
    }

    @Override
    public EventFullDto updateEventByInitiator(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest) {
        Event eventToUpdate = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event с id " + eventId + " не существует"));

        if (eventToUpdate.getState().equals(EventState.CANCELED) || eventToUpdate.getState().equals(EventState.PENDING)) {
            if (updateEventUserRequest.getEventDate() != null
                    && updateEventUserRequest.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
                throw new ValidationException("Неккореткное время Event");
            }
            if (StateActionUser.SEND_TO_REVIEW == updateEventUserRequest.getStateAction()) {
                eventToUpdate.setState(EventState.PENDING);
            }
            if (StateActionUser.CANCEL_REVIEW == updateEventUserRequest.getStateAction()) {
                eventToUpdate.setState(EventState.CANCELED);
            }
        } else {
            throw new BadRequestException("State должен быть либо CANCELED либо PENDING" + eventToUpdate.getState());
        }

        updateEventEntity(updateEventUserRequest, eventToUpdate);
        eventRepository.save(eventToUpdate);
        return addConfirmedRequestsAndViews(eventMapper.toEventFullDto(eventToUpdate));
    }

    // !!ADMIN
    @Override
    public List<EventFullDto> getEventsByAdmin(List<Long> userIdList, List<String> states, List<Long> categories,
                                               String rangeStart, String rangeEnd, Integer from, Integer size) {
        final PageRequest pageRequest = PageRequest.of(from / size, size);

        List<EventFullDto> eventFullDtoList = new ArrayList<>();
        if (states == null & rangeStart == null & rangeEnd == null) {
            List<Event> eventList = eventRepository.findAll(pageRequest).toList();
            for (Event currentEvent : eventList) {
                EventFullDto eventFullDto = eventMapper.toEventFullDto(currentEvent);
                eventFullDtoList.add(addConfirmedRequestsAndViews(eventFullDto));
            }
            return eventFullDtoList;
        }

        LocalDateTime start;
        if (rangeStart != null && !rangeStart.isEmpty()) {
            start = LocalDateTime.parse(rangeStart, DTF);
        } else {
            start = LocalDateTime.now().plusYears(5);
        }

        LocalDateTime end;
        if (rangeEnd != null && !rangeEnd.isEmpty()) {
            end = LocalDateTime.parse(rangeEnd, DTF);
        } else {
            end = LocalDateTime.now().plusYears(5);
        }

        List<Event> eventList = eventRepository.findEvents(userIdList, states, categories, start, end, pageRequest).toList();
        for (Event currentEvent : eventList) {
            EventFullDto eventFullDto = eventMapper.toEventFullDto(currentEvent);
            eventFullDtoList.add(addConfirmedRequestsAndViews(eventFullDto));
        }
        return eventFullDtoList;
    }

    @Override
    public EventFullDto updateEventByAdmin(Long eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        Event eventToUpdate = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event с id " + eventId + " не существует"));

        if (updateEventAdminRequest.getEventDate() != null) {
            if (updateEventAdminRequest.getEventDate().minusHours(1).isBefore(LocalDateTime.now())) {
                throw new ValidationException("Неккореткное время Event");
            }
        }
        if (updateEventAdminRequest.getStateAction() != null) {
            if (updateEventAdminRequest.getStateAction() == StateActionAdmin.PUBLISH_EVENT) {
                if (eventToUpdate.getState().equals(EventState.PENDING)) {
                    eventToUpdate.setState(EventState.PUBLISHED);
                    eventToUpdate.setPublishedOn(LocalDateTime.now());
                } else {
                    throw new BadRequestException("Event должен быть PENDING " +
                            updateEventAdminRequest.getStateAction());
                }
            }
            if (updateEventAdminRequest.getStateAction() == StateActionAdmin.REJECT_EVENT) {
                if (eventToUpdate.getState().equals(EventState.PUBLISHED)) {
                    throw new BadRequestException("Event должен быть PENDING " +
                            updateEventAdminRequest.getStateAction());
                }
                eventToUpdate.setState(EventState.CANCELED);
            }
        }
        updateEventEntity(updateEventAdminRequest, eventToUpdate);

        eventRepository.save(eventToUpdate);
        return addConfirmedRequestsAndViews(eventMapper.toEventFullDto(eventToUpdate));
    }

    // !!PUBLIC
    @Override
    public List<EventShortDto> getEventList(String text, List<Long> categoryIdList, Boolean paid, String rangeStart,
                                            String rangeEnd, Boolean onlyAvailable, String sort, Integer from,
                                            Integer size, String userIp, String requestUri) {
        LocalDateTime start = null;
        LocalDateTime end = null;

        statsClient.hit(userIp, requestUri);

        if (rangeStart != null && rangeEnd != null) {
            start = LocalDateTime.parse(rangeStart, DTF);
            end = LocalDateTime.parse(rangeEnd, DTF);
            if (start.isAfter(end)) {
                throw new ValidationException("Некорректная дата");
            }
        } else {
            if (rangeStart == null && rangeEnd == null) {
                start = LocalDateTime.now();
                end = LocalDateTime.now().plusYears(10);
            } else {
                if (rangeStart == null) {
                    start = LocalDateTime.now();
                }
            }
        }

        final PageRequest pageRequest = PageRequest.of(from / size, size);
        List<Event> eventList = eventRepository.searchPublishedEvents(text, categoryIdList, paid, start, end, pageRequest)
                .getContent();

        List<EventShortDto> eventShortDtoList = new ArrayList<>();
        if (eventList.isEmpty()) {
            return eventShortDtoList;
        }

        for (Event currentEvent : eventList) {
            eventShortDtoList.add(addShortConfirmedRequestsAndViews(eventMapper.toEventShortDto(currentEvent)));
        }

        if (sort != null) {
            switch (SortValue.valueOf(sort)) {
                case EVENT_DATE:
                    eventShortDtoList.sort(Comparator.comparing(EventShortDto::getEventDate));
                    break;

                case VIEWS:
                    eventShortDtoList.sort(Comparator.comparing(EventShortDto::getViews));
                    break;
                default:
                    throw new ValidationException("Невалидные параметры сортировки");
            }
        }
        return eventShortDtoList;
    }

    @Override
    public EventFullDto getEvent(Long eventId, String userIp, String requestUri) {
        Event event = eventRepository.findByIdAndState(eventId, EventState.PUBLISHED)
                .orElseThrow(() -> new NotFoundException("Event с id " + eventId + " не существует"));

        statsClient.hit(userIp, requestUri);

        return addConfirmedRequestsAndViews(eventMapper.toEventFullDto(event));
    }

    private void updateEventEntity(UpdateEventUserRequest event, Event eventToUpdate) {
        eventToUpdate.setAnnotation(Objects.requireNonNullElse(event.getAnnotation(), eventToUpdate.getAnnotation()));

        eventToUpdate.setCategory(event.getCategory() == null
                ? eventToUpdate.getCategory()
                : categoryRepository.findById(event.getCategory()).orElseThrow(() -> new NotFoundException("Категория не найдена")));

        eventToUpdate.setDescription(Objects.requireNonNullElse(event.getDescription(), eventToUpdate.getDescription()));
        eventToUpdate.setEventDate(Objects.requireNonNullElse(event.getEventDate(), eventToUpdate.getEventDate()));

        eventToUpdate.setLocation(event.getLocation() == null
                ? eventToUpdate.getLocation()
                : locationRepository.findByLatAndLon(event.getLocation().getLat(), event.getLocation().getLon())
                .orElse(new Location(null, event.getLocation().getLat(), event.getLocation().getLon())));

        eventToUpdate.setPaid(Objects.requireNonNullElse(event.getPaid(), eventToUpdate.getPaid()));
        eventToUpdate.setParticipantLimit(Objects.requireNonNullElse(event.getParticipantLimit(), eventToUpdate.getParticipantLimit()));

        eventToUpdate.setRequestModeration(Objects.requireNonNullElse(event.getRequestModeration(), eventToUpdate.getRequestModeration()));
        eventToUpdate.setTitle(Objects.requireNonNullElse(event.getTitle(), eventToUpdate.getTitle()));
    }

    private void updateEventEntity(UpdateEventAdminRequest event, Event eventToUpdate) {
        eventToUpdate.setAnnotation(Objects.requireNonNullElse(event.getAnnotation(), eventToUpdate.getAnnotation()));
        eventToUpdate.setCategory(event.getCategory() == null
                ? eventToUpdate.getCategory()
                : categoryRepository.findById(event.getCategory()).orElseThrow(() -> new NotFoundException("Категория не найдена")));

        eventToUpdate.setDescription(Objects.requireNonNullElse(event.getDescription(), eventToUpdate.getDescription()));
        eventToUpdate.setEventDate(Objects.requireNonNullElse(event.getEventDate(), eventToUpdate.getEventDate()));

        eventToUpdate.setLocation(event.getLocation() == null
                ? eventToUpdate.getLocation()
                : locationRepository.findByLatAndLon(event.getLocation().getLat(), event.getLocation().getLon())
                .orElse(new Location(null, event.getLocation().getLat(), event.getLocation().getLon())));

        eventToUpdate.setPaid(Objects.requireNonNullElse(event.getPaid(), eventToUpdate.getPaid()));
        eventToUpdate.setParticipantLimit(Objects.requireNonNullElse(event.getParticipantLimit(), eventToUpdate.getParticipantLimit()));

        eventToUpdate.setRequestModeration(Objects.requireNonNullElse(event.getRequestModeration(), eventToUpdate.getRequestModeration()));
        eventToUpdate.setTitle(Objects.requireNonNullElse(event.getTitle(), eventToUpdate.getTitle()));
    }

    private EventFullDto addConfirmedRequestsAndViews(EventFullDto eventFullDto) {
        eventFullDto.setConfirmedRequests(
                requestRepository.countByEventIdAndStatus(eventFullDto.getId(), RequestStatus.CONFIRMED));

        List<String> uris = new ArrayList<>();

        uris.add("/events/" + eventFullDto.getId());
        ViewStatsRequest viewStatsRequest = new ViewStatsRequest(
                LocalDateTime.now().minusYears(100),
                LocalDateTime.now(),
                uris,
                true);
        List<ViewStats> viewStatsList = statsClient.getStats(viewStatsRequest);
        if (viewStatsList.isEmpty()) {
            eventFullDto.setViews(0L);
        } else {
            eventFullDto.setViews(viewStatsList.get(0).getHits());
        }
        return eventFullDto;
    }

    private EventShortDto addShortConfirmedRequestsAndViews(EventShortDto eventShortDto) {
        eventShortDto.setConfirmedRequests(
                requestRepository.countByEventIdAndStatus(eventShortDto.getId(), RequestStatus.CONFIRMED));

        // Добавить views к каждому событию
        List<String> uris = new ArrayList<>();

        // создаем uri для обращения к базе данных статистики
        uris.add("/events/" + eventShortDto.getId());
        ViewStatsRequest viewStatsRequest = new ViewStatsRequest(
                LocalDateTime.now().minusYears(100),
                LocalDateTime.now(),
                uris,
                true);
        List<ViewStats> viewStatsList = statsClient.getStats(viewStatsRequest);
        if (viewStatsList.isEmpty()) {
            eventShortDto.setViews(0L);
        } else {
            eventShortDto.setViews(viewStatsList.get(0).getHits());
        }
        return eventShortDto;
    }

}
