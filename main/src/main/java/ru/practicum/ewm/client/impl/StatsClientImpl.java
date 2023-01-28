package ru.practicum.ewm.client.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.ewm.client.StatsClient;
import ru.practicum.ewm.client.dto.RequestHitDto;
import ru.practicum.ewm.client.dto.ResponseStatsDto;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatsClientImpl implements StatsClient {

    protected final RestTemplate rest;
    protected final ObjectMapper objectMapper;

    private static final String APP_NAME = "ewm-main-service";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public StatsClientImpl(@Value("${stats-server.url}")String serverUrl, RestTemplateBuilder builder,
                           ObjectMapper objectMapper) {
        this.rest = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
        this.objectMapper = objectMapper;
    }

    @Override
    public void createHit(String uri, String ip) {
        var path = "/hit";
        var hit = new RequestHitDto(APP_NAME, uri, ip, LocalDateTime.now());
        HttpEntity<Object> requestEntity = new HttpEntity<>(hit, defaultHeaders());
        rest.exchange(path, HttpMethod.POST, requestEntity, Object.class);
    }

    @Override
    public List<ResponseStatsDto> findUniqueIpStats(LocalDateTime start, LocalDateTime end, String uris) {
        var path = "/stats?start={start}&end={end}&uris={uris}&unique={unique}";
        Map<String, Object> parameters = Map.of(
                "start", URLEncoder.encode(start.format(DATE_TIME_FORMATTER), StandardCharsets.UTF_8),
                "end", URLEncoder.encode(end.format(DATE_TIME_FORMATTER), StandardCharsets.UTF_8),
                "uris", uris,
                "unique", true
        );
        return sendStatsRequest(path, parameters);
    }

    @Override
    public List<ResponseStatsDto> findAllIpStats(LocalDateTime start, LocalDateTime end, String uris) {
        var path = "/stats?start={start}&end={end}&uris={uris}";
        Map<String, Object> parameters = Map.of(
                "start", URLEncoder.encode(start.format(DATE_TIME_FORMATTER), StandardCharsets.UTF_8),
                "end", URLEncoder.encode(end.format(DATE_TIME_FORMATTER), StandardCharsets.UTF_8),
                "uris", uris
        );
        return sendStatsRequest(path, parameters);
    }

    private List<ResponseStatsDto> sendStatsRequest(String path, Map<String, Object> parameters) {
        ResponseEntity<Object[]> response = rest.getForEntity(path, Object[].class, parameters);
        Object[] objects = response.getBody();
        if (objects != null) {
            return Arrays.stream(objects)
                    .map(object -> objectMapper.convertValue(object, ResponseStatsDto.class))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }

}
