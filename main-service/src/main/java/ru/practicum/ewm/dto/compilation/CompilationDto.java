package ru.practicum.ewm.dto.compilation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.dto.event.EventShortDto;

import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompilationDto {

    private Long id;
    private Boolean pinned;
    private String title;
    private Set<EventShortDto> events;

}
