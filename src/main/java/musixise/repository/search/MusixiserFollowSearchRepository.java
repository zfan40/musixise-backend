package musixise.repository.search;

import musixise.domain.MusixiserFollow;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the MusixiserFollow entity.
 */
public interface MusixiserFollowSearchRepository extends ElasticsearchRepository<MusixiserFollow, Long> {
}
