package com.westermeister.news.schedule;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.westermeister.news.entity.Lock;
import com.westermeister.news.entity.Snippet;
import com.westermeister.news.repository.LockRepository;
import com.westermeister.news.repository.SnippetRepository;
import com.westermeister.news.spider.nytimes.NytimesSpider;
import com.westermeister.news.spider.nytimes.NytimesStory;
import com.westermeister.news.spider.openai.OpenAiSpider;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

/**
 * Responsible for refreshing the news snippets on the homepage.
 */
@Component
public class NewsUpdater {
    /**
     * VERY IMPORTANT: This number controls the number of news snippets displayed on the homepage.
     * It also controls the maximum number of (paid) API calls made to OpenAI for summarization.
     * <p>
     * As a side note, it's not actually the number of API calls that counts towards pricing,
     * but rather the "token" count. To find out more, see: https://openai.com/pricing
     * <p>
     * NOTE: Increasing this beyond ~25 has no effect, since the NYTimes API only returns about that many stories.
     */
    private final int MAX_SNIPPETS = 15;

    private final String taskName = "TASK_UPDATE_NEWS";

    private Logger logger = LoggerFactory.getLogger(NewsUpdater.class);

    private LockRepository lockRepo;

    private SnippetRepository snippetRepo;

    private NytimesSpider nytimesSpider;

    private OpenAiSpider openAiSpider;

    /**
     * Inject dependencies.
     *
     * @param lockRepo       used to access database table for distributed locks
     * @param snippetRepo    used to save news snippets to database
     * @param nytimesSpider  used to get top stories from NYTimes API
     * @param openAiSpider   used to summarize top stories from NYTimes API
     */
    public NewsUpdater(
        LockRepository lockRepo,
        SnippetRepository snippetRepo,
        NytimesSpider nytimesSpider,
        OpenAiSpider openAiSpider
    ) {
        this.lockRepo = lockRepo;
        this.snippetRepo = snippetRepo;
        this.nytimesSpider = nytimesSpider;
        this.openAiSpider = openAiSpider;
    }

    /**
     * Create the row that'll be used as a distributed lock to prevent multiple nodes from running this task.
     */
    @PostConstruct
    public void setUpLock() {
        try {
            LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
            Lock lock = new Lock(taskName, now);
            lockRepo.save(lock);
        } catch (DataIntegrityViolationException e) {}
    }

    /**
     * Twice a day: get top stories, summarize each of them, and update database.
     */
    @Scheduled(cron = "0 0 13,23 * * *")
    @Transactional
    public void updateNews() {
        logger.info("Starting scheduled task: {}", taskName);
        try {
            acquireLock();
            performUpdate();
            logger.info("Completed scheduled task: {}", taskName);
        } catch (Exception e) {
            throw new RuntimeException("Failed to execute scheduled task", e);
        }
    }

    private void acquireLock() throws IOException {
        try {
            logger.info("Acquiring lock...");
            lockRepo.findByTask(taskName);
            logger.info("Successfully acquired lock");
        } catch (PessimisticLockingFailureException e) {
            throw new IOException("Another node already acquired lock; aborting scheduled task", e);
        }
    }

    private void performUpdate() throws IOException, InterruptedException {
        // Get stories from NYTimes API.
        TimeUnit.SECONDS.sleep(12);
        List<NytimesStory> stories = nytimesSpider.getStories();
        while (stories.size() > MAX_SNIPPETS) {
            stories.remove(stories.size() - 1);
        }

        List<Snippet> newSnippets = new ArrayList<>();
        for (int i = 0; i < stories.size(); i++) {
            // Extract relevant story data.
            String headline = stories.get(i).headline();
            String abstractText = stories.get(i).abstractText();
            String source = stories.get(i).url();

            // Call OpenAI's API.
            TimeUnit.SECONDS.sleep(1);
            logger.info(
                "{} / {} Calling OpenAI with headline / abstract: {} / {}",
                i + 1,
                stories.size(),
                headline,
                abstractText
            );
            String summary = openAiSpider.getSummary(headline, abstractText);

            // Prepare new snippet object.
            LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
            Snippet snippet = new Snippet((short) i, summary, source, now);
            newSnippets.add(snippet);
        }
        snippetRepo.deleteAll();
        snippetRepo.flush();
        snippetRepo.saveAll(newSnippets);
    }
}
