package ru.practicum.ewm.dto.location;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LocationDto {
    private Long id;

    private Float lat; // Широта

    private Float lon; // Долгота
}