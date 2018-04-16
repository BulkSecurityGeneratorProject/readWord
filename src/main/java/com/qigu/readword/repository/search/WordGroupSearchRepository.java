package com.qigu.readword.repository.search;

import com.qigu.readword.domain.WordGroup;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the WordGroup entity.
 */
public interface WordGroupSearchRepository extends ElasticsearchRepository<WordGroup, Long> {
}
