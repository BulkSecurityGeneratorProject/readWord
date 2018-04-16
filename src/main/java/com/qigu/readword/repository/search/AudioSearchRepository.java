package com.qigu.readword.repository.search;

import com.qigu.readword.domain.Audio;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Audio entity.
 */
public interface AudioSearchRepository extends ElasticsearchRepository<Audio, Long> {
}
