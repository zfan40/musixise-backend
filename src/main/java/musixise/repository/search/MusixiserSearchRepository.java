package musixise.repository.search;

import musixise.domain.Musixiser;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Musixiser entity.
 */
public interface MusixiserSearchRepository extends ElasticsearchRepository<Musixiser, Long> {
}
