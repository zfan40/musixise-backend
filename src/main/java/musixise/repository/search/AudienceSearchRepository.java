package musixise.repository.search;

import musixise.domain.Audience;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Audience entity.
 */
public interface AudienceSearchRepository extends ElasticsearchRepository<Audience, Long> {
}
