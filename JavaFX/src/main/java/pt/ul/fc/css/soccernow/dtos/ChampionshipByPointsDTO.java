package pt.ul.fc.css.soccernow.dtos;

import java.util.List;
import java.util.Map;

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
