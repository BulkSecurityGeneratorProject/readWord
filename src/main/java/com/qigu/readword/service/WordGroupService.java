package com.qigu.readword.service;

import com.qigu.readword.service.dto.WordGroupDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing WordGroup.
 */
public interface WordGroupService {

    /**
     * Save a wordGroup.
     *
     * @param wordGroupDTO the entity to save
     * @return the persisted entity
     */
    WordGroupDTO save(WordGroupDTO wordGroupDTO);

    /**
     * Get all the wordGroups.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<WordGroupDTO> findAll(Pageable pageable);

    /**
     * Get the "id" wordGroup.
     *
     * @param id the id of the entity
     * @return the entity
     */
    WordGroupDTO findOne(Long id);

    /**
     * Delete the "id" wordGroup.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the wordGroup corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<WordGroupDTO> search(String query, Pageable pageable);
}
