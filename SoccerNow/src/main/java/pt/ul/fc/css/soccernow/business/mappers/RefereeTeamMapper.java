package pt.ul.fc.css.soccernow.business.mappers;

import java.util.ArrayList;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.ul.fc.css.soccernow.business.dtos.RefereeTeamDTO;
import pt.ul.fc.css.soccernow.business.entities.Game;
import pt.ul.fc.css.soccernow.business.entities.Referee;
import pt.ul.fc.css.soccernow.business.entities.RefereeTeam;
import pt.ul.fc.css.soccernow.business.handlers.RefereeTeamHandler;

/**
 * Mapper for RefereeTeam entity.
 *
 * @author Ana Morgado 56957
 * @author João Azevedo 56913
 * @author Sofia Pereira 56352
 */
@Component
public class RefereeTeamMapper {

  private static RefereeTeamHandler refereeTeamHandler;

  @Autowired
  public RefereeTeamMapper(RefereeTeamHandler refereeTeamHandler) {
    RefereeTeamMapper.refereeTeamHandler = refereeTeamHandler;
  }

  /**
   * Converts the RefereeTeam entity into a RefereeTeamDTO.
   *
   * @return RefereeTeamDTO object with relevant data.
   */
  public static RefereeTeamDTO toDTO(RefereeTeam refTeam) {
    RefereeTeamDTO dto = new RefereeTeamDTO();
    dto.setId(refTeam.getId());
    dto.setIsChampionshipGame(refTeam.isChampionshipGame());
    dto.setMainReferee(refTeam.getMainReferee() != null ? refTeam.getMainReferee().getId() : null);
    dto.setIsActive(refTeam.getIsActive());

    dto.setReferees(
        refTeam.getReferees() != null
            ? refTeam.getReferees().stream().map(Referee::getId).collect(Collectors.toList())
            : new ArrayList<>());

    dto.setGames(
        refTeam.getGames() != null
            ? refTeam.getGames().stream().map(Game::getId).collect(Collectors.toList())
            : new ArrayList<>());

    return dto;
  }

  /**
   * Converts the RefereeTeamDTO into an RefereeTeam.
   *
   * @return RefereeTeam object with relevant data.
   */
  public static RefereeTeam toEntity(RefereeTeamDTO dto) {
    if (dto == null) return null;

    return refereeTeamHandler.getRefereeTeamById(dto.getId());
  }
}
