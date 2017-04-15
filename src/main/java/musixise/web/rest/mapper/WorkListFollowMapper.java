package musixise.web.rest.mapper;


import musixise.config.Constants;
import musixise.domain.WorkListFollow;
import musixise.utils.DateUtil;
import musixise.utils.StringUtil;
import musixise.web.rest.dto.OwnerDTO;
import musixise.web.rest.dto.WorkListFollowDTO;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Created by zhaowei on 17/4/15.
 */
@Mapper(componentModel = "spring", uses = {DateMapper.class})
public interface WorkListFollowMapper {

    List<WorkListFollowDTO> workListFollowsToWorkListFollowDTOs(List<WorkListFollow> workListFollows);

    default WorkListFollowDTO workListFollowToWorkListFollowDTO(WorkListFollow workListFollow) {
        WorkListFollowDTO workListFollowDTO = new WorkListFollowDTO();
        workListFollowDTO.setId(workListFollow.getWork().getId());
        workListFollowDTO.setTitle(workListFollow.getWork().getTitle());
        workListFollowDTO.setCover(workListFollow.getWork().getCover());
        workListFollowDTO.setContent(workListFollow.getWork().getContent());
        String url = "";
        if (StringUtils.isNoneBlank(workListFollow.getWork().getUrl())) {
            if (workListFollow.getWork().getUrl().indexOf("clouddn") > 0) {
                url = workListFollow.getWork().getUrl();
            } else {
                //拼装地址
                url = String.format(Constants.QINIU_AUDIO_DOMAIN, workListFollow.getWork().getUrl());
            }
        }
        workListFollowDTO.setUrl(url);
        workListFollowDTO.setCreatedDate(DateUtil.asDate(workListFollow.getWork().getCreatedDate()));
        workListFollowDTO.setLastModifiedDate(DateUtil.asDate(workListFollow.getWork().getLastModifiedDate()));
        workListFollowDTO.setUserId(workListFollow.getWork().getUserId());
        workListFollowDTO.setFileHash(StringUtil.getMD5(workListFollow.getWork().getUrl()));
        workListFollowDTO.setCollectNum(workListFollow.getWork().getCollectNum());

        OwnerDTO ownerDTO = new OwnerDTO();
        if (workListFollow.getWork().getMusixiser()!= null) {
            ownerDTO.setUid(workListFollow.getWork().getMusixiser().getUserId());
            ownerDTO.setNickName(workListFollow.getWork().getMusixiser().getRealname());
            ownerDTO.setLargeAvatar(workListFollow.getWork().getMusixiser().getLargeAvatar());
            ownerDTO.setSmallAvatar(workListFollow.getWork().getMusixiser().getSmallAvatar());
        }
        workListFollowDTO.setOwner(ownerDTO);

        return workListFollowDTO;
    }
}
