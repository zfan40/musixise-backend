package musixise.web.rest.mapper;

import musixise.domain.*;
import musixise.web.rest.dto.MusixiserDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Musixiser and its DTO MusixiserDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MusixiserMapper {

    MusixiserDTO musixiserToMusixiserDTO(Musixiser musixiser);

    List<MusixiserDTO> musixisersToMusixiserDTOs(List<Musixiser> musixisers);

    Musixiser musixiserDTOToMusixiser(MusixiserDTO musixiserDTO);

    List<Musixiser> musixiserDTOsToMusixisers(List<MusixiserDTO> musixiserDTOs);
}
