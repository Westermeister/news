package com.westermeister.news.schedule;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.westermeister.news.repository.LockRepository;
import com.westermeister.news.repository.SnippetRepository;
import com.westermeister.news.spider.nytimes.NytimesSpider;
import com.westermeister.news.spider.nytimes.NytimesStory;
import com.westermeister.news.spider.openai.OpenAiSpider;

@SpringBootTest
public class NewsUpdaterTests {
    @MockBean
    private NytimesSpider nytimesSpider;

    @MockBean
    private OpenAiSpider openAiSpider;

    @Autowired
    private LockRepository lockRepo;

    @Autowired
    private SnippetRepository snippetRepo;

    @Autowired
    private NewsUpdater newsUpdater;

    @Test
    void testUpdateNews() throws IOException {
        // Mock dependencies.
        List<NytimesStory> mockStories = new ArrayList<>();
        mockStories.add(new NytimesStory("headline_1", "abstract_1", "url_1"));
        mockStories.add(new NytimesStory("headline_2", "abstract_2", "url_2"));
        when(nytimesSpider.getStories()).thenReturn(mockStories);
        when(openAiSpider.getSummary("headline_1", "abstract_1")).thenReturn("summary_1");
        when(openAiSpider.getSummary("headline_2", "abstract_2")).thenReturn("summary_2");

        // Run test.
        newsUpdater.updateNews();

        // Verify result.
        long numLocks = lockRepo.count();
        assertEquals(numLocks, 1);
        long numSnippets = snippetRepo.count();
        assertEquals(numSnippets, 2);
    }
}
