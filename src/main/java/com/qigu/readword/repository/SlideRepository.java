package com.qigu.readword.repository;

import com.qigu.readword.domain.Slide;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Slide entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SlideRepository extends JpaRepository<Slide, Long>, JpaSpecificationExecutor<Slide> {

}
