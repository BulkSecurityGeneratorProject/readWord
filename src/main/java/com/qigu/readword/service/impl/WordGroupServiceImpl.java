package com.qigu.readword.service.impl;

import com.qigu.readword.service.WordGroupService;
import com.qigu.readword.domain.WordGroup;
import com.qigu.readword.repository.WordGroupRepository;
import com.qigu.readword.repository.search.WordGroupSearchRepository;
import com.qigu.readword.service.dto.WordGroupDTO;
import com.qigu.readword.service.mapper.WordGroupMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing WordGroup.
 */
@Service
@Transactional
public class WordGroupServiceImpl implements WordGroupService {

    private final Logger log = LoggerFactory.getLogger(WordGroupServiceImpl.class);

    private final WordGroupRepository wordGroupRepository;

    private final WordGroupMapper wordGroupMapper;

    private final WordGroupSearchRepository wordGroupSearchRepository;

    public WordGroupServiceImpl(WordGroupRepository wordGroupRepository, WordGroupMapper wordGroupMapper, WordGroupSearchRepository wordGroupSearchRepository) {
        this.wordGroupRepository = wordGroupRepository;
        this.wordGroupMapper = wordGroupMapper;
        this.wordGroupSearchRepository = wordGroupSearchRepository;
    }

    /**
     * Save a wordGroup.
     *
     * @param wordGroupDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public WordGroupDTO save(WordGroupDTO wordGroupDTO) {
        log.debug("Request to save WordGroup : {}", wordGroupDTO);
        WordGroup wordGroup = wordGroupMapper.toEntity(wordGroupDTO);
        wordGroup = wordGroupRepository.save(wordGroup);
        WordGroupDTO result = wordGroupMapper.toDto(wordGroup);
        wordGroupSearchRepository.save(wordGroup);
        return result;
    }

    /**
     * Get all the wordGroups.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<WordGroupDTO> findAll(Pageable pageable) {
        log.debug("Request to get all WordGroups");
        return wordGroupRepository.findAll(pageable)
            .map(wordGroupMapper::toDto);
    }

    /**
     * Get one wordGroup by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public WordGroupDTO findOne(Long id) {
        log.debug("Request to get WordGroup : {}", id);
        WordGroup wordGroup = wordGroupRepository.findOne(id);
        return wordGroupMapper.toDto(wordGroup);
    }

    /**
     * Delete the wordGroup by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete WordGroup : {}", id);
        wordGroupRepository.delete(id);
        wordGroupSearchRepository.delete(id);
    }

    /**
     * Search for the wordGroup corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<WordGroupDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of WordGroups for query {}", query);
        Page<WordGroup> result = wordGroupSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(wordGroupMapper::toDto);
    }
}
