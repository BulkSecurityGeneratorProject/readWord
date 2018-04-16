package com.qigu.readword.repository.search;

import com.qigu.readword.domain.Word;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Word entity.
 */
public interface WordSearchRepository extends ElasticsearchRepository<Word, Long> {
}
