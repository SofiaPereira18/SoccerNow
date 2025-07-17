package pt.ul.fc.css.soccernow.dtos;

import java.util.List;

public class TableTeamDTO {

  private long id;
  private String name;
  private List<String> playersId;
  private List<String> podiumPositionsId;
  private List<Long> startingTeamId;
  private List<String> championshipsId;
  private List<Long> gamesId;

  public TableTeamDTO() {}

  public TableTeamDTO(
      Long id,
      String name,
      List<String> playersId,
      List<String> podiumPositionsId,
      List<Long> startingTeamId,
      List<String> championshipsId,
      List<Long> gamesId) {
    this.id = id;
    this.name = name;
    this.playersId = playersId;
    this.podiumPositionsId = podiumPositionsId;
    this.startingTeamId = startingTeamId;
    this.championshipsId = championshipsId;
    this.gamesId = gamesId;
  }

  public void setPlayersId(List<String> playersId) {
    this.playersId = playersId;
  }

  public List<String> getPodiumPositionsId() {
    return podiumPositionsId;
  }

  public void setPodiumPositionsId(List<String> podiumPositionsId) {
    this.podiumPositionsId = podiumPositionsId;
  }

  public List<Long> getStartingTeamId() {
    return startingTeamId;
  }

  public void setStartingTeamId(List<Long> startingTeamId) {
    this.startingTeamId = startingTeamId;
  }

  public List<Long> getGamesId() {
    return gamesId;
  }

  public void setGamesId(List<Long> gamesId) {
    this.gamesId = gamesId;
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

  public List<String> getPlayersId() {
    return playersId;
  }

  public void setPlayers(List<String> playersId) {
    this.playersId = playersId;
  }

  public List<String> getPodiumPositions() {
    return podiumPositionsId;
  }

  public void setPodiumPositions(List<String> podiumPositionsId) {
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

  public List<String> getChampionshipsId() {
    return championshipsId;
  }

  public void setChampionshipsId(List<String> championshipsId) {
    this.championshipsId = championshipsId;
  }
}
