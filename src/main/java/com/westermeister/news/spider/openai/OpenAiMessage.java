package com.westermeister.news.spider.openai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Domain class for OpenAI message i.e. GPT chat response.
 *
 * @param role     the message type
 * @param content  the message from GPT
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record OpenAiMessage(
    String role,
    String content
) {}
