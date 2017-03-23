package musixise.web.rest.mapper;

import musixise.domain.WorkList;
import musixise.utils.StringUtil;
import musixise.utils.DateUtil;
import musixise.web.rest.dto.WorkListDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper for the entity WorkList and its DTO WorkListDTO.
 */
@Mapper(componentModel = "spring", uses = {DateMapper.class})
public interface WorkListMapper {


    //WorkListDTO workListToWorkListDTO(WorkList workList);

    List<WorkListDTO> workListsToWorkListDTOs(List<WorkList> workLists);

    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    //@Mapping(target = "lastModifiedDate", ignore = true)
    WorkList workListDTOToWorkList(WorkListDTO workListDTO);

    List<WorkList> workListDTOsToWorkLists(List<WorkListDTO> workListDTOs);

    default WorkListDTO workListToWorkListDTO(WorkList workList) {
        WorkListDTO workListDTO = new WorkListDTO();

        workListDTO.setCollectNum( workList.getCollectNum() );
        workListDTO.setTitle( workList.getTitle() );
        workListDTO.setCover( workList.getCover() );
        workListDTO.setStatus( workList.getStatus() );
        workListDTO.setId( workList.getId() );
        workListDTO.setContent( workList.getContent() );
        workListDTO.setUrl( workList.getUrl() );
        workListDTO.setUserId( workList.getUserId() );
        workListDTO.setFileHash(StringUtil.getMD5(workList.getUrl()));
        workListDTO.setCreatedDate(DateUtil.asDate(workList.getCreatedDate()));
        workListDTO.setLastModifiedDate(DateUtil.asDate(workList.getLastModifiedDate()));

        return workListDTO;
    }

}
