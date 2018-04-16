package com.qigu.readword.service;

import com.qigu.readword.service.dto.FavoriteDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Favorite.
 */
public interface FavoriteService {

    /**
     * Save a favorite.
     *
     * @param favoriteDTO the entity to save
     * @return the persisted entity
     */
    FavoriteDTO save(FavoriteDTO favoriteDTO);

    /**
     * Get all the favorites.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<FavoriteDTO> findAll(Pageable pageable);

    /**
     * Get the "id" favorite.
     *
     * @param id the id of the entity
     * @return the entity
     */
    FavoriteDTO findOne(Long id);

    /**
     * Delete the "id" favorite.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the favorite corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<FavoriteDTO> search(String query, Pageable pageable);
}
