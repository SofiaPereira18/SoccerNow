package pt.ul.fc.css.soccernow.business.mappers;

import pt.ul.fc.css.soccernow.business.dtos.StartingTeamDTO;
import pt.ul.fc.css.soccernow.business.entities.StartingTeam;
import pt.ul.fc.css.soccernow.business.handlers.TeamHandler;

public class StartingTeamMapper {

  private static TeamHandler tHandler;

  public static StartingTeamDTO toDTO(StartingTeam startingTeam) {
    if (startingTeam == null) {
      return null;
    }
    return new StartingTeamDTO(
        startingTeam.getId(),
        startingTeam.getTeam().getId(),
        startingTeam.getPlayers().stream().map(p -> p.getId()).toList(),
        startingTeam.getGoalkeeper().getId(),
        startingTeam.getGames() != null
            ? startingTeam.getGames().stream()
                .map(
                    g -> {
                      if (g.isActive()) {
                        return g.getId();
                      } else {
                        return null;
                      }
                    })
                .toList()
            : null);
  }

  public static StartingTeam toEntity(StartingTeamDTO startingTeamDTO) {
    if (startingTeamDTO == null) {
      return null;
    }
    return tHandler.getStartingTeamById(startingTeamDTO.getId());
  }
}
