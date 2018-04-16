package com.qigu.readword.service.impl;

import com.qigu.readword.service.FavoriteService;
import com.qigu.readword.domain.Favorite;
import com.qigu.readword.repository.FavoriteRepository;
import com.qigu.readword.repository.search.FavoriteSearchRepository;
import com.qigu.readword.service.dto.FavoriteDTO;
import com.qigu.readword.service.mapper.FavoriteMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Favorite.
 */
@Service
@Transactional
public class FavoriteServiceImpl implements FavoriteService {

    private final Logger log = LoggerFactory.getLogger(FavoriteServiceImpl.class);

    private final FavoriteRepository favoriteRepository;

    private final FavoriteMapper favoriteMapper;

    private final FavoriteSearchRepository favoriteSearchRepository;

    public FavoriteServiceImpl(FavoriteRepository favoriteRepository, FavoriteMapper favoriteMapper, FavoriteSearchRepository favoriteSearchRepository) {
        this.favoriteRepository = favoriteRepository;
        this.favoriteMapper = favoriteMapper;
        this.favoriteSearchRepository = favoriteSearchRepository;
    }

    /**
     * Save a favorite.
     *
     * @param favoriteDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public FavoriteDTO save(FavoriteDTO favoriteDTO) {
        log.debug("Request to save Favorite : {}", favoriteDTO);
        Favorite favorite = favoriteMapper.toEntity(favoriteDTO);
        favorite = favoriteRepository.save(favorite);
        FavoriteDTO result = favoriteMapper.toDto(favorite);
        favoriteSearchRepository.save(favorite);
        return result;
    }

    /**
     * Get all the favorites.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<FavoriteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Favorites");
        return favoriteRepository.findAll(pageable)
            .map(favoriteMapper::toDto);
    }

    /**
     * Get one favorite by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public FavoriteDTO findOne(Long id) {
        log.debug("Request to get Favorite : {}", id);
        Favorite favorite = favoriteRepository.findOneWithEagerRelationships(id);
        return favoriteMapper.toDto(favorite);
    }

    /**
     * Delete the favorite by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Favorite : {}", id);
        favoriteRepository.delete(id);
        favoriteSearchRepository.delete(id);
    }

    /**
     * Search for the favorite corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<FavoriteDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Favorites for query {}", query);
        Page<Favorite> result = favoriteSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(favoriteMapper::toDto);
    }
}
