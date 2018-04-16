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

import com.qigu.readword.domain.Image;
import com.qigu.readword.domain.*; // for static metamodels
import com.qigu.readword.repository.ImageRepository;
import com.qigu.readword.repository.search.ImageSearchRepository;
import com.qigu.readword.service.dto.ImageCriteria;

import com.qigu.readword.service.dto.ImageDTO;
import com.qigu.readword.service.mapper.ImageMapper;

/**
 * Service for executing complex queries for Image entities in the database.
 * The main input is a {@link ImageCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ImageDTO} or a {@link Page} of {@link ImageDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ImageQueryService extends QueryService<Image> {

    private final Logger log = LoggerFactory.getLogger(ImageQueryService.class);


    private final ImageRepository imageRepository;

    private final ImageMapper imageMapper;

    private final ImageSearchRepository imageSearchRepository;

    public ImageQueryService(ImageRepository imageRepository, ImageMapper imageMapper, ImageSearchRepository imageSearchRepository) {
        this.imageRepository = imageRepository;
        this.imageMapper = imageMapper;
        this.imageSearchRepository = imageSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ImageDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ImageDTO> findByCriteria(ImageCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Image> specification = createSpecification(criteria);
        return imageMapper.toDto(imageRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ImageDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ImageDTO> findByCriteria(ImageCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Image> specification = createSpecification(criteria);
        final Page<Image> result = imageRepository.findAll(specification, page);
        return result.map(imageMapper::toDto);
    }

    /**
     * Function to convert ImageCriteria to a {@link Specifications}
     */
    private Specifications<Image> createSpecification(ImageCriteria criteria) {
        Specifications<Image> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Image_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Image_.name));
            }
            if (criteria.getUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUrl(), Image_.url));
            }
        }
        return specification;
    }

}
