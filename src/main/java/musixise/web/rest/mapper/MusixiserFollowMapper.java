package musixise.web.rest.mapper;

import musixise.domain.*;
import musixise.web.rest.dto.MusixiserFollowDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity MusixiserFollow and its DTO MusixiserFollowDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MusixiserFollowMapper {

    MusixiserFollowDTO musixiserFollowToMusixiserFollowDTO(MusixiserFollow musixiserFollow);

    List<MusixiserFollowDTO> musixiserFollowsToMusixiserFollowDTOs(List<MusixiserFollow> musixiserFollows);

    MusixiserFollow musixiserFollowDTOToMusixiserFollow(MusixiserFollowDTO musixiserFollowDTO);

    List<MusixiserFollow> musixiserFollowDTOsToMusixiserFollows(List<MusixiserFollowDTO> musixiserFollowDTOs);
}
