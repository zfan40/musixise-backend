package musixise.web.rest.mapper;

import musixise.domain.MusixiserFollow;
import musixise.web.rest.dto.MusixiserFollowDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * Mapper for the entity MusixiserFollow and its DTO MusixiserFollowDTO.
 */
@Mapper(componentModel = "spring", uses = {DateMapper.class, MusixiserMapper.class})
public interface MusixiserFollowMapper {

    @Mappings({
        @Mapping(target = "followId", source = "musixiserFollow.musixiser.userId"),
        @Mapping(target = "realname", source = "musixiserFollow.musixiser.realname"),
        @Mapping(target = "smallAvatar", source = "musixiserFollow.musixiser.smallAvatar"),
        @Mapping(target = "largeAvatar", source = "musixiserFollow.musixiser.largeAvatar"),
    })
    MusixiserFollowDTO musixiserFollowToMusixiserFollowDTO(MusixiserFollow musixiserFollow);

    List<MusixiserFollowDTO> musixiserFollowsToMusixiserFollowDTOs(List<MusixiserFollow> musixiserFollows);

    @Mapping(target = "createdDate", ignore= true)
    MusixiserFollow musixiserFollowDTOToMusixiserFollow(MusixiserFollowDTO musixiserFollowDTO);

    List<MusixiserFollow> musixiserFollowDTOsToMusixiserFollows(List<MusixiserFollowDTO> musixiserFollowDTOs);


}
