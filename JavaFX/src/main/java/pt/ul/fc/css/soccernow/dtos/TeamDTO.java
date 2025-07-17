package pt.ul.fc.css.soccernow.dtos;

import java.util.List;

public class TeamDTO {

  private long id;
  private String name;
  private List<Long> playersId;
  private List<Long> podiumPositionsId;
  private List<Long> startingTeamId;
  private List<Long> championshipsId;

  public TeamDTO() {
  }

  public TeamDTO(
      Long id,
      String name,
      List<Long> playersId,
      List<Long> podiumPositionsId,
      List<Long> startingTeamId,
      List<Long> championshipsId) {
    this.id = id;
    this.name = name;
    this.playersId = playersId;
    this.podiumPositionsId = podiumPositionsId;
    this.startingTeamId = startingTeamId;
    this.championshipsId = championshipsId;
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

  public List<Long> getPlayersId() {
    return playersId;
  }

  public void setPlayers(List<Long> playersId) {
    this.playersId = playersId;
  }

  public List<Long> getPodiumPositions() {
    return podiumPositionsId;
  }

  public void setPodiumPositions(List<Long> podiumPositionsId) {
    this.podiumPositionsId = podiumPositionsId;
  }

  public List<Long> getStartingTeam() {
    return startingTeamId;
  }

  public void setStartingTeam(List<Long> startingTeamId) {
    this.startingTeamId = startingTeamId;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Long> getChampionshipsId() {
    return championshipsId;
  }

  public void setChampionshipsId(List<Long> championshipsId) {
    this.championshipsId = championshipsId;
  }
}
