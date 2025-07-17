package pt.ul.fc.css.soccernow.business.entities;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Team {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  private String name;

  @ManyToMany private List<Player> players;

  @OneToMany(mappedBy = "team")
  private List<PodiumPosition> podiumPositions;

  @OneToMany(mappedBy = "team")
  private List<StartingTeam> startingTeam;

  @ManyToMany(mappedBy = "teams")
  private List<Championship> championships;

  private boolean isActive;

  public Team() {
    this.isActive = true;
  }

  public Team(String name) {
    this.name = name;
    this.isActive = true;
  }

  public Team(
      String name,
      List<Player> players,
      List<PodiumPosition> podiumPositions,
      List<StartingTeam> startingTeams,
      List<Championship> championships) {
    this.name = name;
    this.players = players;
    this.podiumPositions = podiumPositions;
    this.startingTeam = startingTeams;
    this.championships = championships;
    this.isActive = true;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Player> getPlayers() {
    return players;
  }

  public void setPlayers(List<Player> players) {
    this.players = players;
  }

  public List<PodiumPosition> getPodiumPositions() {
    return podiumPositions;
  }

  public void setPodiumPositions(List<PodiumPosition> podiumPositions) {
    this.podiumPositions = podiumPositions;
  }

  public List<StartingTeam> getStartingTeam() {
    return startingTeam;
  }

  public void setStartingTeam(List<StartingTeam> startingTeam) {
    this.startingTeam = startingTeam;
  }

  public long getId() {
    return id;
  }

  public List<Long> getStartingTeamId() {
    if (startingTeam != null) {
      return startingTeam.stream().map(t -> t.getId()).collect(Collectors.toList());
    }
    return null;
  }

  public List<Long> getChampionshipId() {
    if (championships != null) {
      return championships.stream().map(t -> t.getId()).collect(Collectors.toList());
    }
    return null;
  }

  public List<Long> getPodiumPositionsId() {
    if (podiumPositions != null) {
      return podiumPositions.stream().map(t -> t.getId()).collect(Collectors.toList());
    }
    return null;
  }

  public List<Long> getPlayersId() {
    if (players != null) {
      return players.stream().map(t -> t.getId()).collect(Collectors.toList());
    }
    return null;
  }

  public void setId(long id) {
    this.id = id;
  }

  public boolean isActive() {
    return isActive;
  }

  public void setActive(boolean isActive) {
    this.isActive = isActive;
  }

  public void addStartingTeam(StartingTeam st) {
    this.startingTeam.add(st);
  }

  public void addPlayer(Player player) {
    this.players.add(player);
  }

  public void removePlayer(Player player) {
    this.players.remove(player);
  }

  public void removeStartingTeam(StartingTeam st) {
    this.startingTeam.remove(st);
  }

  public List<Championship> getChampionships() {
    return championships;
  }

  public void setChampionships(List<Championship> championships) {
    this.championships = championships;
  }

  public void addChampionship(Championship championship) {
    this.championships.add(championship);
  }

  public void removeChampionship(Championship championship) {
    this.championships.remove(championship);
  }

  public List<Championship> getChampionshipsNotFinished() {
    List<Championship> champsNF = new ArrayList<>();
    for (Championship c : championships) {
      if (c.isActive() && !c.isFinished()) {
        champsNF.add(c);
      }
    }
    return champsNF;
  }
}
