package ru.practicum.ewm.dto.compilation;

import jakarta.validation.constraints.Size;
import lombok.*;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCompilationRequest {
    private Boolean pinned;
    @Size(min = 1, max = 50)
    private String title;
    private Set<Long> events; // здесь id event
}
