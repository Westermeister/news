package com.westermeister.news.repository;

import org.springframework.data.repository.CrudRepository;

import com.westermeister.news.entity.Session;

/**
 * CRUD interface for interacting with the session table.
 */
public interface SessionRepository extends CrudRepository<Session, Long> {
}
