package ru.practicum.ewm.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.enums.RequestStatusUpdate;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventRequestStatusUpdateRequest {

   private List<Long> requestIds;
   private RequestStatusUpdate status;

}
