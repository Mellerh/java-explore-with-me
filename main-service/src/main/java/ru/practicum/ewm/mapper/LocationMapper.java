package ru.practicum.ewm.mapper;

import org.mapstruct.Mapper;
import ru.practicum.ewm.dto.location.LocationDto;
import ru.practicum.ewm.model.Location;

@Mapper(componentModel = "spring")
public interface LocationMapper {
    Location toLocation(LocationDto locationDto);

    LocationDto toLocationDto(Location location);
}
