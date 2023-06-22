package com.westermeister.news;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Application entry point.
 */
@SpringBootApplication
@EnableScheduling
public class NewsApplication {
    /**
     * Start the Spring app.
     *
     * @param args  passed into Spring `run` method, otherwise no effect
     */
    public static void main(String[] args) {
        SpringApplication.run(NewsApplication.class, args);
    }
}
