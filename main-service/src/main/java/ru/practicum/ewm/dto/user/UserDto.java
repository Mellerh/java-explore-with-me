package ru.practicum.ewm.dto.user;

import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;


@Getter
@Setter
@AllArgsConstructor
public class UserDto {
    private Long id;
    @Size(min = 2, max = 250)
    private String name;
    @Size(min = 2, max = 250)
    private String email;
}
