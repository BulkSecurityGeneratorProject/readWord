package com.qigu.readword.service;

import com.qigu.readword.service.dto.AudioDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Audio.
 */
public interface AudioService {

    /**
     * Save a audio.
     *
     * @param audioDTO the entity to save
     * @return the persisted entity
     */
    AudioDTO save(AudioDTO audioDTO);

    /**
     * Get all the audio.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<AudioDTO> findAll(Pageable pageable);

    /**
     * Get the "id" audio.
     *
     * @param id the id of the entity
     * @return the entity
     */
    AudioDTO findOne(Long id);

    /**
     * Delete the "id" audio.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the audio corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<AudioDTO> search(String query, Pageable pageable);
}
