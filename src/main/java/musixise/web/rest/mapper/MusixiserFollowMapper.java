package musixise.web.rest.mapper;

import musixise.domain.MusixiserFollow;
import musixise.web.rest.dto.MusixiserFollowDTO;
import musixise.web.rest.dto.MusixiserFollowerDTO;
import musixise.web.rest.dto.MusixiserFollowingDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * Mapper for the entity MusixiserFollow and its DTO MusixiserFollowDTO.
 */
@Mapper(componentModel = "spring", uses = {DateMapper.class, MusixiserMapper.class})
public interface MusixiserFollowMapper {

    MusixiserFollowDTO musixiserFollowToMusixiserFollowDTO(MusixiserFollow musixiserFollow);

    List<MusixiserFollowDTO> musixiserFollowsToMusixiserFollowDTOs(List<MusixiserFollow> musixiserFollows);

    @Mapping(target = "createdDate", ignore= true)
    MusixiserFollow musixiserFollowDTOToMusixiserFollow(MusixiserFollowDTO musixiserFollowDTO);

    List<MusixiserFollow> musixiserFollowDTOsToMusixiserFollows(List<MusixiserFollowDTO> musixiserFollowDTOs);

    @Mappings({
        @Mapping(target = "userId", source = "musixiserFollow.follower.userId"),
        @Mapping(target = "realname", source = "musixiserFollow.follower.realname"),
        @Mapping(target = "smallAvatar", source = "musixiserFollow.follower.smallAvatar"),
        @Mapping(target = "largeAvatar", source = "musixiserFollow.follower.largeAvatar"),
    })
    MusixiserFollowerDTO musixiserFollowToMusixiserFollowerDTO(MusixiserFollow musixiserFollow);
    List<MusixiserFollowerDTO> musixiserFollowsToMusixiserFollowerDTOs(List<MusixiserFollow> musixiserFollows);

    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "userId", source = "musixiserFollow.musixiser.userId"),
        @Mapping(target = "realname", source = "musixiserFollow.musixiser.realname"),
        @Mapping(target = "smallAvatar", source = "musixiserFollow.musixiser.smallAvatar"),
        @Mapping(target = "largeAvatar", source = "musixiserFollow.musixiser.largeAvatar"),
    })
    MusixiserFollowingDTO musixiserFollowToMusixiserFollowingDTO(MusixiserFollow musixiserFollow);

    List<MusixiserFollowingDTO> musixiserFollowsToMusixiserFollowingDTOs(List<MusixiserFollow> musixiserFollows);

}
