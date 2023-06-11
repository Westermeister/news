package com.westermeister.news.config;

import java.util.Base64;
import java.util.Base64.Encoder;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * A collection of "miscellaneous", standalone beans that don't have a more specific place to be defined.
 */
@Configuration
public class MiscBeanConfiguration {
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
