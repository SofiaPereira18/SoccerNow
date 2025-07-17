package pt.ul.fc.css.soccernow.business.entities;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyJoinColumn;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
public class ChampionshipByPoints extends Championship {

  @ElementCollection
  @CollectionTable(name = "pointByTeam", joinColumns = @JoinColumn(name = "championship_id"))
  @MapKeyJoinColumn(name = "team_id")
  @Column(name = "points")
  private Map<Long, Integer> pointsByTeam = new HashMap<>();

  /** Default constructor for the ChampionshipByPoints class. */
  public ChampionshipByPoints() {}

  /**
   * Constructs a new championship by points.
   *
   * @param name the name of the championship
   * @param podiumPositions the list of podium positions
   * @param championshipGames the list of games in the championship
   * @param teams the list of teams in the championship
   */
  public ChampionshipByPoints(String name, List<Team> teams) {
    super(name, teams);
    for (Team team : teams) {
      pointsByTeam.put(team.getId(), 0);
    }
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

  public Long getWinner() {
    return pointsByTeam.entrySet().stream()
        .max(Map.Entry.comparingByValue())
        .map(Map.Entry::getKey)
        .orElse(null);
  }

  public Long getSecond() {
    return pointsByTeam.entrySet().stream()
        .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
        .skip(1)
        .map(Map.Entry::getKey)
        .findFirst()
        .orElse(null);
  }

  public Long getThird() {
    return pointsByTeam.entrySet().stream()
        .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
        .skip(2)
        .map(Map.Entry::getKey)
        .findFirst()
        .orElse(null);
  }
}
