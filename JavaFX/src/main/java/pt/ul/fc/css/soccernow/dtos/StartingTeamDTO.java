package pt.ul.fc.css.soccernow.dtos;

import java.util.ArrayList;
import java.util.List;

public class StartingTeamDTO {
  private Long id;
  private Long teamId;
  private List<Long> gamesId = new ArrayList<>();
  private List<Long> playersIds;
  private Long goalkeeperId;

  public StartingTeamDTO() {}

  public StartingTeamDTO(
      Long id, Long teamId, List<Long> playersIds, Long goalkeeperId, List<Long> gamesId) {
    this.id = id;
    this.teamId = teamId;
    this.playersIds = playersIds;
    this.goalkeeperId = goalkeeperId;
    this.gamesId = gamesId;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getTeamId() {
    return teamId;
  }

  public void setTeamId(Long teamId) {
    this.teamId = teamId;
  }

  public List<Long> getGamesId() {
    return gamesId;
  }

  public void setGamesId(List<Long> gamesId) {
    this.gamesId = gamesId;
  }

  public List<Long> getPlayersIds() {
    return playersIds;
  }

  public void setPlayersIds(List<Long> playersIds) {
    this.playersIds = playersIds;
  }

  public Long getGoalkeeperId() {
    return goalkeeperId;
  }

  public void setGoalkeeperId(Long goalkeeperId) {
    this.goalkeeperId = goalkeeperId;
  }
}
