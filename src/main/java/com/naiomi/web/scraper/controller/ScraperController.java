package com.naiomi.web.scraper.controller;

import com.naiomi.web.scraper.service.ScraperService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/scraper")
@RequiredArgsConstructor
public class ScraperController {

    private final ScraperService scraperService;

    @PostMapping
    public Map<String, List<String>> scrapeWebsites(List<String> urls) {
        return Map.of("content", scraperService.scrapeWebsites(urls));
    }
}
