package ru.practicum.ewm.stats.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Stats {

    private Long id;

    private String uri;

    private String ip;

    private LocalDateTime created;

}
