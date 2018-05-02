package com.qigu.readword.repository;

import com.qigu.readword.domain.Question;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Question entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuestionRepository extends JpaRepository<Question, Long>, JpaSpecificationExecutor<Question> {
    @Query("select question from Question question where question.user.login = ?#{principal.username}")
    Question findByCurrentUser();
}
