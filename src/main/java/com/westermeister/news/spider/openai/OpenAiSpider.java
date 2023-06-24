package com.westermeister.news.spider.openai;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Interact with Open AI API.
 */
@Component
public class OpenAiSpider {
    @Value("${OPENAI_API_KEY:api_key_not_found}")
    private String apiKey;
    private RestTemplate restTemplate;
    private ObjectMapper objectMapper;

    /**
     * Inject dependencies.
     *
     * @param restTemplateBuilder  used to build object that calls API
     * @param objectMapper         used to serialize request body into JSON
     */
    public OpenAiSpider(RestTemplateBuilder restTemplateBuilder, ObjectMapper objectMapper) {
        this.restTemplate = restTemplateBuilder.build();
        this.objectMapper = objectMapper;
    }

    /**
     * Get summary from the API.
     *
     * @param headline      the news headline
     * @param abstractText  the abstract text for the corresponding article
     * @return              resulting summary
     * @throws IOException  if there was any problem related to calling the API
     */
    public String getSummary(String headline, String abstractText) throws IOException {
        String endpoint = "https://api.openai.com/v1/chat/completions";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        // Compose request body.
        List<OpenAiMessage> messages = new ArrayList<>();
        OpenAiMessage systemConfig = new OpenAiMessage(
            "system",
            "You will be given a news headline and an abstract of the corresponding article. "
            + "Write a factual summary in one sentence. Write the summary in a matter-of-fact tone, "
            + "preserving facts but removing any editorialized elements of the inputs. "
            + "Write the summary as if you were a journalist who's in charge of replacing "
            + "sensationalist text with factual text. "
            + "Try to stay close to the wording and vocabulary of the inputs."
        );
        OpenAiMessage chatInput = new OpenAiMessage(
            "user",
            String.format("Headline: %s\nAbstract: %s", headline, abstractText)
        );
        messages.add(systemConfig);
        messages.add(chatInput);
        OpenAiRequest requestObject = new OpenAiRequest("gpt-3.5-turbo", messages, 0);
        String requestBody;
        try {
            requestBody = objectMapper.writeValueAsString(requestObject);
        } catch (JsonProcessingException e) {
            throw new IOException("Could not serialize OpenAI request body to JSON", e);
        }

        // Send API request.
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        try {
            OpenAiResponse response = restTemplate.postForObject(endpoint, entity, OpenAiResponse.class);
            if (response == null) {
                throw new IOException("Got null object for response to OpenAI API request");
            }
            List<OpenAiChoice> choices = response.choices();
            if (choices.size() == 0) {
                throw new IOException("Received empty choices-array for OpenAI response");
            }
            OpenAiMessage message = choices.get(0).message();
            String summary = message.content();
            return summary;
        } catch (RestClientException e) {
            throw new IOException("Failed to call OpenAI API endpoint", e);
        }
    }
}
