package com.qigu.readword.repository;

import com.qigu.readword.domain.Word;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Word entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WordRepository extends JpaRepository<Word, Long>, JpaSpecificationExecutor<Word> {

    @Query("select word from Word word where word.user.login = ?#{principal.username}")
    List<Word> findByUserIsCurrentUser();

}
