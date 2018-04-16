package com.qigu.readword.service.impl;

import com.qigu.readword.service.MessageContentService;
import com.qigu.readword.domain.MessageContent;
import com.qigu.readword.repository.MessageContentRepository;
import com.qigu.readword.repository.search.MessageContentSearchRepository;
import com.qigu.readword.service.dto.MessageContentDTO;
import com.qigu.readword.service.mapper.MessageContentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing MessageContent.
 */
@Service
@Transactional
public class MessageContentServiceImpl implements MessageContentService {

    private final Logger log = LoggerFactory.getLogger(MessageContentServiceImpl.class);

    private final MessageContentRepository messageContentRepository;

    private final MessageContentMapper messageContentMapper;

    private final MessageContentSearchRepository messageContentSearchRepository;

    public MessageContentServiceImpl(MessageContentRepository messageContentRepository, MessageContentMapper messageContentMapper, MessageContentSearchRepository messageContentSearchRepository) {
        this.messageContentRepository = messageContentRepository;
        this.messageContentMapper = messageContentMapper;
        this.messageContentSearchRepository = messageContentSearchRepository;
    }

    /**
     * Save a messageContent.
     *
     * @param messageContentDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public MessageContentDTO save(MessageContentDTO messageContentDTO) {
        log.debug("Request to save MessageContent : {}", messageContentDTO);
        MessageContent messageContent = messageContentMapper.toEntity(messageContentDTO);
        messageContent = messageContentRepository.save(messageContent);
        MessageContentDTO result = messageContentMapper.toDto(messageContent);
        messageContentSearchRepository.save(messageContent);
        return result;
    }

    /**
     * Get all the messageContents.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MessageContentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MessageContents");
        return messageContentRepository.findAll(pageable)
            .map(messageContentMapper::toDto);
    }

    /**
     * Get one messageContent by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public MessageContentDTO findOne(Long id) {
        log.debug("Request to get MessageContent : {}", id);
        MessageContent messageContent = messageContentRepository.findOne(id);
        return messageContentMapper.toDto(messageContent);
    }

    /**
     * Delete the messageContent by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete MessageContent : {}", id);
        messageContentRepository.delete(id);
        messageContentSearchRepository.delete(id);
    }

    /**
     * Search for the messageContent corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MessageContentDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of MessageContents for query {}", query);
        Page<MessageContent> result = messageContentSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(messageContentMapper::toDto);
    }
}
