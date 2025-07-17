package pt.ul.fc.css.soccernow.business.mappers;

import java.util.ArrayList;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.ul.fc.css.soccernow.business.dtos.PlayerDTO;
import pt.ul.fc.css.soccernow.business.entities.Player;
import pt.ul.fc.css.soccernow.business.entities.StartingTeam;
import pt.ul.fc.css.soccernow.business.entities.Team;
import pt.ul.fc.css.soccernow.business.handlers.UserHandler;

/**
 * Mapper for Player entity.
 *
 * @author Ana Morgado 56957
 * @author João Azevedo 56913
 * @author Sofia Pereira 56352
 */
@Component
public class PlayerMapper {

  private static UserHandler userHandler;

  @Autowired
  public PlayerMapper(UserHandler userHandler) {
    PlayerMapper.userHandler = userHandler;
  }

  /**
   * Converts the Player entity into a PlayerDTO.
   *
   * @return PlayerDTO object with relevant data.
   */
  public static PlayerDTO toDTO(Player player) {
    PlayerDTO dto = new PlayerDTO();
    dto.setId(player.getId());
    dto.setFirstName(player.getFirstName());
    dto.setLastName(player.getLastName());
    dto.setPosition(player.getPosition());
    dto.setIsActive(player.getIsActive());

    dto.setTeams(
        player.getTeams() != null
            ? player.getTeams().stream().map(Team::getId).collect(Collectors.toList())
            : new ArrayList<>());

    dto.setStartingTeams(
        player.getStartingTeams() != null
            ? player.getStartingTeams().stream()
                .map(StartingTeam::getId)
                .collect(Collectors.toList())
            : new ArrayList<>());

    return dto;
  }

  /**
   * Converts the PlayerDTO into a Player.
   *
   * @return Player object with relevant data.
   */
  public static Player toEntity(PlayerDTO dto) {
    if (dto == null) return null;

    return userHandler.getPlayerById(dto.getId());
  }
}
