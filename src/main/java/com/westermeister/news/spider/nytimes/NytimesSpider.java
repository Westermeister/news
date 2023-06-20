package com.westermeister.news.spider.nytimes;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Interact with New York Times API.
 */
@Component
public class NytimesSpider {
    @Value("${NYTIMES_API_KEY:api_key_not_found}")
    private String apiKey;
    private RestTemplate restTemplate;

    /**
     * Inject dependencies.
     *
     * @param restTemplateBuilder  used to build an object to call the API
     */
    public NytimesSpider(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    /**
     * Get stories from the API.
     *
     * @return list of stories; if error, will be empty
     */
    public List<NytimesStory> getStories() {
        List<NytimesStory> result = new ArrayList<>();
        String endpoint = String.format("https://api.nytimes.com/svc/topstories/v2/home.json?api-key=%s", apiKey);
        try {
            NytimesResponse response = restTemplate.getForObject(endpoint, NytimesResponse.class);
            if (response == null || !response.status().equals("OK")) return result;
            result = response.results();
            return result;
        } catch (RestClientException e) {
            System.err.format("Received error while calling NYTimes API: %s%n", e);
            return result;
        }
    }
}
