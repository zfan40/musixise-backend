package musixise.service.impl;

import musixise.config.Constants;
import musixise.security.SecurityUtils;
import musixise.service.MusixiserService;
import musixise.domain.Musixiser;
import musixise.repository.MusixiserRepository;
import musixise.repository.search.MusixiserSearchRepository;
import musixise.web.rest.dto.MusixiserDTO;
import musixise.web.rest.dto.user.RegisterDTO;
import musixise.web.rest.mapper.MusixiserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Musixiser.
 */
@Service
@Transactional
public class MusixiserServiceImpl implements MusixiserService{

    private final Logger log = LoggerFactory.getLogger(MusixiserServiceImpl.class);

    @Inject
    private MusixiserRepository musixiserRepository;

    @Inject
    private MusixiserMapper musixiserMapper;

    @Inject
    private MusixiserSearchRepository musixiserSearchRepository;

    /**
     * Save a musixiser.
     *
     * @param musixiserDTO the entity to save
     * @return the persisted entity
     */
    public MusixiserDTO save(MusixiserDTO musixiserDTO) {
        log.debug("Request to save Musixiser : {}", musixiserDTO);
        Musixiser musixiser = musixiserMapper.musixiserDTOToMusixiser(musixiserDTO);
        musixiser = musixiserRepository.save(musixiser);
        MusixiserDTO result = musixiserMapper.musixiserToMusixiserDTO(musixiser);
        musixiserSearchRepository.save(musixiser);
        return result;
    }

    /**
     *  Get all the musixisers.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Musixiser> findAll(Pageable pageable) {
        log.debug("Request to get all Musixisers");
        Page<Musixiser> result = musixiserRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one musixiser by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public MusixiserDTO findOne(Long id) {
        log.debug("Request to get Musixiser : {}", id);
        Musixiser musixiser = musixiserRepository.findOne(id);
        MusixiserDTO musixiserDTO = musixiserMapper.musixiserToMusixiserDTO(musixiser);
        return musixiserDTO;
    }

    /**
     *  Delete the  musixiser by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Musixiser : {}", id);
        musixiserRepository.delete(id);
        musixiserSearchRepository.delete(id);
    }

    /**
     * Search for the musixiser corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Musixiser> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Musixisers for query {}", query);
        return musixiserSearchRepository.search(queryStringQuery(query), pageable);
    }

    public String getDefaultAvatar() {
        List<String> list = new ArrayList<>();
        list.add("https://gw.alicdn.com/tps/TB1wTxdOXXXXXXbXXXXXXXXXXXX-750-750.png");
        list.add("https://gw.alicdn.com/tps/TB15cU5NVXXXXcYXpXXXXXXXXXX-750-750.png");
        list.add("https://gw.alicdn.com/tps/TB10xXcOXXXXXaOXXXXXXXXXXXX-750-750.png");
        list.add("https://gw.alicdn.com/tps/TB1IXADNVXXXXaeapXXXXXXXXXX-750-750.png");
        list.add("https://gw.alicdn.com/tps/TB1vYheOXXXXXaOXXXXXXXXXXXX-750-750.png");
        list.add("https://gw.alicdn.com/tps/TB1gQQONVXXXXczXVXXXXXXXXXX-750-750.png");
        list.add("https://gw.alicdn.com/tps/TB13OMFNVXXXXcoaXXXXXXXXXXX-750-750.png");
        list.add("https://gw.alicdn.com/tps/TB1fcMYNVXXXXXqXVXXXXXXXXXX-750-750.png");

        int index=(int)(Math.random()* list.size());
        return list.get(index);
    }

    @Override
    public MusixiserDTO getInfoByUid(Long id) {

        Musixiser musixiser = musixiserRepository.findOneByUserId(id);

        if (musixiser == null) return null;
        MusixiserDTO musixiserDTO = new MusixiserDTO();
        //musixiserDTO.setId(musixiser.getId());
        musixiserDTO.setUserId(musixiser.getUserId());
        musixiserDTO.setUsername(SecurityUtils.getCurrentUserLogin());
        musixiserDTO.setEmail(musixiser.getEmail());

        //拼接图片地址
        if (musixiser.getLargeAvatar() != null && !musixiser.getLargeAvatar().equals("") && musixiser.getLargeAvatar().indexOf("alicdn") == -1) {
            if (musixiser.getLargeAvatar().indexOf("http") == -1) {
                musixiser.setLargeAvatar(String.format(Constants.QINIU_IMG_DOMAIN, musixiser.getLargeAvatar()));
            }
        }

        if (musixiser.getSmallAvatar() != null && !musixiser.getSmallAvatar().equals("") && musixiser.getSmallAvatar().indexOf("alicdn") == -1) {
            if (musixiser.getSmallAvatar().indexOf("http") == -1) {
                musixiser.setSmallAvatar(String.format(Constants.QINIU_IMG_DOMAIN, musixiser.getSmallAvatar()));
            }
        }

        musixiserDTO.setLargeAvatar(musixiser.getLargeAvatar());
        musixiserDTO.setSmallAvatar(musixiser.getSmallAvatar());
        musixiserDTO.setNation(musixiser.getNation());
        musixiserDTO.setBirth(musixiser.getBirth());
        musixiserDTO.setTel(musixiser.getTel());
        musixiserDTO.setRealname(musixiser.getRealname());
        musixiserDTO.setIsMaster(musixiser.getIsMaster());

        return musixiserDTO;
    }

    @Override
    public Musixiser registerMusixiser(Long id, RegisterDTO registerDTO) {
        //保存个人信息
        Musixiser musixiser = new Musixiser();

        musixiser.setUserId(id);
        musixiser.setRealname(registerDTO.getRealname());
        musixiser.setTel(registerDTO.getTel());
        musixiser.setEmail(registerDTO.getEmail());
        musixiser.setBirth(registerDTO.getBirth());
        musixiser.setGender(registerDTO.getGender());

        //判断图片是否为空,为空则设置默认图片
        if (registerDTO.getLargeAvatar() == null || registerDTO.getLargeAvatar().equals("")) {
            String defalutAvatar = getDefaultAvatar();
            registerDTO.setLargeAvatar(defalutAvatar);
            registerDTO.setSmallAvatar(defalutAvatar);
        }

        musixiser.setSmallAvatar(registerDTO.getSmallAvatar());
        musixiser.setLargeAvatar(registerDTO.getLargeAvatar());
        musixiser.setNation(registerDTO.getNation());

        return musixiserRepository.save(musixiser);
    }
}
