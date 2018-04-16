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

import com.qigu.readword.domain.Message;
import com.qigu.readword.domain.*; // for static metamodels
import com.qigu.readword.repository.MessageRepository;
import com.qigu.readword.repository.search.MessageSearchRepository;
import com.qigu.readword.service.dto.MessageCriteria;

import com.qigu.readword.service.dto.MessageDTO;
import com.qigu.readword.service.mapper.MessageMapper;

/**
 * Service for executing complex queries for Message entities in the database.
 * The main input is a {@link MessageCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MessageDTO} or a {@link Page} of {@link MessageDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MessageQueryService extends QueryService<Message> {

    private final Logger log = LoggerFactory.getLogger(MessageQueryService.class);


    private final MessageRepository messageRepository;

    private final MessageMapper messageMapper;

    private final MessageSearchRepository messageSearchRepository;

    public MessageQueryService(MessageRepository messageRepository, MessageMapper messageMapper, MessageSearchRepository messageSearchRepository) {
        this.messageRepository = messageRepository;
        this.messageMapper = messageMapper;
        this.messageSearchRepository = messageSearchRepository;
    }

    /**
     * Return a {@link List} of {@link MessageDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MessageDTO> findByCriteria(MessageCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Message> specification = createSpecification(criteria);
        return messageMapper.toDto(messageRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MessageDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MessageDTO> findByCriteria(MessageCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Message> specification = createSpecification(criteria);
        final Page<Message> result = messageRepository.findAll(specification, page);
        return result.map(messageMapper::toDto);
    }

    /**
     * Function to convert MessageCriteria to a {@link Specifications}
     */
    private Specifications<Message> createSpecification(MessageCriteria criteria) {
        Specifications<Message> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Message_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Message_.name));
            }
            if (criteria.getSendTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSendTime(), Message_.sendTime));
            }
            if (criteria.getImgId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getImgId(), Message_.img, Image_.id));
            }
            if (criteria.getContentId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getContentId(), Message_.content, MessageContent_.id));
            }
        }
        return specification;
    }

}
