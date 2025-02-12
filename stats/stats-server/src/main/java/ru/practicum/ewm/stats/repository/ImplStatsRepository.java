package ru.practicum.ewm.stats.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.dto.stats.EndpointHitDto;
import ru.practicum.ewm.dto.stats.ViewStats;
import ru.practicum.ewm.dto.stats.ViewStatsRequest;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ImplStatsRepository implements StatsRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<ViewStats> getStats(ViewStatsRequest viewStatsRequest) {
        return null;
    }

    @Override
    public void recordHit(EndpointHitDto endpointHit) {

    }
}
