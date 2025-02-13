package ru.practicum.ewm.stats.service;

import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.stats.EndpointHitDto;
import ru.practicum.ewm.dto.stats.ViewStats;

import java.util.List;

@Service
public interface StatsService {

    List<ViewStats> getStats(String start, String end, List<String> uris, boolean unique);

    void recordHit(EndpointHitDto endpointHit);

}
