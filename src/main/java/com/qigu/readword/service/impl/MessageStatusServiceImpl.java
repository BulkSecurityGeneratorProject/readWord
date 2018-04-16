package com.qigu.readword.service.impl;

import com.qigu.readword.service.MessageStatusService;
import com.qigu.readword.domain.MessageStatus;
import com.qigu.readword.repository.MessageStatusRepository;
import com.qigu.readword.repository.search.MessageStatusSearchRepository;
import com.qigu.readword.service.dto.MessageStatusDTO;
import com.qigu.readword.service.mapper.MessageStatusMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing MessageStatus.
 */
@Service
@Transactional
public class MessageStatusServiceImpl implements MessageStatusService {

    private final Logger log = LoggerFactory.getLogger(MessageStatusServiceImpl.class);

    private final MessageStatusRepository messageStatusRepository;

    private final MessageStatusMapper messageStatusMapper;

    private final MessageStatusSearchRepository messageStatusSearchRepository;

    public MessageStatusServiceImpl(MessageStatusRepository messageStatusRepository, MessageStatusMapper messageStatusMapper, MessageStatusSearchRepository messageStatusSearchRepository) {
        this.messageStatusRepository = messageStatusRepository;
        this.messageStatusMapper = messageStatusMapper;
        this.messageStatusSearchRepository = messageStatusSearchRepository;
    }

    /**
     * Save a messageStatus.
     *
     * @param messageStatusDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public MessageStatusDTO save(MessageStatusDTO messageStatusDTO) {
        log.debug("Request to save MessageStatus : {}", messageStatusDTO);
        MessageStatus messageStatus = messageStatusMapper.toEntity(messageStatusDTO);
        messageStatus = messageStatusRepository.save(messageStatus);
        MessageStatusDTO result = messageStatusMapper.toDto(messageStatus);
        messageStatusSearchRepository.save(messageStatus);
        return result;
    }

    /**
     * Get all the messageStatuses.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MessageStatusDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MessageStatuses");
        return messageStatusRepository.findAll(pageable)
            .map(messageStatusMapper::toDto);
    }

    /**
     * Get one messageStatus by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public MessageStatusDTO findOne(Long id) {
        log.debug("Request to get MessageStatus : {}", id);
        MessageStatus messageStatus = messageStatusRepository.findOne(id);
        return messageStatusMapper.toDto(messageStatus);
    }

    /**
     * Delete the messageStatus by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete MessageStatus : {}", id);
        messageStatusRepository.delete(id);
        messageStatusSearchRepository.delete(id);
    }

    /**
     * Search for the messageStatus corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MessageStatusDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of MessageStatuses for query {}", query);
        Page<MessageStatus> result = messageStatusSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(messageStatusMapper::toDto);
    }
}
