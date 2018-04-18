package com.qigu.readword.repository.search;

import com.qigu.readword.domain.Slide;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Slide entity.
 */
public interface SlideSearchRepository extends ElasticsearchRepository<Slide, Long> {
}
