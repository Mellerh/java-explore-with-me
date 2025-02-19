package ru.practicum.ewm.dto.compilation;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Valid
public class NewCompilationDto {
    private Boolean pinned = false;
    @NotBlank
    @Size(max = 50)
    private String title;
    private Set<Long> events; // здесь id event
}
