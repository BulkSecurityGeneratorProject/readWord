package com.qigu.readword.service.impl;

import com.qigu.readword.service.WordService;
import com.qigu.readword.domain.Word;
import com.qigu.readword.repository.WordRepository;
import com.qigu.readword.repository.search.WordSearchRepository;
import com.qigu.readword.service.dto.WordDTO;
import com.qigu.readword.service.mapper.WordMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Word.
 */
@Service
@Transactional
public class WordServiceImpl implements WordService {

    private final Logger log = LoggerFactory.getLogger(WordServiceImpl.class);

    private final WordRepository wordRepository;

    private final WordMapper wordMapper;

    private final WordSearchRepository wordSearchRepository;

    public WordServiceImpl(WordRepository wordRepository, WordMapper wordMapper, WordSearchRepository wordSearchRepository) {
        this.wordRepository = wordRepository;
        this.wordMapper = wordMapper;
        this.wordSearchRepository = wordSearchRepository;
    }

    /**
     * Save a word.
     *
     * @param wordDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public WordDTO save(WordDTO wordDTO) {
        log.debug("Request to save Word : {}", wordDTO);
        Word word = wordMapper.toEntity(wordDTO);
        word = wordRepository.save(word);
        WordDTO result = wordMapper.toDto(word);
        wordSearchRepository.save(word);
        return result;
    }

    /**
     * Get all the words.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<WordDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Words");
        return wordRepository.findAll(pageable)
            .map(wordMapper::toDto);
    }

    /**
     * Get one word by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public WordDTO findOne(Long id) {
        log.debug("Request to get Word : {}", id);
        Word word = wordRepository.findOne(id);
        return wordMapper.toDto(word);
    }

    /**
     * Delete the word by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Word : {}", id);
        wordRepository.delete(id);
        wordSearchRepository.delete(id);
    }

    /**
     * Search for the word corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<WordDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Words for query {}", query);
        Page<Word> result = wordSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(wordMapper::toDto);
    }
}
