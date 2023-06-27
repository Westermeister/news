package com.westermeister.news.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Hibernate entity for news snippets.
 *
 * @see "Snippet.md" for schema docs
 */
@Entity
@Table(name = "snippet")
public class Snippet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "slot")
    private Short slot;

    @Column(name = "summary")
    private String summary;

    @Column(name = "source")
    private String source;

    @Column(name = "created")
    private LocalDateTime created;

    protected Snippet() {}

    /**
     * Construct a new news snippet.
     *
     * @see "Snippet.md" for schema docs
     */
    public Snippet(Short slot, String summary, String source, LocalDateTime created) {
        this.slot = slot;
        this.summary = summary;
        this.source = source;
        this.created = created;
    }

    @Override
    public String toString() {
        return "Snippet [id=" + id + ", slot=" + slot + ", summary=" + summary + ", source=" + source + ", created="
                + created + "]";
    }

    /**
     * @see "Snippet.md" for schema docs
     */
    public Long getId() {
        return id;
    }

    /**
     * @see "Snippet.md" for schema docs
     */
    public Short getSlot() {
        return slot;
    }

    /**
     * @see "Snippet.md" for schema docs
     */
    public void setSlot(Short slot) {
        this.slot = slot;
    }

    /**
     * @see "Snippet.md" for schema docs
     */
    public String getSummary() {
        return summary;
    }

    /**
     * @see "Snippet.md" for schema docs
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     * @see "Snippet.md" for schema docs
     */
    public String getSource() {
        return source;
    }

    /**
     * @see "Snippet.md" for schema docs
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * @see "Snippet.md" for schema docs
     */
    public LocalDateTime getCreated() {
        return created;
    }

    /**
     * @see "Snippet.md" for schema docs
     */
    public void setCreated(LocalDateTime created) {
        this.created = created;
    }
}
