package ru.practicum.ewm.errorHandler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class ApiError {
    private String message;
    private String reason;
    private String status;
    private String timestamp;
}
