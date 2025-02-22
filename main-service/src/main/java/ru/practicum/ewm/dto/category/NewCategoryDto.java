package ru.practicum.ewm.dto.category;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Valid
public class NewCategoryDto {
    @NotNull
    @NotBlank
    @Size(min = 1, max = 50)
    private String name;
}
