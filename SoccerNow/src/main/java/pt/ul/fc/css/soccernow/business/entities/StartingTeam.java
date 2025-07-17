package pt.ul.fc.css.soccernow.business.entities;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class StartingTeam {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ManyToOne private Team team;

  @ManyToMany(mappedBy = "startingTeams")
  private List<Game> games;

  @ManyToMany(mappedBy = "startingTeams")
  private List<Player> players;

  @ManyToOne private Player goalkeeper;

  private boolean isActive;

  public static final Long NUM_PLAYER = 4L;

  public StartingTeam() {
    this.isActive = true;
  }

  public StartingTeam(Team team, List<Player> players, Player goalkeeper) {
    this.team = team;
    this.players = players;
    this.goalkeeper = goalkeeper;
    this.isActive = true;
    this.games = new ArrayList<Game>();
  }

  public Team getTeam() {
    return team;
  }

  public void setTeam(Team team) {
    this.team = team;
  }

  public List<Player> getPlayers() {
    return players;
  }

  public void setPlayers(List<Player> players) {
    this.players = players;
  }

  public Player getGoalkeeper() {
    return goalkeeper;
  }

  public void setGoalkeeper(Player goalkeeper) {
    this.goalkeeper = goalkeeper;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public List<Game> getGames() {
    if (games != null) {
      return games;
    }
    return null;
  }

  public void addGame(Game game) {
    this.games.add(game);
  }

  public void setGames(List<Game> games) {
    this.games = games;
  }

  public boolean isActive() {
    return isActive;
  }

  public void setActive(boolean isActive) {
    this.isActive = isActive;
  }

  public void addPlayer(Player player) {
    if (players.size() < NUM_PLAYER) {
      players.add(player);
    } else {
      throw new IllegalStateException("Cannot add more players to the starting team");
    }
  }

  public void addGK(Player player) {
    this.goalkeeper = player;
  }

  public void removePlayer(Player p) {
    if (players.contains(p)) {
      players.remove(p);
    } else {
      throw new IllegalStateException("Player not in starting team");
    }
  }

  public void removeGK() {
    if (goalkeeper != null) {
      goalkeeper = null;
    } else {
      throw new IllegalStateException("No goalkeeper in starting team");
    }
  }

  public static Long getNumPlayer() {
    return NUM_PLAYER;
  }

  public List<Game> getAllToPlayGames() {
    List<Game> gamesToPlay = new ArrayList<>();
    for (Game game : games) {
      if (game.isActive() && !game.isFinished()) {
        gamesToPlay.add(game);
      }
    }
    return gamesToPlay;
  }
}
