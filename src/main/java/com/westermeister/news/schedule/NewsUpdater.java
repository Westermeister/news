package com.westermeister.news.schedule;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
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

@Component
public class NewsUpdater {
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
     * Thrice a day: get top stories, summarize each of them, and update database.
     */
    // @Scheduled(cron = "0 0 0,8,16 * * *")
    @Scheduled(cron = "*/59 * * * * *")
    @Transactional
    public void updateNews() {
        logger.info("Starting scheduled task: {}", taskName);
        try {
            acquireLock();
            performUpdate();
            logger.info("Completed scheduled task: {}", taskName);
        } catch (Exception e) {
            logger.error("Failed to execute task {}; got below error:", taskName);
            e.printStackTrace();
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
        while (stories.size() > 10) {
            stories.remove(stories.size() - 1);
        }

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

            // Save result to database.
            Snippet existingSnippet = snippetRepo.findFirstBySlot((short) i).orElse(null);
            LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
            if (existingSnippet == null) {
                Snippet snippet = new Snippet((short) i, summary, source, now);
                snippetRepo.save(snippet);
            } else {
                existingSnippet.setSummary(summary);
                existingSnippet.setSource(source);
                existingSnippet.setLastUpdated(now);
                snippetRepo.save(existingSnippet);
            }
        }
    }
}
