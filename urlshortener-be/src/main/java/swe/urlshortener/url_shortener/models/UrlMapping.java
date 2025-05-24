package swe.urlshortener.url_shortener.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "url_mappings")
public class UrlMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "short_url")
    private String shortUrl;
    @Column(name = "long_url")
    private String longUrl;
    @Column(name = "created_at")
    private Date createdAt;
}