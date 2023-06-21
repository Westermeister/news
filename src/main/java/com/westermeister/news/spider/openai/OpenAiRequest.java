package com.westermeister.news.spider.openai;

import java.util.List;

/**
 * Domain class for OpenAI requests.
 *
 * @param model        the model to use
 * @param messages     the messages to send
 * @param temperature  value from 0 to 1
 */
public record OpenAiRequest(
    String model,
    List<OpenAiMessage> messages,
    double temperature
) {}
