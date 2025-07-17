package pt.ul.fc.css.soccernow.business.dtos;

import java.util.List;

public class ChampionshipDTO {

  private long id;
  private String name;
  private List<Long> podiumPositionsIds;
  private List<Long> gamesIds;
  private List<Long> teamsIds;
  private Boolean isFinished;

  public ChampionshipDTO() {}

  public ChampionshipDTO(
      long id,
      String name,
      List<Long> podiumPositionsIds,
      List<Long> gamesIds,
      List<Long> teamsIds,
      Boolean isFinished) {
    this.id = id;
    this.name = name;
    this.podiumPositionsIds = podiumPositionsIds;
    this.gamesIds = gamesIds;
    this.teamsIds = teamsIds;
    this.isFinished = isFinished;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Long> getPodiumPositionsIds() {
    return podiumPositionsIds;
  }

  public void setPodiumPositionsIds(List<Long> podiumPositionsIds) {
    this.podiumPositionsIds = podiumPositionsIds;
  }

  public List<Long> getGamesIds() {
    return gamesIds;
  }

  public void setGamesIds(List<Long> gamesIds) {
    this.gamesIds = gamesIds;
  }

  public List<Long> getTeamsIds() {
    return teamsIds;
  }

  public void setTeamsIds(List<Long> teamsIds) {
    this.teamsIds = teamsIds;
  }

  public Boolean getIsFinished() {
    return isFinished;
  }

  public void setIsFinished(Boolean isFinished) {
    this.isFinished = isFinished;
  }
}
