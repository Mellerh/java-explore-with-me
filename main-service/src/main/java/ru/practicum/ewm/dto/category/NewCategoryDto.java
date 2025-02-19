package ru.practicum.ewm.dto.category;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Valid
public class NewCategoryDto {

    @NotNull
    @NotBlank
//    @Size(min = 1, max = 50)
    private String name;

}
