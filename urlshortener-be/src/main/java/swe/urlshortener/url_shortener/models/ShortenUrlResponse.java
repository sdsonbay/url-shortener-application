package swe.urlshortener.url_shortener.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShortenUrlResponse {
    public String shortUrl;
}
