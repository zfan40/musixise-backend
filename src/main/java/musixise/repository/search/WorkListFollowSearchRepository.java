package musixise.repository.search;

import musixise.domain.WorkListFollow;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the WorkListFollow entity.
 */
public interface WorkListFollowSearchRepository extends ElasticsearchRepository<WorkListFollow, Long> {
}
