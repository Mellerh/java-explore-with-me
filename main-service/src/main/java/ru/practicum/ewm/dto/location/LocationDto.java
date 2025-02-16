package ru.practicum.ewm.dto.location;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationDto {

    private Long id;
    private Float lat;
    private Float lon;

}
