package com.qigu.readword.repository;

import com.qigu.readword.domain.MessageContent;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the MessageContent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MessageContentRepository extends JpaRepository<MessageContent, Long>, JpaSpecificationExecutor<MessageContent> {

}
