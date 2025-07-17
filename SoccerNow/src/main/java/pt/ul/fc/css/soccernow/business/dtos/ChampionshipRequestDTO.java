package pt.ul.fc.css.soccernow.business.dtos;

import java.util.List;

public class ChampionshipRequestDTO {

  private String name;
  private List<Long> teams;

  public ChampionshipRequestDTO() {}

  public ChampionshipRequestDTO(String name, List<Long> teams) {
    this.name = name;
    this.teams = teams;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Long> getTeams() {
    return teams;
  }

  public void setTeams(List<Long> teams) {
    this.teams = teams;
  }
}
