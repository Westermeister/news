package com.westermeister.news.spider.nytimes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Domain class for a NYTimes story.
 *
 * @param headline      the story headline
 * @param abstractText  short summary description of the story
 * @param url           url of the story
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record NytimesStory(
    @JsonProperty("title")
    String headline,
    @JsonProperty("abstract")
    String abstractText,
    String url
) {}
