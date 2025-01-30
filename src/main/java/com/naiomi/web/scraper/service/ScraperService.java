package com.naiomi.web.scraper.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ScraperService {

    private final HttpClient httpClient = HttpClient.newHttpClient();

    public List<String> scrapeWebsites(List<String> urls) {
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            return urls.stream()
                    .map(url -> executor.submit(() -> fetchContent(url)))
                    .map(future -> {
                        try {
                            return future.get();
                        } catch (Exception e) {
                            log.error("Error occurred while fetching content", e);
                            return null;
                        }
                    })
                    .collect(Collectors.toList());
        }
    }

    private String fetchContent(String url) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            log.info("Fetched: {} ({} chars)", url, response.body().length());
            return response.body().substring(0, Math.min(200, response.body().length())) + "..."; // Limit output
        } catch (Exception e) {
            log.error("Failed to fetch {}: {}", url, e.getMessage());
            return "Error fetching " + url;
        }
    }

}
