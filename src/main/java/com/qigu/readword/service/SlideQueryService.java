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

import com.qigu.readword.domain.Slide;
import com.qigu.readword.domain.*; // for static metamodels
import com.qigu.readword.repository.SlideRepository;
import com.qigu.readword.repository.search.SlideSearchRepository;
import com.qigu.readword.service.dto.SlideCriteria;

import com.qigu.readword.service.dto.SlideDTO;
import com.qigu.readword.service.mapper.SlideMapper;
import com.qigu.readword.domain.enumeration.LifeStatus;

/**
 * Service for executing complex queries for Slide entities in the database.
 * The main input is a {@link SlideCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SlideDTO} or a {@link Page} of {@link SlideDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SlideQueryService extends QueryService<Slide> {

    private final Logger log = LoggerFactory.getLogger(SlideQueryService.class);


    private final SlideRepository slideRepository;

    private final SlideMapper slideMapper;

    private final SlideSearchRepository slideSearchRepository;

    public SlideQueryService(SlideRepository slideRepository, SlideMapper slideMapper, SlideSearchRepository slideSearchRepository) {
        this.slideRepository = slideRepository;
        this.slideMapper = slideMapper;
        this.slideSearchRepository = slideSearchRepository;
    }

    /**
     * Return a {@link List} of {@link SlideDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SlideDTO> findByCriteria(SlideCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Slide> specification = createSpecification(criteria);
        return slideMapper.toDto(slideRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SlideDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SlideDTO> findByCriteria(SlideCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Slide> specification = createSpecification(criteria);
        final Page<Slide> result = slideRepository.findAll(specification, page);
        return result.map(slideMapper::toDto);
    }

    /**
     * Function to convert SlideCriteria to a {@link Specifications}
     */
    private Specifications<Slide> createSpecification(SlideCriteria criteria) {
        Specifications<Slide> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Slide_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Slide_.name));
            }
            if (criteria.getRank() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRank(), Slide_.rank));
            }
            if (criteria.getLifeStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getLifeStatus(), Slide_.lifeStatus));
            }
            if (criteria.getImgId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getImgId(), Slide_.img, Image_.id));
            }
        }
        return specification;
    }

}
