package ru.practicum.ewm.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.EventShortDto;
import ru.practicum.ewm.dto.event.NewEventDto;
import ru.practicum.ewm.model.Event;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EventMapper {

    @Mapping(source = "category", target = "category.id")
    Event toEvent(NewEventDto newEventDto);

    EventShortDto toEventShortDto(Event event);

    EventFullDto toEventFullDto(Event event);

    List<EventShortDto> toEventShortDtoList(List<Event> eventList);
}
