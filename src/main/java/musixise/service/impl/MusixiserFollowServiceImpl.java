package musixise.service.impl;

import musixise.domain.MusixiserFollow;
import musixise.repository.MusixiserFollowRepository;
import musixise.repository.search.MusixiserFollowSearchRepository;
import musixise.service.MusixiserFollowService;
import musixise.utils.PageUtil;
import musixise.web.rest.dto.MusixiserFollowDTO;
import musixise.web.rest.dto.MusixiserFollowerDTO;
import musixise.web.rest.dto.MusixiserFollowingDTO;
import musixise.web.rest.dto.PageDTO;
import musixise.web.rest.mapper.MusixiserFollowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing MusixiserFollow.
 */
@Service
@Transactional
public class MusixiserFollowServiceImpl implements MusixiserFollowService{

    private final Logger log = LoggerFactory.getLogger(MusixiserFollowServiceImpl.class);

    @Inject
    private MusixiserFollowRepository musixiserFollowRepository;

    @Inject
    private MusixiserFollowMapper musixiserFollowMapper;

    @Inject
    private MusixiserFollowSearchRepository musixiserFollowSearchRepository;

    /**
     * Save a musixiserFollow.
     *
     * @param musixiserFollowDTO the entity to save
     * @return the persisted entity
     */
    public MusixiserFollowDTO save(MusixiserFollowDTO musixiserFollowDTO) {
        log.debug("Request to save MusixiserFollow : {}", musixiserFollowDTO);
        MusixiserFollow musixiserFollow = musixiserFollowMapper.musixiserFollowDTOToMusixiserFollow(musixiserFollowDTO);
        musixiserFollow = musixiserFollowRepository.save(musixiserFollow);
        MusixiserFollowDTO result = musixiserFollowMapper.musixiserFollowToMusixiserFollowDTO(musixiserFollow);
        musixiserFollowSearchRepository.save(musixiserFollow);
        return result;
    }

    /**
     *  Get all the musixiserFollows.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<MusixiserFollow> findAll(Pageable pageable) {
        log.debug("Request to get all MusixiserFollows");
        Page<MusixiserFollow> result = musixiserFollowRepository.findAll(pageable);
        return result;
    }

    @Transactional(readOnly = true)
    public Page<MusixiserFollow> findAllByUserId(Pageable pageable, Long userId) {
        log.debug("Request to get all MusixiserFollows");
        Page<MusixiserFollow> result = musixiserFollowRepository.findAllByUserId(pageable, userId);
        return result;
    }

    /**
     * 获取用户关注列表
     * @param pageable
     * @param userId
     * @return
     */
    public PageDTO<MusixiserFollowingDTO> findFollowingByUserId(Pageable pageable, Long userId) {
        log.debug("Request to get findFollowsByUserId");
        Page<MusixiserFollow> result = musixiserFollowRepository.findAllByUserId(pageable, userId);
        if (result.getTotalElements() >0) {
            List<MusixiserFollowingDTO> musixiserFollowDTOList = musixiserFollowMapper.musixiserFollowsToMusixiserFollowingDTOs(result.getContent());

            return PageUtil.getPage(musixiserFollowDTOList, result);


        } else {
            return null;
        }

    }

    /**
     * 获取用户粉丝列表
     * @param pageable
     * @param userId
     * @return
     */
    public PageDTO<MusixiserFollowerDTO> findFollowerByUserId(Pageable pageable, Long userId) {
        Page<MusixiserFollow> result = musixiserFollowRepository.findAllByFollowId(pageable, userId);
        if (result.getTotalElements() >0) {
            List<MusixiserFollowerDTO> musixiserFollowDTOList = musixiserFollowMapper.musixiserFollowsToMusixiserFollowerDTOs(result.getContent());

            return PageUtil.getPage(musixiserFollowDTOList, result);

        } else {
            return null;
        }

    }


    /**
     *  Get one musixiserFollow by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public MusixiserFollowDTO findOne(Long id) {
        log.debug("Request to get MusixiserFollow : {}", id);
        MusixiserFollow musixiserFollow = musixiserFollowRepository.findOne(id);
        MusixiserFollowDTO musixiserFollowDTO = musixiserFollowMapper.musixiserFollowToMusixiserFollowDTO(musixiserFollow);
        return musixiserFollowDTO;
    }

    /**
     *  Delete the  musixiserFollow by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete MusixiserFollow : {}", id);
        musixiserFollowRepository.delete(id);
        musixiserFollowSearchRepository.delete(id);
    }

    /**
     * Search for the musixiserFollow corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<MusixiserFollow> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of MusixiserFollows for query {}", query);
        return musixiserFollowSearchRepository.search(queryStringQuery(query), pageable);
    }

    @Override
    public Boolean isFollowed(Long userId, Long followId) {
        if (musixiserFollowRepository.findOneByUserIdAndFollowId(userId, followId).isPresent()) {
            return true;
        } else {
            return false;
        }
    }
}
