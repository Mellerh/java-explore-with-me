package ru.practicum.ewm.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.enums.RequestStatus;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParticipationRequestDto {

    private Long id;
    private LocalDateTime created;
    private Long event;
    private Long requester;
    private RequestStatus status;

}
