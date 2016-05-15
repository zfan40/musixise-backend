package musixise.web.rest.mapper;

import musixise.domain.*;
import musixise.web.rest.dto.StagesDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Stages and its DTO StagesDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface StagesMapper {

    StagesDTO stagesToStagesDTO(Stages stages);

    List<StagesDTO> stagesToStagesDTOs(List<Stages> stages);

    Stages stagesDTOToStages(StagesDTO stagesDTO);

    List<Stages> stagesDTOsToStages(List<StagesDTO> stagesDTOs);
}
