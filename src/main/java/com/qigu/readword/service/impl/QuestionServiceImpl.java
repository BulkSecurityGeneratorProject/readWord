package com.qigu.readword.service.impl;

import com.qigu.readword.domain.User;
import com.qigu.readword.repository.UserRepository;
import com.qigu.readword.security.SecurityUtils;
import com.qigu.readword.service.QuestionService;
import com.qigu.readword.domain.Question;
import com.qigu.readword.repository.QuestionRepository;
import com.qigu.readword.repository.search.QuestionSearchRepository;
import com.qigu.readword.service.dto.QuestionDTO;
import com.qigu.readword.service.mapper.QuestionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Question.
 */
@Service
@Transactional
public class QuestionServiceImpl implements QuestionService {

    private final Logger log = LoggerFactory.getLogger(QuestionServiceImpl.class);

    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;

    private final QuestionMapper questionMapper;

    private final QuestionSearchRepository questionSearchRepository;

    public QuestionServiceImpl(QuestionRepository questionRepository, UserRepository userRepository, QuestionMapper questionMapper, QuestionSearchRepository questionSearchRepository) {
        this.questionRepository = questionRepository;
        this.userRepository = userRepository;
        this.questionMapper = questionMapper;
        this.questionSearchRepository = questionSearchRepository;
    }

    /**
     * Save a question.
     *
     * @param questionDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public QuestionDTO save(QuestionDTO questionDTO) {
        log.debug("Request to save Question : {}", questionDTO);
        Question question = questionMapper.toEntity(questionDTO);
        question = questionRepository.save(question);
        QuestionDTO result = questionMapper.toDto(question);
        questionSearchRepository.save(question);
        return result;
    }

    /**
     * Get all the questions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<QuestionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Questions");
        return questionRepository.findAll(pageable)
            .map(questionMapper::toDto);
    }

    /**
     * Get one question by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public QuestionDTO findOne(Long id) {
        log.debug("Request to get Question : {}", id);
        Question question = questionRepository.findOne(id);
        return questionMapper.toDto(question);
    }

    /**
     * Delete the question by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Question : {}", id);
        questionRepository.delete(id);
        questionSearchRepository.delete(id);
    }

    /**
     * Search for the question corresponding to the query.
     *
     * @param query    the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<QuestionDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Questions for query {}", query);
        Page<Question> result = questionSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(questionMapper::toDto);
    }

    @Override
    public QuestionDTO findByLogin() {

        Question question = questionRepository.findByCurrentUser();
        if (question == null) {
            question = new Question();
            Optional<String> currentUserLogin = SecurityUtils.getCurrentUserLogin();
            if (currentUserLogin.isPresent()) {
                Optional<User> currentUser = userRepository.findOneByLogin(currentUserLogin.get());
                if (currentUser.isPresent()) {
                    question.setUser(currentUser.get());
                    questionRepository.save(question);
                }
            }
        }
        return questionMapper.toDto(question);
    }

}
