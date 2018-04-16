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

import com.qigu.readword.domain.MessageStatus;
import com.qigu.readword.domain.*; // for static metamodels
import com.qigu.readword.repository.MessageStatusRepository;
import com.qigu.readword.repository.search.MessageStatusSearchRepository;
import com.qigu.readword.service.dto.MessageStatusCriteria;

import com.qigu.readword.service.dto.MessageStatusDTO;
import com.qigu.readword.service.mapper.MessageStatusMapper;
import com.qigu.readword.domain.enumeration.MessageStatusEnum;

/**
 * Service for executing complex queries for MessageStatus entities in the database.
 * The main input is a {@link MessageStatusCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MessageStatusDTO} or a {@link Page} of {@link MessageStatusDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MessageStatusQueryService extends QueryService<MessageStatus> {

    private final Logger log = LoggerFactory.getLogger(MessageStatusQueryService.class);


    private final MessageStatusRepository messageStatusRepository;

    private final MessageStatusMapper messageStatusMapper;

    private final MessageStatusSearchRepository messageStatusSearchRepository;

    public MessageStatusQueryService(MessageStatusRepository messageStatusRepository, MessageStatusMapper messageStatusMapper, MessageStatusSearchRepository messageStatusSearchRepository) {
        this.messageStatusRepository = messageStatusRepository;
        this.messageStatusMapper = messageStatusMapper;
        this.messageStatusSearchRepository = messageStatusSearchRepository;
    }

    /**
     * Return a {@link List} of {@link MessageStatusDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MessageStatusDTO> findByCriteria(MessageStatusCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<MessageStatus> specification = createSpecification(criteria);
        return messageStatusMapper.toDto(messageStatusRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MessageStatusDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MessageStatusDTO> findByCriteria(MessageStatusCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<MessageStatus> specification = createSpecification(criteria);
        final Page<MessageStatus> result = messageStatusRepository.findAll(specification, page);
        return result.map(messageStatusMapper::toDto);
    }

    /**
     * Function to convert MessageStatusCriteria to a {@link Specifications}
     */
    private Specifications<MessageStatus> createSpecification(MessageStatusCriteria criteria) {
        Specifications<MessageStatus> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), MessageStatus_.id));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), MessageStatus_.status));
            }
            if (criteria.getMsgId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getMsgId(), MessageStatus_.msg, Message_.id));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getUserId(), MessageStatus_.user, User_.id));
            }
        }
        return specification;
    }

}
