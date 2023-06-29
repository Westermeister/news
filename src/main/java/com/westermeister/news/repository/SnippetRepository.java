package com.westermeister.news.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.westermeister.news.entity.Snippet;

import java.util.List;
import java.util.Optional;

/**
 * CRUD interface for interacting with the snippet table.
 */
public interface SnippetRepository extends JpaRepository<Snippet, Long> {
    /**
     * Find snippet by its slot number.
     *
     * @param slot  slot number for the snipet
     * @return      list of snippets
     */
    Optional<Snippet> findFirstBySlot(Short slot);

    /**
     * Find snippets ordered by slot numbers.
     *
     * @return list of snippets by slot number in ascending order
     */
    List<Snippet> findAllByOrderBySlotAsc();
}
