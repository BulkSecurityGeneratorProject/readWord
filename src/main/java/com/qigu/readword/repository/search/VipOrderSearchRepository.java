package com.qigu.readword.repository.search;

import com.qigu.readword.domain.VipOrder;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the VipOrder entity.
 */
public interface VipOrderSearchRepository extends ElasticsearchRepository<VipOrder, Long> {
}
