package swe.urlshortener.url_shortener.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import swe.urlshortener.url_shortener.models.UrlMapping;

import java.util.Optional;

@Repository
public interface UrlMappingRepository extends JpaRepository<UrlMapping, Long> {
    Optional<UrlMapping> findByShortUrl(String shortUrl);
    Optional<UrlMapping> findByLongUrl(String longUrl);
}
