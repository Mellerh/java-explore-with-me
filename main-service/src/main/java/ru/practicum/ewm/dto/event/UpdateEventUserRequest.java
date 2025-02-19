package ru.practicum.ewm.dto.event;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.dto.location.LocationDto;
import ru.practicum.ewm.enums.StateActionUser;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventUserRequest {
    @Size(min = 20, max = 2000)
    private String annotation;
    private Long category;
    @Size(min = 20, max = 7000)
    private String description;
    private LocalDateTime eventDate;
    private LocationDto location;
    private Boolean paid;
    @Positive
    private Long participantLimit;
    private Boolean requestModeration;
    private StateActionUser stateAction; // SEND_TO_REVIEW, CANCEL_REVIEW
    @Size(min = 3, max = 120)
    private String title;
}
