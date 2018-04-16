package com.qigu.readword.repository.search;

import com.qigu.readword.domain.Favorite;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Favorite entity.
 */
public interface FavoriteSearchRepository extends ElasticsearchRepository<Favorite, Long> {
}
