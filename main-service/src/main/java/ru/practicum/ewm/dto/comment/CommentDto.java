package ru.practicum.ewm.dto.comment;


import jakarta.validation.Valid;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Valid
@Builder
public class CommentDto {

    private Long id;
    private String text;
    private String authorName;
    private Long eventId;
    private LocalDateTime created;

}
