package ru.practicum.ewm.stats.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.ewm.dto.stats.EndpointHitDto;
import ru.practicum.ewm.dto.stats.ViewStats;
import ru.practicum.ewm.dto.stats.ViewStatsRequest;

import java.util.List;

@Repository
public class ImplStatsRepository implements StatsRepository {
    @Override
    public List<ViewStats> getStats(ViewStatsRequest viewStatsRequest) {
        return null;
    }

    @Override
    public void recordHit(EndpointHitDto endpointHit) {

    }
}
