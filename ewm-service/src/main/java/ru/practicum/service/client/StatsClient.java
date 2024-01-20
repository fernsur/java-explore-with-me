package ru.practicum.service.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ru.practicum.dto.EndpointHit;

@Service
@RequiredArgsConstructor
public class StatsClient {
    @Value("${stats-server.url}")
    private String statUrl;
    private final RestTemplate restTemplate = new RestTemplate();

    public void createHit(EndpointHit hit) {
        HttpEntity<EndpointHit> request = new HttpEntity<EndpointHit>(hit);
        restTemplate.postForObject(statUrl + "/hit", request, String.class);
    }
}
