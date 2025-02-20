package ru.practicum.ewm.dto.stats;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EndpointHitDto {

    @NotBlank
    private String app;

    @NotBlank
    private String uri;

    @NotBlank
    private String ip;

    private LocalDateTime created = LocalDateTime.now();

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("app", app);
        values.put("ip", ip);
        values.put("created", created);
        values.put("uri", uri);

        return values;
    }

}

