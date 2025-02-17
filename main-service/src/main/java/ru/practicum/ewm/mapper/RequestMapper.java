package ru.practicum.ewm.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.dto.request.ParticipationRequestDto;
import ru.practicum.ewm.model.Request;

@Mapper(componentModel = "spring")
@Component
public interface RequestMapper {

    @Mapping(source = "event.id", target = "event")
    @Mapping(source = "requester.id", target = "requester")
    ParticipationRequestDto toParticipationRequestDto(Request request);
}
