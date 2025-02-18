package ru.practicum.ewm.service.request;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.ewm.dto.request.ParticipationRequestDto;
import ru.practicum.ewm.enums.EventState;
import ru.practicum.ewm.enums.RequestStatus;
import ru.practicum.ewm.enums.RequestStatusUpdate;
import ru.practicum.ewm.exception.exceptions.BadRequestException;
import ru.practicum.ewm.exception.exceptions.NotFoundException;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.Request;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.model.mapper.RequestMapper;
import ru.practicum.ewm.repostirory.EventRepository;
import ru.practicum.ewm.repostirory.RequestRepository;
import ru.practicum.ewm.repostirory.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    // !!PRIVATE
    @Override
    public List<ParticipationRequestDto> getRequestsByCurrentUserOfCurrentEvent(Long userId, Long eventId) {
        List<ParticipationRequestDto> participationRequestDtoList = new ArrayList<>();

        if (!userRepository.existsById(userId) && !eventRepository.existsById(eventId)) {
            return participationRequestDtoList;
        }

        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User does not exist " + userId);
        }
        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundException("Event does not exist " + eventId);
        }

        List<Request> requestList;
        if (userId.equals(eventRepository.findById(eventId).get().getInitiator().getId())) {
            requestList = requestRepository.findAllByEvent_InitiatorIdAndEvent_Id(userId, eventId);
        } else {
            throw new ValidationException("User с id " + userId + " инициатор event с id " + eventId);
        }

        for (Request request : requestList) {
            participationRequestDtoList.add(requestMapper.toParticipationRequestDto(request));
        }
        return participationRequestDtoList;
    }

    @Override
    public EventRequestStatusUpdateResult updateRequest(Long userId, Long eventId,
                                                        EventRequestStatusUpdateRequest eventRequest) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User с id " + userId + " не создан");
        }
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event с id " + eventId + " не создан"));

        if (event.getParticipantLimit() == 0 && !event.getRequestModeration()) {
            throw new ValidationException("Moderation не требуется: id - " + eventId);
        }

        Long confirmedRequest = requestRepository.countByEventIdAndStatus(eventId, RequestStatus.CONFIRMED);

        if (confirmedRequest >= event.getParticipantLimit()) {
            throw new BadRequestException("Participation достигнут лимит на участив в Event с id " + eventId);
        }

        List<Long> requestIdList = eventRequest.getRequestIds();
        RequestStatusUpdate status = eventRequest.getStatus();

        List<Request> requestList = requestRepository.findAllByIdIn(requestIdList);
        if (requestList.isEmpty()) {
            throw new NotFoundException("Requests не существует ");
        }

        List<Request> confirmedRequests = new ArrayList<>();
        List<Request> rejectedRequests = new ArrayList<>();

        List<Request> updatedRequests = new ArrayList<>();

        for (Request currentRequest : requestList) {
            if (status == RequestStatusUpdate.CONFIRMED && currentRequest.getStatus().equals(RequestStatus.PENDING)) {
                if (currentRequest.getStatus().equals(RequestStatus.CONFIRMED)) {
                    throw new BadRequestException("Request уже подтверждён");
                }
                if (confirmedRequest >= event.getParticipantLimit()) {
                    currentRequest.setStatus(RequestStatus.REJECTED);
                    updatedRequests.add(currentRequest);
                    rejectedRequests.add(currentRequest);
                }
                currentRequest.setStatus(RequestStatus.CONFIRMED);
                updatedRequests.add(currentRequest);
                confirmedRequest++;
                confirmedRequests.add(currentRequest);
            }

            if (status == RequestStatusUpdate.REJECTED && currentRequest.getStatus().equals(RequestStatus.PENDING)) {
                currentRequest.setStatus(RequestStatus.REJECTED);
                updatedRequests.add(currentRequest);
                rejectedRequests.add(currentRequest);
            }
        }

        requestRepository.saveAll(updatedRequests);
        eventRepository.save(event);

        List<ParticipationRequestDto> confirmedRequestsDto =
                confirmedRequests.stream().map(requestMapper::toParticipationRequestDto).collect(Collectors.toList());
        List<ParticipationRequestDto> rejectedRequestsDto =
                rejectedRequests.stream().map(requestMapper::toParticipationRequestDto).collect(Collectors.toList());

        EventRequestStatusUpdateResult updateResult = new EventRequestStatusUpdateResult();
        updateResult.setConfirmedRequests(confirmedRequestsDto);
        updateResult.setRejectedRequests(rejectedRequestsDto);


        return updateResult;
    }


    @Override
    public List<ParticipationRequestDto> getRequestsByCurrentUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User с id " + userId + " не существует");
        }
        List<Request> requestList = requestRepository.findAllByRequesterIdAndNotInitiator(userId);
        List<ParticipationRequestDto> participationRequestDtoList = new ArrayList<>();
        for (Request request : requestList) {
            participationRequestDtoList.add(requestMapper.toParticipationRequestDto(request));
        }
        return participationRequestDtoList;
    }

    @Override
    public ParticipationRequestDto addRequest(Long userId, Long eventId) {

        User requester = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User с id " + userId + " не существует"));

        // выгружаем данные события
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event с id " + eventId + " не существует"));

        // создаем запрос
        Request request = new Request(LocalDateTime.now(), event, requester, RequestStatus.PENDING);

        if (requestRepository.existsByRequesterIdAndEventId(userId, eventId)) {
            throw new BadRequestException("Request с id-пользователем и id-event уже существует " + userId + eventId);
        }

        if (event.getInitiator().getId().equals(userId)) {
            throw new BadRequestException("Initiator не может запрашивать " + userId);
        }
        if (!(event.getState().equals(EventState.PUBLISHED))) {
            throw new BadRequestException("Event ещё не опубликован");
        }

        Long confirmedRequest = requestRepository.countByEventIdAndStatus(eventId, RequestStatus.CONFIRMED);
        Long limit = event.getParticipantLimit();

        if (limit != 0) {
            if (limit.equals(confirmedRequest)) {
                throw new BadRequestException("Достигнут лимит подтверждений: " + limit);
            }
        } else {
            request.setStatus(RequestStatus.CONFIRMED);
        }

        if (!event.getRequestModeration()) {
            request.setStatus(RequestStatus.CONFIRMED);
        }

        requestRepository.save(request);
        return requestMapper.toParticipationRequestDto(request);
    }

    @Override
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        Request request = requestRepository.findByIdAndRequesterId(requestId, userId)
                .orElseThrow(() -> new NotFoundException("Запрос с id " + requestId
                        + " от пользователя с " + userId + " не существует"));

        request.setStatus(RequestStatus.CANCELED);
        requestRepository.save(request);

        return requestMapper.toParticipationRequestDto(request);
    }

}
