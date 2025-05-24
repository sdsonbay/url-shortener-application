package swe.urlshortener.url_shortener.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import swe.urlshortener.url_shortener.models.ShortenUrlRequest;
import swe.urlshortener.url_shortener.models.ShortenUrlResponse;
import swe.urlshortener.url_shortener.services.UrlService;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/url")
public class UrlController {
    private UrlService urlService;

    public UrlController(UrlService urlShortenerService) {
        this.urlService = urlShortenerService;
    }

    @PostMapping(value = "/shorten", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ShortenUrlResponse> shortenUrl(@RequestBody ShortenUrlRequest request) {
        var shortUrl = urlService.shortenUrl(request.url);
        var response = ShortenUrlResponse.builder()
                .shortUrl("http://localhost:8080/api/v1/url/to/" + shortUrl)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/to/{shortUrl}")
    public ResponseEntity<Void> redirect(@PathVariable String shortUrl) {
        var longUrl = urlService.getLongUrl(shortUrl);
        if(longUrl.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(longUrl));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
}
