package com.westermeister.news.spider.openai;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Domain class for response from OpenAI GPT API.
 *
 * @param choices  possible chat completions
 * @param usage    possible usage
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record OpenAiResponse(
    List<OpenAiChoice> choices,
    OpenAiUsage usage
) {}
