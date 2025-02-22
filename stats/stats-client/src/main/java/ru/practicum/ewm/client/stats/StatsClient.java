package ru.practicum.ewm.client.stats;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.stats.EndpointHitDto;
import ru.practicum.ewm.dto.stats.ViewStats;
import ru.practicum.ewm.dto.stats.ViewStatsRequest;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class StatsClient {
    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final String application;

    private final String statsServiceUri;

    private final ObjectMapper json;

    private final HttpClient httpClient;

    public StatsClient(@Value("ewm-main-service") String application,
                       @Value("${STATS_SERVER_URL:http://stats-server:9090}") String statsServiceUri,
                       ObjectMapper json) {
        this.application = application;
        this.statsServiceUri = statsServiceUri;
        this.json = json;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(2))
                .build();
    }

    public void hit(String userIp, String requestUri) {
        EndpointHitDto hit = EndpointHitDto.builder()
                .app(application)
                .ip(userIp)
                .uri(requestUri)
                .created(LocalDateTime.now())
                .build();

        log.info("StatsClient / hit: {}", hit.toString());

        try {
            HttpRequest.BodyPublisher bodyPublisher = HttpRequest
                    .BodyPublishers
                    .ofString(json.writeValueAsString(hit));

            // формируем запрос
            HttpRequest hitRequest = HttpRequest.newBuilder()
                    .uri(URI.create(statsServiceUri + "/hit"))
                    .POST(bodyPublisher)
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .header(HttpHeaders.ACCEPT, "application/json")
                    .build();

            log.info("StatsClient / hitRequest: {}", hitRequest.toString());

            // отправляем сформированный запрос
            HttpResponse<Void> response = httpClient.send(hitRequest, HttpResponse.BodyHandlers.discarding());
            log.debug("Response from stats-service: {}", response);
        } catch (Exception e) {
            log.warn("Cannot record hit", e);
        }
    }

    public List<ViewStats> getStats(ViewStatsRequest request) {
        try {
            String queryString = toQueryString(request);

            // полученную строку вставляем в запрос
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(statsServiceUri + "/stats" + queryString))
                    .header(HttpHeaders.ACCEPT, "application/json")
                    .build();

            // отправляем сформированный запрос
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            if (HttpStatus.valueOf(response.statusCode()).is2xxSuccessful()) {
                return json.readValue(response.body(), new TypeReference<>() {
                });
            }

            log.debug("Response from stats-service: {}", response);
        } catch (Exception e) {
            log.warn("Cannot get view stats from request: " + request, e);
        }
        return Collections.emptyList();
    }

    // преобразуем dto в строку запроса
    private String toQueryString(ViewStatsRequest request) {
        String start = encode(DTF.format(request.getStart()));
        String end = encode(DTF.format(request.getEnd()));

        String queryString = String.format("?start=%s&end=%s",
                start, end);

        if (request.getUris() != null && !request.getUris().isEmpty()) {
            queryString += "&uris=" + String.join(",", request.getUris());
        }

        queryString += String.format("&unique=%b", request.isUnique());

        return queryString;
    }

    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}