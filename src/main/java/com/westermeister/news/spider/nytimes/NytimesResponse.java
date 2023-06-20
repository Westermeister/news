package com.westermeister.news.spider.nytimes;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Domain class for response from NYTimes Top Stories API.
 *
 * @param status      should be "OK" if everything went right
 * @param numResults  number of stories returned by the API
 * @param results     contains the stories
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record NytimesResponse(
    String status,
    Short numResults,
    List<NytimesStory> results
) {}
