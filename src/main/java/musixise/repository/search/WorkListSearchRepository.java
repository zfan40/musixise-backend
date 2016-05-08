package musixise.repository.search;

import musixise.domain.WorkList;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the WorkList entity.
 */
public interface WorkListSearchRepository extends ElasticsearchRepository<WorkList, Long> {
}
