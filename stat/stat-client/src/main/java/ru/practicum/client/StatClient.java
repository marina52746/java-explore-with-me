package ru.practicum.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class StatClient {
    final RestTemplate template;
    final String statUrl;

    public StatClient(RestTemplate template, String statUrl) {
        this.template = template;
        this.statUrl = statUrl;
    }

    public static void main(String[] args) {

    }
}

