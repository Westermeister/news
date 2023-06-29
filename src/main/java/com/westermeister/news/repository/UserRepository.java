package com.westermeister.news.repository;

import org.springframework.data.repository.CrudRepository;

import com.westermeister.news.entity.User;
import java.util.List;

/**
 * CRUD interface for interacting with the user table.
 */
public interface UserRepository extends CrudRepository<User, Long> {
    /**
     * Find user by email.
     *
     * @param email  user's email
     * @return       list of users
     */
    List<User> findFirstByEmail(String email);
}
