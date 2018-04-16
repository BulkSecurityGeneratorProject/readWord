package com.qigu.readword.repository;

import com.qigu.readword.domain.WordGroup;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the WordGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WordGroupRepository extends JpaRepository<WordGroup, Long>, JpaSpecificationExecutor<WordGroup> {

    @Query("select word_group from WordGroup word_group where word_group.user.login = ?#{principal.username}")
    List<WordGroup> findByUserIsCurrentUser();

}
