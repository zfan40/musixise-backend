package musixise.web.rest.mapper;

import musixise.domain.*;
import musixise.web.rest.dto.WorkListDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity WorkList and its DTO WorkListDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface WorkListMapper {

    WorkListDTO workListToWorkListDTO(WorkList workList);

    List<WorkListDTO> workListsToWorkListDTOs(List<WorkList> workLists);

    WorkList workListDTOToWorkList(WorkListDTO workListDTO);

    List<WorkList> workListDTOsToWorkLists(List<WorkListDTO> workListDTOs);
}
