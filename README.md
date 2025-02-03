# Spring Boot Web Scraper with Project Loom

#### Overview

This is a high-performance web scraper built using Spring Boot and Project Loom. It utilizes virtual threads for concurrent HTTP requests, making it efficient and scalable.

#### Features

- Uses Project Loom (Virtual Threads) for non-blocking concurrent scraping.

- Implements Structured Concurrency to manage multiple requests cleanly.

- Uses Scoped Values for efficient metadata tracking (e.g. request IDs).

- Provides a REST API to scrape multiple websites at once.

#### Technologies Used

- Java 21 (for Project Loom support)

- Spring Boot 3.4+

- HTTP Client (java.net.http)

- JUnit 5 & Mockito (for testing)
