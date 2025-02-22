package ru.practicum.ewm.dto.request;

import lombok.*;
import ru.practicum.ewm.enums.RequestStatusUpdate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventRequestStatusUpdateRequest {
    private List<Long> requestIds;
    private RequestStatusUpdate status;
}
