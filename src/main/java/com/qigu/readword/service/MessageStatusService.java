package com.qigu.readword.service;

import com.qigu.readword.service.dto.MessageStatusDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing MessageStatus.
 */
public interface MessageStatusService {

    /**
     * Save a messageStatus.
     *
     * @param messageStatusDTO the entity to save
     * @return the persisted entity
     */
    MessageStatusDTO save(MessageStatusDTO messageStatusDTO);

    /**
     * Get all the messageStatuses.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<MessageStatusDTO> findAll(Pageable pageable);

    /**
     * Get the "id" messageStatus.
     *
     * @param id the id of the entity
     * @return the entity
     */
    MessageStatusDTO findOne(Long id);

    /**
     * Delete the "id" messageStatus.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the messageStatus corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<MessageStatusDTO> search(String query, Pageable pageable);
}
