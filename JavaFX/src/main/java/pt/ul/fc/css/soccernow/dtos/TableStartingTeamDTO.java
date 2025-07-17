package pt.ul.fc.css.soccernow.dtos;

import java.util.ArrayList;
import java.util.List;

public class TableStartingTeamDTO {
  private Long id;
  private String teamId;
  private List<Long> gamesId = new ArrayList<>();
  private List<String> playersIds;
  private String goalkeeperId;

  public TableStartingTeamDTO() {}

  public TableStartingTeamDTO(
      Long id, String teamId, List<String> playersIds, String goalkeeperId, List<Long> gamesId) {
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

  public String getTeamId() {
    return teamId;
  }

  public void setTeamId(String teamId) {
    this.teamId = teamId;
  }

  public List<Long> getGamesId() {
    return gamesId;
  }

  public void setGamesId(List<Long> gamesId) {
    this.gamesId = gamesId;
  }

  public List<String> getPlayersIds() {
    return playersIds;
  }

  public void setPlayersIds(List<String> playersIds) {
    this.playersIds = playersIds;
  }

  public String getGoalkeeperId() {
    return goalkeeperId;
  }

  public void setGoalkeeperId(String goalkeeperId) {
    this.goalkeeperId = goalkeeperId;
  }
}
