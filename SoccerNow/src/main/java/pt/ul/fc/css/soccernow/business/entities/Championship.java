package pt.ul.fc.css.soccernow.business.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Championship {

  @Id @GeneratedValue private long id;

  private String name;

  private boolean isFinished = false;

  @OneToMany(mappedBy = "championship")
  private List<PodiumPosition> podiumPositions = new ArrayList<>();

  @OneToMany(mappedBy = "championship")
  private List<Game> games = new ArrayList<>();

  @ManyToMany
  @JoinTable(
      name = "championship_teams",
      joinColumns = @JoinColumn(name = "championship_id"),
      inverseJoinColumns = @JoinColumn(name = "team_id"))
  private List<Team> teams = new ArrayList<>();

  private boolean isActive = true;

  public Championship() {}

  public Championship(String name, List<Team> teams) {
    this.name = name;
    this.teams = teams;
  }

  public long getId() {
    return this.id;
  }

  public void setId(long championshipId) {
    this.id = championshipId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<PodiumPosition> getPodiumPositions() {
    return podiumPositions;
  }

  public void setPodiumPositions(List<PodiumPosition> podiumPositions) {
    this.podiumPositions = podiumPositions;
  }

  public List<Game> getGames() {
    return games;
  }

  public void setGames(List<Game> championshipGames) {
    this.games = championshipGames;
  }

  public List<Team> getTeams() {
    return teams;
  }

  public void setTeams(List<Team> teams) {
    this.teams = teams;
  }

  public void addGame(Game game) {
    this.games.add(game);
  }

  public void removeGame(Game game) {
    this.games.remove(game);
  }

  public void addTeam(Team team) {
    this.teams.add(team);
  }

  public void removeTeam(Team team) {
    this.teams.remove(team);
  }

  public void addPodiumPosition(PodiumPosition podiumPosition) {
    this.podiumPositions.add(podiumPosition);
  }

  public void removePodiumPosition(PodiumPosition podiumPosition) {
    this.podiumPositions.remove(podiumPosition);
  }

  public void setActive(boolean isActive) {
    this.isActive = isActive;
  }

  public boolean isActive() {
    return isActive;
  }

  public boolean isFinished() {
    return isFinished;
  }

  public void setFinished(boolean isFinished) {
    this.isFinished = isFinished;
  }

  public List<Game> getGamesToPlay(){
    List<Game> gamesToPlay = new ArrayList<>();
    for(Game g : games){
      if(g.isActive() && !g.isFinished()){
        gamesToPlay.add(g);
      }
    }
    return gamesToPlay;
  }

}
