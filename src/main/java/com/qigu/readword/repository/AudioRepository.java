package com.qigu.readword.repository;

import com.qigu.readword.domain.Audio;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the Audio entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AudioRepository extends JpaRepository<Audio, Long>, JpaSpecificationExecutor<Audio> {

    List<Audio> findAllByOneSpeedUrlIsNull();
}
