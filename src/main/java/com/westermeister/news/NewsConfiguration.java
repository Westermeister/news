package com.westermeister.news;

import java.util.Base64;
import java.util.Base64.Encoder;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Provides manual bean definitions for classes from built-in / third-party libraries.
 */
@Configuration
public class NewsConfiguration {
    /**
     * Bean definition for a URL-safe Base64 encoder without padding.
     *
     * @return the encoder object
     */
    @Bean
    Encoder encoder() {
        return Base64.getUrlEncoder().withoutPadding();
    }
}
