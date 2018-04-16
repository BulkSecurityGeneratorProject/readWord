package com.qigu.readword.repository;

import com.qigu.readword.domain.MessageStatus;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the MessageStatus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MessageStatusRepository extends JpaRepository<MessageStatus, Long>, JpaSpecificationExecutor<MessageStatus> {

}
