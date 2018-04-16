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

import com.qigu.readword.domain.MessageContent;
import com.qigu.readword.domain.*; // for static metamodels
import com.qigu.readword.repository.MessageContentRepository;
import com.qigu.readword.repository.search.MessageContentSearchRepository;
import com.qigu.readword.service.dto.MessageContentCriteria;

import com.qigu.readword.service.dto.MessageContentDTO;
import com.qigu.readword.service.mapper.MessageContentMapper;

/**
 * Service for executing complex queries for MessageContent entities in the database.
 * The main input is a {@link MessageContentCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MessageContentDTO} or a {@link Page} of {@link MessageContentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MessageContentQueryService extends QueryService<MessageContent> {

    private final Logger log = LoggerFactory.getLogger(MessageContentQueryService.class);


    private final MessageContentRepository messageContentRepository;

    private final MessageContentMapper messageContentMapper;

    private final MessageContentSearchRepository messageContentSearchRepository;

    public MessageContentQueryService(MessageContentRepository messageContentRepository, MessageContentMapper messageContentMapper, MessageContentSearchRepository messageContentSearchRepository) {
        this.messageContentRepository = messageContentRepository;
        this.messageContentMapper = messageContentMapper;
        this.messageContentSearchRepository = messageContentSearchRepository;
    }

    /**
     * Return a {@link List} of {@link MessageContentDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MessageContentDTO> findByCriteria(MessageContentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<MessageContent> specification = createSpecification(criteria);
        return messageContentMapper.toDto(messageContentRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MessageContentDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MessageContentDTO> findByCriteria(MessageContentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<MessageContent> specification = createSpecification(criteria);
        final Page<MessageContent> result = messageContentRepository.findAll(specification, page);
        return result.map(messageContentMapper::toDto);
    }

    /**
     * Function to convert MessageContentCriteria to a {@link Specifications}
     */
    private Specifications<MessageContent> createSpecification(MessageContentCriteria criteria) {
        Specifications<MessageContent> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), MessageContent_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), MessageContent_.name));
            }
        }
        return specification;
    }

}
