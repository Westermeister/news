package com.westermeister.news.repository;

import org.springframework.data.repository.CrudRepository;

import com.westermeister.news.entity.Snippet;

import java.util.List;

/**
 * CRUD interface for interacting with the snippet table.
 */
public interface SnippetRepository extends CrudRepository<Snippet, Long> {
    /**
     * Find snippet by its slot number.
     *
     * @param slot  slot number for the snipet
     * @return      list of snippets
     */
    List<Snippet> findFirstBySlot(Short slot);
}
