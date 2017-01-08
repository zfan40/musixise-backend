package musixise.web.rest.mapper;

import musixise.domain.WorkList;
import musixise.web.rest.dto.WorkListDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper for the entity WorkList and its DTO WorkListDTO.
 */
@Mapper(componentModel = "spring", uses = DateMapper.class)
public interface WorkListMapper {

    WorkListDTO workListToWorkListDTO(WorkList workList);

    List<WorkListDTO> workListsToWorkListDTOs(List<WorkList> workLists);

    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    WorkList workListDTOToWorkList(WorkListDTO workListDTO);

    List<WorkList> workListDTOsToWorkLists(List<WorkListDTO> workListDTOs);
}
