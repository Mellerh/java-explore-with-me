package ru.practicum.ewm.stats.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.ewm.dto.stats.EndpointHitDto;
import ru.practicum.ewm.dto.stats.ViewStats;
import ru.practicum.ewm.dto.stats.ViewStatsRequest;

import java.util.List;

@Repository
public interface StatsRepository {

    List<ViewStats> getStats(ViewStatsRequest viewStatsRequest);

    void recordHit(EndpointHitDto endpointHit);

}
