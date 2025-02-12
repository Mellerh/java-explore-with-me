package ru.practicum.ewm.stats.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.stats.EndpointHitDto;
import ru.practicum.ewm.dto.stats.mappers.StatsMapper;
import ru.practicum.ewm.dto.stats.ViewStats;
import ru.practicum.ewm.stats.repository.StatsRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImplStatsService implements StatsService {

    private final StatsRepository statsRepository;

    @Override
    public List<ViewStats> getStats(String start, String end, List<String> uris, boolean unique) {
        return statsRepository.getStats(StatsMapper.toRequestMapper(start, end, uris, unique));

    }

    @Override
    public void recordHit(EndpointHitDto endpointHit) {
        statsRepository.recordHit(endpointHit);
    }
}
