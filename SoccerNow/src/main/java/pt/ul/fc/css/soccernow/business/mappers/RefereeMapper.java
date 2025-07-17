package pt.ul.fc.css.soccernow.business.mappers;

import java.util.ArrayList;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.ul.fc.css.soccernow.business.dtos.RefereeDTO;
import pt.ul.fc.css.soccernow.business.entities.Referee;
import pt.ul.fc.css.soccernow.business.entities.RefereeTeam;
import pt.ul.fc.css.soccernow.business.handlers.UserHandler;

/**
 * Mapper for Referee entity.
 *
 * @author Ana Morgado 56957
 * @author João Azevedo 56913
 * @author Sofia Pereira 56352
 */
@Component
public class RefereeMapper {

  private static UserHandler userHandler;

  @Autowired
  public RefereeMapper(UserHandler userHandler) {
    RefereeMapper.userHandler = userHandler;
  }

  /**
   * Converts the Referee entity into a RefereeDTO.
   *
   * @return RefereeDTO object with relevant data.
   */
  public static RefereeDTO toDTO(Referee referee) {
    if (referee == null) return null;

    RefereeDTO dto = new RefereeDTO();
    dto.setId(referee.getId());
    dto.setFirstName(referee.getFirstName());
    dto.setLastName(referee.getLastName());
    dto.setHasCertificate(referee.hasCertificate());
    dto.setIsActive(referee.getIsActive());

    dto.setRefereeTeams(
        referee.getRefereeTeams() != null
            ? referee.getRefereeTeams().stream()
                .map(RefereeTeam::getId)
                .collect(Collectors.toList())
            : new ArrayList<>());

    return dto;
  }

  /**
   * Converts the RefereeDTO into an Referee.
   *
   * @return Referee object with relevant data.
   */
  public static Referee toEntity(RefereeDTO dto) {
    if (dto == null) return null;

    return userHandler.getRefereeById(dto.getId());
  }
}
