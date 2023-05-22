package com.westermeister.news.repository;

import org.springframework.data.repository.CrudRepository;

import com.westermeister.news.entity.User;

/**
 * CRUD interface for interacting with the user table.
 */
public interface UserRepository extends CrudRepository<User, Long> {
}
