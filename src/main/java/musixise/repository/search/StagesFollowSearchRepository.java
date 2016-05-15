package musixise.repository.search;

import musixise.domain.StagesFollow;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the StagesFollow entity.
 */
public interface StagesFollowSearchRepository extends ElasticsearchRepository<StagesFollow, Long> {
}
