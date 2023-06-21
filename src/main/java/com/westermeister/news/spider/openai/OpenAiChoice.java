package com.westermeister.news.spider.openai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Domain class for OpenAI choice i.e. GPT chat response option.
 *
 * @param message  the message object response
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record OpenAiChoice(
    OpenAiMessage message
) {}
