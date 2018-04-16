package com.qigu.readword.service;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.qigu.readword.domain.Favorite;
import com.qigu.readword.domain.*; // for static metamodels
import com.qigu.readword.repository.FavoriteRepository;
import com.qigu.readword.repository.search.FavoriteSearchRepository;
import com.qigu.readword.service.dto.FavoriteCriteria;

import com.qigu.readword.service.dto.FavoriteDTO;
import com.qigu.readword.service.mapper.FavoriteMapper;

/**
 * Service for executing complex queries for Favorite entities in the database.
 * The main input is a {@link FavoriteCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FavoriteDTO} or a {@link Page} of {@link FavoriteDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FavoriteQueryService extends QueryService<Favorite> {

    private final Logger log = LoggerFactory.getLogger(FavoriteQueryService.class);


    private final FavoriteRepository favoriteRepository;

    private final FavoriteMapper favoriteMapper;

    private final FavoriteSearchRepository favoriteSearchRepository;

    public FavoriteQueryService(FavoriteRepository favoriteRepository, FavoriteMapper favoriteMapper, FavoriteSearchRepository favoriteSearchRepository) {
        this.favoriteRepository = favoriteRepository;
        this.favoriteMapper = favoriteMapper;
        this.favoriteSearchRepository = favoriteSearchRepository;
    }

    /**
     * Return a {@link List} of {@link FavoriteDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FavoriteDTO> findByCriteria(FavoriteCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Favorite> specification = createSpecification(criteria);
        return favoriteMapper.toDto(favoriteRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FavoriteDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FavoriteDTO> findByCriteria(FavoriteCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Favorite> specification = createSpecification(criteria);
        final Page<Favorite> result = favoriteRepository.findAll(specification, page);
        return result.map(favoriteMapper::toDto);
    }

    /**
     * Function to convert FavoriteCriteria to a {@link Specifications}
     */
    private Specifications<Favorite> createSpecification(FavoriteCriteria criteria) {
        Specifications<Favorite> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Favorite_.id));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getUserId(), Favorite_.user, User_.id));
            }
            if (criteria.getWordsId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getWordsId(), Favorite_.words, Word_.id));
            }
        }
        return specification;
    }

}
