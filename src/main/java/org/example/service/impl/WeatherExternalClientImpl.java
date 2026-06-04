package org.example.service.impl;

import lombok.Data;
import org.example.service.IWeatherExternalClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;

@Service
public class WeatherExternalClientImpl implements IWeatherExternalClient {
    private final RestTemplate restTemplate;
    private final String baseUrl;
    private final String currentParams;

    public WeatherExternalClientImpl(
            RestTemplateBuilder builder,
            @Value("${api.weather.provider.base-url}") String baseUrl,
            @Value("${api.weather.provider.current-params}") String currentParams
    ) {
        this.restTemplate = builder.build();
        this.baseUrl = baseUrl;
        this.currentParams = currentParams;
    }

    @Override
    public Map<String, Object> fetchCurrent(double lat, double lon) {
        String url = UriComponentsBuilder
                .fromHttpUrl(baseUrl)
                .path("/v1/forecast")
                .queryParam("latitude", lat)
                .queryParam("longitude", lon)
                .queryParam("current", currentParams)
                .toUriString();

        OpenMeteoCurrentResponse resp =
                restTemplate.getForObject(url, OpenMeteoCurrentResponse.class);

        if (resp == null || resp.getCurrent() == null) {
            return Collections.emptyMap();
        }
        return resp.getCurrent();
    }

    @Data
    public static class OpenMeteoCurrentResponse {
        private Map<String, Object> current;
    }
}

