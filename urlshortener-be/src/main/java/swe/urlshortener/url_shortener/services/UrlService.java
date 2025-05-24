package swe.urlshortener.url_shortener.services;

import org.springframework.stereotype.Service;
import swe.urlshortener.url_shortener.repositories.UrlMappingRepository;
import swe.urlshortener.url_shortener.models.UrlMapping;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;

@Service
public class UrlService {
    private UrlMappingRepository urlMappingRepository;

    public UrlService(UrlMappingRepository urlMappingRepository) {
        this.urlMappingRepository = urlMappingRepository;
    }

    private String generateShortUrl(String longUrl) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(longUrl.getBytes(StandardCharsets.UTF_8));
            String encoded = Base64.getUrlEncoder().encodeToString(hash);
            return encoded.substring(0, 8);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating short URL", e);
        }
    }

    public String shortenUrl(String longUrl) {
        var existingUrlMapping = urlMappingRepository.findByLongUrl(longUrl);
        if (existingUrlMapping.isPresent()) {
            return existingUrlMapping.get().getShortUrl();
        }

        String shortUrl = generateShortUrl(longUrl);
        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setLongUrl(longUrl);
        urlMapping.setShortUrl(shortUrl);
        urlMapping.setCreatedAt(new Date());

        urlMappingRepository.save(urlMapping);
        return shortUrl;
    }

    public String getLongUrl(String shortUrl) {
        var existingUrlMapping = urlMappingRepository.findByShortUrl(shortUrl);
        if(existingUrlMapping.isEmpty()){
            return null;
        }

        return existingUrlMapping.get().getLongUrl();
    }
}
