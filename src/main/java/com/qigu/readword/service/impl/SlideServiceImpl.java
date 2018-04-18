package com.qigu.readword.service.impl;

import com.qigu.readword.service.SlideService;
import com.qigu.readword.domain.Slide;
import com.qigu.readword.repository.SlideRepository;
import com.qigu.readword.repository.search.SlideSearchRepository;
import com.qigu.readword.service.dto.SlideDTO;
import com.qigu.readword.service.mapper.SlideMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Slide.
 */
@Service
@Transactional
public class SlideServiceImpl implements SlideService {

    private final Logger log = LoggerFactory.getLogger(SlideServiceImpl.class);

    private final SlideRepository slideRepository;

    private final SlideMapper slideMapper;

    private final SlideSearchRepository slideSearchRepository;

    public SlideServiceImpl(SlideRepository slideRepository, SlideMapper slideMapper, SlideSearchRepository slideSearchRepository) {
        this.slideRepository = slideRepository;
        this.slideMapper = slideMapper;
        this.slideSearchRepository = slideSearchRepository;
    }

    /**
     * Save a slide.
     *
     * @param slideDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public SlideDTO save(SlideDTO slideDTO) {
        log.debug("Request to save Slide : {}", slideDTO);
        Slide slide = slideMapper.toEntity(slideDTO);
        slide = slideRepository.save(slide);
        SlideDTO result = slideMapper.toDto(slide);
        slideSearchRepository.save(slide);
        return result;
    }

    /**
     * Get all the slides.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SlideDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Slides");
        return slideRepository.findAll(pageable)
            .map(slideMapper::toDto);
    }

    /**
     * Get one slide by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public SlideDTO findOne(Long id) {
        log.debug("Request to get Slide : {}", id);
        Slide slide = slideRepository.findOne(id);
        return slideMapper.toDto(slide);
    }

    /**
     * Delete the slide by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Slide : {}", id);
        slideRepository.delete(id);
        slideSearchRepository.delete(id);
    }

    /**
     * Search for the slide corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SlideDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Slides for query {}", query);
        Page<Slide> result = slideSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(slideMapper::toDto);
    }
}
