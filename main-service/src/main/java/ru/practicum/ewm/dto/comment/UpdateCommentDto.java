package ru.practicum.ewm.dto.comment;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Valid
@Builder
public class UpdateCommentDto {

    @NotNull
    @NotBlank
    private String text;

}
