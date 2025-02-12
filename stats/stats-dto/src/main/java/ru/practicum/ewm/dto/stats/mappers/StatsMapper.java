package ru.practicum.ewm.dto.stats.mappers;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.dto.stats.ViewStatsRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class StatsMapper {

    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static ViewStatsRequest toRequestMapper(String start, String end, List<String> uris, boolean unique) {
        return ViewStatsRequest.builder()
                .start(LocalDateTime.parse(start, timeFormatter))
                .end(LocalDateTime.parse(end, timeFormatter))
                .uris(uris)
                .unique(unique)
                .build();
    }

}
