package com.westermeister.news.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Hibernate entity for distributed locks.
 *
 * @see "Lock.md" for schema docs
 */
@Entity
@Table(name = "lock")
public class Lock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "task")
    private String task;

    @Column(name = "created")
    private LocalDateTime created;

    protected Lock() {}

    /**
     * Construct a new distributed lock.
     *
     * @see "Lock.md" for schema docs
     */
    public Lock(String task, LocalDateTime created) {
        this.task = task;
        this.created = created;
    }

    @Override
    public String toString() {
        return "Lock [id=" + id + ", task=" + task + ", created=" + created + "]";
    }

    /**
     * @see "Lock.md" for schema docs
     */
    public Long getId() {
        return id;
    }

    /**
     * @see "Lock.md" for schema docs
     */
    public String getTask() {
        return task;
    }

    /**
     * @see "Lock.md" for schema docs
     */
    public void setTask(String task) {
        this.task = task;
    }

    /**
     * @see "Lock.md" for schema docs
     */
    public LocalDateTime getCreated() {
        return created;
    }

    /**
     * @see "Lock.md" for schema docs
     */
    public void setCreated(LocalDateTime created) {
        this.created = created;
    }
}
