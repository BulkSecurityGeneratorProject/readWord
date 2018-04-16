package com.qigu.readword.service;

import com.qigu.readword.service.dto.MessageContentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing MessageContent.
 */
public interface MessageContentService {

    /**
     * Save a messageContent.
     *
     * @param messageContentDTO the entity to save
     * @return the persisted entity
     */
    MessageContentDTO save(MessageContentDTO messageContentDTO);

    /**
     * Get all the messageContents.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<MessageContentDTO> findAll(Pageable pageable);

    /**
     * Get the "id" messageContent.
     *
     * @param id the id of the entity
     * @return the entity
     */
    MessageContentDTO findOne(Long id);

    /**
     * Delete the "id" messageContent.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the messageContent corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<MessageContentDTO> search(String query, Pageable pageable);
}
