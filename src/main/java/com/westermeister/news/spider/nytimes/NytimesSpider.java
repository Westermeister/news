package com.westermeister.news.spider.nytimes;

import java.io.IOException;
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
     * @return              list of stories
     * @throws IOException  if there's an error while calling the API, or if the API returns an unexpected response
     */
    public List<NytimesStory> getStories() throws IOException {
        List<NytimesStory> result = new ArrayList<>();
        String endpoint = String.format("https://api.nytimes.com/svc/topstories/v2/home.json?api-key=%s", apiKey);
        try {
            NytimesResponse response = restTemplate.getForObject(endpoint, NytimesResponse.class);
            if (response == null) {
                throw new IOException("Got null object after calling NYTimes API");
            }
            if (!response.status().equals("OK")) {
                throw new IOException(String.format("Got non-OK status from NYTimes API: %s%n", response.status()));
            }
            result = response.results();
            return result;
        } catch (RestClientException e) {
            throw new IOException("Failed call to NYTimes API", e);
        }
    }
}
