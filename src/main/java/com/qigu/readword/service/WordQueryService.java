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

import com.qigu.readword.domain.Word;
import com.qigu.readword.domain.*; // for static metamodels
import com.qigu.readword.repository.WordRepository;
import com.qigu.readword.repository.search.WordSearchRepository;
import com.qigu.readword.service.dto.WordCriteria;

import com.qigu.readword.service.dto.WordDTO;
import com.qigu.readword.service.mapper.WordMapper;
import com.qigu.readword.domain.enumeration.LifeStatus;

/**
 * Service for executing complex queries for Word entities in the database.
 * The main input is a {@link WordCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link WordDTO} or a {@link Page} of {@link WordDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class WordQueryService extends QueryService<Word> {

    private final Logger log = LoggerFactory.getLogger(WordQueryService.class);


    private final WordRepository wordRepository;

    private final WordMapper wordMapper;

    private final WordSearchRepository wordSearchRepository;

    public WordQueryService(WordRepository wordRepository, WordMapper wordMapper, WordSearchRepository wordSearchRepository) {
        this.wordRepository = wordRepository;
        this.wordMapper = wordMapper;
        this.wordSearchRepository = wordSearchRepository;
    }

    /**
     * Return a {@link List} of {@link WordDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<WordDTO> findByCriteria(WordCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Word> specification = createSpecification(criteria);
        return wordMapper.toDto(wordRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link WordDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<WordDTO> findByCriteria(WordCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Word> specification = createSpecification(criteria);
        final Page<Word> result = wordRepository.findAll(specification, page);
        return result.map(wordMapper::toDto);
    }

    /**
     * Function to convert WordCriteria to a {@link Specifications}
     */
    private Specifications<Word> createSpecification(WordCriteria criteria) {
        Specifications<Word> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Word_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Word_.name));
            }
            if (criteria.getRank() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRank(), Word_.rank));
            }
            if (criteria.getLifeStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getLifeStatus(), Word_.lifeStatus));
            }
            if (criteria.getImgId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getImgId(), Word_.img, Image_.id));
            }
            if (criteria.getAudioId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getAudioId(), Word_.audio, Audio_.id));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getUserId(), Word_.user, User_.id));
            }
            if (criteria.getWordGroupId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getWordGroupId(), Word_.wordGroup, WordGroup_.id));
            }
            if (criteria.getFavoritesId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getFavoritesId(), Word_.favorites, Favorite_.id));
            }
        }
        return specification;
    }

}
