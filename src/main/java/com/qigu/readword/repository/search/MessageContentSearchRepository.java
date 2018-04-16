package com.qigu.readword.repository.search;

import com.qigu.readword.domain.MessageContent;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the MessageContent entity.
 */
public interface MessageContentSearchRepository extends ElasticsearchRepository<MessageContent, Long> {
}
