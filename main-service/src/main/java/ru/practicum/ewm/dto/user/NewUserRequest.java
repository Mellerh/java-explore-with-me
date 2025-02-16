package ru.practicum.ewm.dto.user;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewUserRequest {

    @NotBlank
    @NotNull
    @Size(min = 2, max = 250)
    private String name;

    @NotNull
    @NotBlank
    @Email
    @Size(min = 6, max = 254)
    private String email;

}
