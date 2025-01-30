package com.naiomi.web.scraper.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
 class ScraperServiceTest {

    @Mock
    private HttpClient mockHttpClient;

    @Mock
    private HttpResponse<String> mockResponse;

    @InjectMocks
    private ScraperService scraperService;

    @BeforeEach
    void setUp() throws Exception {
        when(mockResponse.body()).thenReturn("<html><body>Sample Content</body></html>");
        when(mockResponse.statusCode()).thenReturn(200);

        // Mock HttpClient behavior
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);
    }

    @Test
    void testScrapeWebsites_Success() {
        List<String> urls = List.of("https://example.com", "https://spring.io");

        List<String> results = scraperService.scrapeWebsites(urls);

        assertNotNull(results);
        assertEquals(2, results.size());
        assertTrue(results.getFirst().contains("Sample Content"));
    }

    @Test
    void testScrapeWebsites_FailedRequest() throws Exception {
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenThrow(new RuntimeException("Network error"));

        List<String> urls = List.of("https://example.com");
        List<String> results = scraperService.scrapeWebsites(urls);

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("Error fetching https://example.com", results.getFirst());
    }

    @Test
    void testScrapeWebsites_EmptyList() {
        List<String> results = scraperService.scrapeWebsites(List.of());

        assertNotNull(results);
        assertEquals(0, results.size());
    }
}
