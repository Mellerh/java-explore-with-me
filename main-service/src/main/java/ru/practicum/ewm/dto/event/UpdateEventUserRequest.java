package ru.practicum.ewm.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.dto.location.LocationDto;
import ru.practicum.ewm.enums.StateActionUser;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventUserRequest {


//    @Size(min = 20, max = 2000)
    private String annotation;

    private Long category;

//    @Size(min = 20, max = 7000)
    private String description;

    private LocalDateTime eventDate;
    private LocationDto location;

    private Boolean paid;
    private Long participantLimit;

    private Boolean requestModeration;

    private StateActionUser stateAction;

//    @Size(min = 3, max = 120)
    private String title;

}
