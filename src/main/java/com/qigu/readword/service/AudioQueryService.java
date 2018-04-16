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

import com.qigu.readword.domain.Audio;
import com.qigu.readword.domain.*; // for static metamodels
import com.qigu.readword.repository.AudioRepository;
import com.qigu.readword.repository.search.AudioSearchRepository;
import com.qigu.readword.service.dto.AudioCriteria;

import com.qigu.readword.service.dto.AudioDTO;
import com.qigu.readword.service.mapper.AudioMapper;

/**
 * Service for executing complex queries for Audio entities in the database.
 * The main input is a {@link AudioCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AudioDTO} or a {@link Page} of {@link AudioDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AudioQueryService extends QueryService<Audio> {

    private final Logger log = LoggerFactory.getLogger(AudioQueryService.class);


    private final AudioRepository audioRepository;

    private final AudioMapper audioMapper;

    private final AudioSearchRepository audioSearchRepository;

    public AudioQueryService(AudioRepository audioRepository, AudioMapper audioMapper, AudioSearchRepository audioSearchRepository) {
        this.audioRepository = audioRepository;
        this.audioMapper = audioMapper;
        this.audioSearchRepository = audioSearchRepository;
    }

    /**
     * Return a {@link List} of {@link AudioDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AudioDTO> findByCriteria(AudioCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Audio> specification = createSpecification(criteria);
        return audioMapper.toDto(audioRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AudioDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AudioDTO> findByCriteria(AudioCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Audio> specification = createSpecification(criteria);
        final Page<Audio> result = audioRepository.findAll(specification, page);
        return result.map(audioMapper::toDto);
    }

    /**
     * Function to convert AudioCriteria to a {@link Specifications}
     */
    private Specifications<Audio> createSpecification(AudioCriteria criteria) {
        Specifications<Audio> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Audio_.id));
            }
            if (criteria.getUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUrl(), Audio_.url));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Audio_.name));
            }
        }
        return specification;
    }

}
