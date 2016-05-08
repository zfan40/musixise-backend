package musixise.repository.search;

import musixise.domain.Stages;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Stages entity.
 */
public interface StagesSearchRepository extends ElasticsearchRepository<Stages, Long> {
}
