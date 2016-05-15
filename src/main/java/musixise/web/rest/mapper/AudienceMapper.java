package musixise.web.rest.mapper;

import musixise.domain.*;
import musixise.web.rest.dto.AudienceDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Audience and its DTO AudienceDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AudienceMapper {

    AudienceDTO audienceToAudienceDTO(Audience audience);

    List<AudienceDTO> audiencesToAudienceDTOs(List<Audience> audiences);

    Audience audienceDTOToAudience(AudienceDTO audienceDTO);

    List<Audience> audienceDTOsToAudiences(List<AudienceDTO> audienceDTOs);
}
