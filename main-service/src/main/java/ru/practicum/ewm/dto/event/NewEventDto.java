package ru.practicum.ewm.dto.event;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.stream.Location;
import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewEventDto {

    @NotNull
    @Size(min = 20, max = 2000)
    private String annotation;

    @NotNull
    private Long category;

    @NotNull
    @Size(min = 20, max = 7000)
    private String description;

    @NotNull
    private LocalDateTime eventDate;

    @NotNull
    private Location location;

    private Boolean paid = false;
    private Long participantLimit = 0L;
    private Boolean requestModeration = true;

    @NotNull
    @Size(min = 3, max = 120)
    private String title;

}
