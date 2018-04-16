package com.qigu.readword.repository;

import com.qigu.readword.domain.Favorite;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the Favorite entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long>, JpaSpecificationExecutor<Favorite> {
    @Query("select distinct favorite from Favorite favorite left join fetch favorite.words")
    List<Favorite> findAllWithEagerRelationships();

    @Query("select favorite from Favorite favorite left join fetch favorite.words where favorite.id =:id")
    Favorite findOneWithEagerRelationships(@Param("id") Long id);

}
