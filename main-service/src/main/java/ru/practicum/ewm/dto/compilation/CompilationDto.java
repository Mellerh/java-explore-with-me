package ru.practicum.ewm.dto.compilation;

import lombok.*;
import ru.practicum.ewm.dto.event.EventShortDto;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompilationDto {
    private Long id;
    private Boolean pinned;
    private String title;
    private Set<EventShortDto> events; // здесь сами event
}
