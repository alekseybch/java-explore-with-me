package ru.practicum.ewm.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.ewm.StatsClient;
import ru.practicum.ewm.dto.EndpointHit;
import ru.practicum.ewm.dto.StatsParameterDto;
import ru.practicum.ewm.dto.ViewStats;

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
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final String appName;

    @Autowired
    public StatsClientImpl(@Value("${stats-server.url}")String serverUrl,
                           @Value("${application.name}")String appName,
                           RestTemplateBuilder builder,
                           ObjectMapper objectMapper) {
        this.rest = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
        this.objectMapper = objectMapper;
        this.appName = appName;
    }

    @Override
    public void createHit(String uri, String ip) {
        var path = "/hit";
        var hit = new EndpointHit(appName, uri, ip, LocalDateTime.now());
        HttpEntity<Object> requestEntity = new HttpEntity<>(hit, defaultHeaders());
        rest.exchange(path, HttpMethod.POST, requestEntity, Object.class);
    }

    @Override
    public List<ViewStats> findUniqueIpStats(StatsParameterDto paramDto) {
        var path = "/stats?start={start}&end={end}&uris={uris}&unique={unique}";
        Map<String, Object> parameters = Map.of(
                "start", URLEncoder.encode(paramDto.getStartDate().format(DATE_TIME_FORMATTER), StandardCharsets.UTF_8),
                "end", URLEncoder.encode(paramDto.getEndDate().format(DATE_TIME_FORMATTER), StandardCharsets.UTF_8),
                "uris", paramDto.getUris(),
                "unique", true
        );
        return sendStatsRequest(path, parameters);
    }

    @Override
    public List<ViewStats> findAllIpStats(StatsParameterDto paramDto) {
        var path = "/stats?start={start}&end={end}&uris={uris}";
        Map<String, Object> parameters = Map.of(
                "start", URLEncoder.encode(paramDto.getStartDate().format(DATE_TIME_FORMATTER), StandardCharsets.UTF_8),
                "end", URLEncoder.encode(paramDto.getEndDate().format(DATE_TIME_FORMATTER), StandardCharsets.UTF_8),
                "uris", paramDto.getUris()
        );
        return sendStatsRequest(path, parameters);
    }

    private List<ViewStats> sendStatsRequest(String path, Map<String, Object> parameters) {
        ResponseEntity<Object[]> response = rest.getForEntity(path, Object[].class, parameters);
        Object[] objects = response.getBody();
        if (objects != null) {
            return Arrays.stream(objects)
                    .map(object -> objectMapper.convertValue(object, ViewStats.class))
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
