package com.qigu.readword.repository.search;

import com.qigu.readword.domain.MessageStatus;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the MessageStatus entity.
 */
public interface MessageStatusSearchRepository extends ElasticsearchRepository<MessageStatus, Long> {
}
