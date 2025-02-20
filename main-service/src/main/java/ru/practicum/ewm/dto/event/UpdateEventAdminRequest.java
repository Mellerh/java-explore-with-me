package ru.practicum.ewm.dto.event;

import jakarta.validation.constraints.Size;
import lombok.*;
import ru.practicum.ewm.dto.location.LocationDto;
import ru.practicum.ewm.enums.StateActionAdmin;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventAdminRequest {
    @Size(min = 20, max = 2000)
    private String annotation;
    private Long category;
    @Size(min = 20, max = 7000)
    private String description;
    private LocalDateTime eventDate;
    private LocationDto location;
    private Boolean paid;
    private Long participantLimit;
    private Boolean requestModeration;
    private StateActionAdmin stateAction; // PUBLISH_EVENT, REJECT_EVENT
    @Size(min = 3, max = 120)
    private String title;
}
