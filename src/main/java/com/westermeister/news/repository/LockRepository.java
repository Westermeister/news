package com.westermeister.news.repository;

import org.springframework.data.repository.CrudRepository;

import com.westermeister.news.entity.Lock;

/**
 * CRUD interface for interacting with the snippet table.
 */
public interface LockRepository extends CrudRepository<Lock, Long> {}
