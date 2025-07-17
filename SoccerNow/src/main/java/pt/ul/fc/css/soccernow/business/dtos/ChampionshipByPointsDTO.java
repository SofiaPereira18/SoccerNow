package pt.ul.fc.css.soccernow.business.dtos;

import java.util.List;
import java.util.Map;
import pt.ul.fc.css.soccernow.business.entities.ChampionshipByPoints;
import pt.ul.fc.css.soccernow.business.entities.Game;
import pt.ul.fc.css.soccernow.business.entities.PodiumPosition;
import pt.ul.fc.css.soccernow.business.entities.Team;

public class ChampionshipByPointsDTO extends ChampionshipDTO {

  private Map<Long, Integer> pointsByTeam;

  /** Default constructor for the ChampionshipByPoints class. */
  public ChampionshipByPointsDTO() {}

  /** Constructor for the ChampionshipByPoints class. */
  public ChampionshipByPointsDTO(
      long id,
      String name,
      List<Long> podiumPositions,
      List<Long> games,
      List<Long> teams,
      Map<Long, Integer> pointsByTeam,
      Boolean isFinished) {
    super(id, name, podiumPositions, games, teams, isFinished);
    this.pointsByTeam = pointsByTeam;
  }

  public ChampionshipByPointsDTO(ChampionshipByPoints championship) {
    super(
        championship.getId(),
        championship.getName(),
        championship.getPodiumPositions().stream().map(PodiumPosition::getId).toList(),
        championship.getGames().stream().map(Game::getId).toList(),
        championship.getTeams().stream().map(Team::getId).toList(),
        championship.isFinished());
    this.pointsByTeam = championship.getPointsByTeam();
  }

  public Map<Long, Integer> getPointsByTeam() {
    return pointsByTeam;
  }

  public void setPointsByTeam(Map<Long, Integer> pointsByTeam) {
    this.pointsByTeam = pointsByTeam;
  }

  public void addPointsToTeam(long teamId, int points) {
    pointsByTeam.put(teamId, pointsByTeam.getOrDefault(teamId, 0) + points);
  }
}
