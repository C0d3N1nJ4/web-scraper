package com.naiomi.web.scraper.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.StructuredTaskScope;

@Service
@Slf4j
public class ScraperService {

    private final HttpClient httpClient;

    public ScraperService(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public List<String> scrapeWebsites(List<String> urls) {
        if (urls.isEmpty()) {
            return List.of();
        }

        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            List<StructuredTaskScope.Subtask<String>> tasks = urls.stream()
                    .map(url -> scope.fork(() -> fetchContent(url)))
                    .toList();

            scope.join();
            scope.throwIfFailed();

            return tasks.stream()
                    .map(StructuredTaskScope.Subtask::get)
                    .toList();
        } catch (Exception e) {
            log.error("Structured concurrency error: {}", e.getMessage(), e);
            return List.of("Error occurred during scraping");
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
            return STR."\{response.body().substring(0, Math.min(200, response.body().length()))}..."; // Limit output
        } catch (Exception e) {
            log.error("Failed to fetch {}: {}", url, e.getMessage());
            return STR."Error fetching content from \{url}";
        }
    }
}
