package com.westermeister.news.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;

import com.westermeister.news.entity.Lock;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;

/**
 * CRUD interface for interacting with the lock table.
 */
public interface LockRepository extends CrudRepository<Lock, Long> {
    /**
     * Lock row by task name.
     *
     * @param task  name of the task
     */
    @org.springframework.data.jpa.repository.Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "jakarta.persistence.lock.timeout", value = "0")})
    Optional<Lock> findByTask(String task);
}
