package com.qigu.readword.service;


import java.util.List;
import java.util.Optional;

import com.qigu.readword.repository.UserRepository;
import com.qigu.readword.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.qigu.readword.domain.Question;
import com.qigu.readword.domain.*; // for static metamodels
import com.qigu.readword.repository.QuestionRepository;
import com.qigu.readword.repository.search.QuestionSearchRepository;
import com.qigu.readword.service.dto.QuestionCriteria;

import com.qigu.readword.service.dto.QuestionDTO;
import com.qigu.readword.service.mapper.QuestionMapper;

/**
 * Service for executing complex queries for Question entities in the database.
 * The main input is a {@link QuestionCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link QuestionDTO} or a {@link Page} of {@link QuestionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class QuestionQueryService extends QueryService<Question> {

    private final Logger log = LoggerFactory.getLogger(QuestionQueryService.class);


    private final QuestionRepository questionRepository;

    private final UserRepository userRepository;

    private final QuestionMapper questionMapper;

    private final QuestionSearchRepository questionSearchRepository;

    public QuestionQueryService(QuestionRepository questionRepository, UserRepository userRepository, QuestionMapper questionMapper, QuestionSearchRepository questionSearchRepository) {
        this.questionRepository = questionRepository;
        this.userRepository = userRepository;
        this.questionMapper = questionMapper;
        this.questionSearchRepository = questionSearchRepository;
    }

    /**
     * Return a {@link List} of {@link QuestionDTO} which matches the criteria from the database
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<QuestionDTO> findByCriteria(QuestionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Question> specification = createSpecification(criteria);
        return questionMapper.toDto(questionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link QuestionDTO} which matches the criteria from the database
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page     The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<QuestionDTO> findByCriteria(QuestionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Question> specification = createSpecification(criteria);
        final Page<Question> result = questionRepository.findAll(specification, page);
        return result.map(questionMapper::toDto);
    }


    /**
     * Function to convert QuestionCriteria to a {@link Specifications}
     */
    private Specifications<Question> createSpecification(QuestionCriteria criteria) {
        Specifications<Question> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Question_.id));
            }
            if (criteria.getContact() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContact(), Question_.contact));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getUserId(), Question_.user, User_.id));
            }
        }
        return specification;
    }

}
