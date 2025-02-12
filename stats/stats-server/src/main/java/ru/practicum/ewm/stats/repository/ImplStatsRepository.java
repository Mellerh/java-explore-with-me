package ru.practicum.ewm.stats.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.dto.stats.EndpointHitDto;
import ru.practicum.ewm.dto.stats.ViewStats;
import ru.practicum.ewm.dto.stats.ViewStatsRequest;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ImplStatsRepository implements StatsRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<ViewStats> getStats(ViewStatsRequest viewStatsRequest) {
        LocalDateTime start = viewStatsRequest.getStart();
        LocalDateTime end = viewStatsRequest.getEnd();

        List<String> urisList = (viewStatsRequest.getUris());
        boolean unique = viewStatsRequest.isUnique();

        if (urisList == null || urisList.isEmpty()) {

            if (unique) {
                String uniqueSqlRequest = "SELECT stats.app, stats.uri, COUNT(DISTINCT stats.ip) as hits " +
                        "FROM stats " +
                        "WHERE stats.created BETWEEN ?1 AND ?2 " +
                        "GROUP BY stats.app, stats.uri " +
                        "ORDER BY COUNT(DISTINCT stats.ip) DESC";

                List<ViewStats> viewStatsList = jdbcTemplate.query(
                        uniqueSqlRequest, (rs, rownUM) -> ViewStats.builder()
                                .app(rs.getString("app"))
                                .uri(rs.getString("uri"))
                                .hits(rs.getLong("hits")).build(),
                        start, end);

                return viewStatsList;

            } else {
                String notUniqueSqlRequest = "SELECT stats.app, stats.uri, COUNT(stats.ip) as hits " +
                        "FROM stats " +
                        "WHERE stats.created BETWEEN ?1 AND ?2 " +
                        "GROUP BY stats.app, stats.uri " +
                        "ORDER BY COUNT(stats.ip) DESC";

                List<ViewStats> viewStatsList = jdbcTemplate.query(
                        notUniqueSqlRequest, (rs, rownUM) -> ViewStats.builder()
                                .app(rs.getString("app"))
                                .uri(rs.getString("uri"))
                                .hits(rs.getLong("hits")).build(),
                        start, end);

                return viewStatsList;
            }
        } else {
            String stringOfUris = toStringFromList(urisList);

            if (unique) {

                String uniqueSqlRequestWithUriList = "SELECT stats.app, stats.uri, COUNT(DISTINCT stats.ip) as hits " +
                        "FROM stats " +
                        "WHERE stats.created BETWEEN ?1 AND ?2 AND (?3) like '%' || s.uri || ',%' " +
                        "GROUP BY stats.app, stats.uri " +
                        "ORDER BY COUNT(DISTINCT stats.ip) DESC";

                List<ViewStats> viewStatsList = jdbcTemplate.query(
                        uniqueSqlRequestWithUriList, (rs, rownUM) -> ViewStats.builder()
                                .app(rs.getString("app"))
                                .uri(rs.getString("uri"))
                                .hits(rs.getLong("hits")).build(),
                        start, end, stringOfUris);
                return viewStatsList;

            } else {
                String uniqueSqlRequestWithUriList = "SELECT stats.app, stats.uri, COUNT(stats.ip) as hits " +
                        "FROM stats " +
                        "WHERE stats.created BETWEEN ?1 AND ?2 AND (?3) like '%' || s.uri || ',%' " +
                        "GROUP BY stats.app, stats.uri " +
                        "ORDER BY COUNT(stats.ip) DESC";

                List<ViewStats> viewStatsList = jdbcTemplate.query(
                        uniqueSqlRequestWithUriList, (rs, rownUM) -> ViewStats.builder()
                                .app(rs.getString("app"))
                                .uri(rs.getString("uri"))
                                .hits(rs.getLong("hits")).build(),
                        start, end, stringOfUris);
                return viewStatsList;
            }

        }

    }

    @Override
    public void recordHit(EndpointHitDto endpointHit) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("stats")
                .usingGeneratedKeyColumns("id");

        jdbcInsert.executeAndReturnKey(endpointHit.toMap());

    }


    private String toStringFromList(List<String> urisList) {
        StringBuilder builder = new StringBuilder();
        for (String s : urisList) {
            builder.append(s).append(",");
        }
        return builder.toString();
    }
}
