package musixise.web.rest.mapper;

import musixise.domain.*;
import musixise.web.rest.dto.StagesFollowDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity StagesFollow and its DTO StagesFollowDTO.
 */
@Mapper(componentModel = "spring", uses = DateMapper.class)
public interface StagesFollowMapper {

    StagesFollowDTO stagesFollowToStagesFollowDTO(StagesFollow stagesFollow);

    List<StagesFollowDTO> stagesFollowsToStagesFollowDTOs(List<StagesFollow> stagesFollows);

    StagesFollow stagesFollowDTOToStagesFollow(StagesFollowDTO stagesFollowDTO);

    List<StagesFollow> stagesFollowDTOsToStagesFollows(List<StagesFollowDTO> stagesFollowDTOs);
}
