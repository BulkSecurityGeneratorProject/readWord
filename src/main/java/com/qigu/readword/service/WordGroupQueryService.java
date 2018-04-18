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

import com.qigu.readword.domain.WordGroup;
import com.qigu.readword.domain.*; // for static metamodels
import com.qigu.readword.repository.WordGroupRepository;
import com.qigu.readword.repository.search.WordGroupSearchRepository;
import com.qigu.readword.service.dto.WordGroupCriteria;

import com.qigu.readword.service.dto.WordGroupDTO;
import com.qigu.readword.service.mapper.WordGroupMapper;
import com.qigu.readword.domain.enumeration.LifeStatus;

/**
 * Service for executing complex queries for WordGroup entities in the database.
 * The main input is a {@link WordGroupCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link WordGroupDTO} or a {@link Page} of {@link WordGroupDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class WordGroupQueryService extends QueryService<WordGroup> {

    private final Logger log = LoggerFactory.getLogger(WordGroupQueryService.class);


    private final WordGroupRepository wordGroupRepository;

    private final WordGroupMapper wordGroupMapper;

    private final WordGroupSearchRepository wordGroupSearchRepository;

    public WordGroupQueryService(WordGroupRepository wordGroupRepository, WordGroupMapper wordGroupMapper, WordGroupSearchRepository wordGroupSearchRepository) {
        this.wordGroupRepository = wordGroupRepository;
        this.wordGroupMapper = wordGroupMapper;
        this.wordGroupSearchRepository = wordGroupSearchRepository;
    }

    /**
     * Return a {@link List} of {@link WordGroupDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<WordGroupDTO> findByCriteria(WordGroupCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<WordGroup> specification = createSpecification(criteria);
        return wordGroupMapper.toDto(wordGroupRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link WordGroupDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<WordGroupDTO> findByCriteria(WordGroupCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<WordGroup> specification = createSpecification(criteria);
        final Page<WordGroup> result = wordGroupRepository.findAll(specification, page);
        return result.map(wordGroupMapper::toDto);
    }

    /**
     * Function to convert WordGroupCriteria to a {@link Specifications}
     */
    private Specifications<WordGroup> createSpecification(WordGroupCriteria criteria) {
        Specifications<WordGroup> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), WordGroup_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), WordGroup_.name));
            }
            if (criteria.getRank() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRank(), WordGroup_.rank));
            }
            if (criteria.getLifeStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getLifeStatus(), WordGroup_.lifeStatus));
            }
            if (criteria.getImgId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getImgId(), WordGroup_.img, Image_.id));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getUserId(), WordGroup_.user, User_.id));
            }
        }
        return specification;
    }

}
