package ru.practicum.ewm.dto.comment;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Valid
@Builder
public class NewCommentDto {

    @NotNull
    @NotBlank
    private String text;

    @Builder.Default
    private LocalDateTime created = LocalDateTime.now();

}
