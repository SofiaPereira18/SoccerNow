package pt.ul.fc.css.soccernow.dtos;

import java.util.ArrayList;
import java.util.List;

public class TableChampionshipDTO {

  private long id;
  private String name;
  private List<String> podiumPositionsNames;
  private List<String> gamesInfo;
  private List<String> teamsNames;
  private String status;

  public TableChampionshipDTO() {}

  public TableChampionshipDTO(
      long id,
      String name,
      List<String> podiumPositions,
      List<String> gamesInfo,
      List<String> teamNames,
      String status) {
    this.id = id;
    this.name = name;
    this.podiumPositionsNames = podiumPositions;
    this.gamesInfo = gamesInfo;
    this.teamsNames = teamNames;
    this.status = status;
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

  public List<String> getPodiumPositionsNames() {
    return podiumPositionsNames;
  }

  public void setPodiumPositionsNames(List<String> podiumPositionsNames) {
    this.podiumPositionsNames = podiumPositionsNames;
  }

  public List<String> getTeamsNames() {
    return teamsNames;
  }

  public void setTeamsNames(List<String> teamsNames) {
    this.teamsNames = teamsNames;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public List<String> getGamesInfo() {
    return gamesInfo;
  }

  public void setGamesInfo(List<String> gamesInfo) {
    this.gamesInfo = gamesInfo;
  }

  public String getTeamsNamesString() {
    return String.join("\n", getTeamsNames());
  }

  public String getGamesInfoString() {
    return String.join("\n", getGamesInfo());
  }

  public String getPodiumPositionsString() {
    List<String> podiumPositions = new ArrayList<>();
    for (int i = 0; i < getPodiumPositionsNames().size(); i++) {
      String name = getPodiumPositionsNames().get(i);
      if (name == null) name = "N/A";
      podiumPositions.add((i + 1) + "º: " + name);
    }
    return String.join("\n", podiumPositions);
  }
}
