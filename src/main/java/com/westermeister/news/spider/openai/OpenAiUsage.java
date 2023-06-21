package com.westermeister.news.spider.openai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Domain class for usage data for an API call to OpenAI.
 *
 * @param promptTokens      number of tokens in input
 * @param completionTokens  number of tokens in output
 * @param totalTokens       total tokens
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record OpenAiUsage(
    String promptTokens,
    String completionTokens,
    String totalTokens
) {}
