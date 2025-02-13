package ru.practicum.ewm.dto.stats;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ViewStatsRequest {

    @NotNull
    private LocalDateTime start;
    @NotNull
    private LocalDateTime end;

    private List<String> uris;
    private boolean unique;

    @AssertTrue(message = "start не может идти после end")
    private boolean isDateValid() {
        return start.isBefore(end);
    }

}
