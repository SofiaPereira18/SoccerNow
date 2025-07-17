package pt.ul.fc.css.soccernow.business.mappers;

import pt.ul.fc.css.soccernow.business.dtos.TeamDTO;
import pt.ul.fc.css.soccernow.business.entities.Team;
import pt.ul.fc.css.soccernow.business.handlers.TeamHandler;

public class TeamMapper {

  private static TeamHandler tHandler;

  public static TeamDTO toDTO(Team team) {
    if (team == null) {
      return null;
    }
    return new TeamDTO(
        team.getId(),
        team.getName(),
        team.getPlayersId(),
        team.getPodiumPositionsId(),
        team.getStartingTeamId(),
        team.getChampionshipId());
  }

  public static Team toEntity(TeamDTO teamDTO) {
    if (teamDTO == null) {
      return null;
    }
    return tHandler.getTeamById(teamDTO.getId());
  }
}
