package pt.ul.fc.css.soccernow.business.entities;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an player.
 *
 * @author Ana Morgado 56957
 * @author João Azevedo 56913
 * @author Sofia Pereira 56352
 */
@Entity
public class Player extends User {

  /** Enumeration of possible player positions. */
  public enum PlayerPosition {
    GOALKEEPER,
    OTHER
  }

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Enumerated(EnumType.STRING)
  private PlayerPosition position;

  @ManyToMany(mappedBy = "players")
  private List<Team> teams;

  @ManyToMany
  @JoinTable(
      name = "player_starting_team",
      joinColumns = @JoinColumn(name = "player_id"),
      inverseJoinColumns = @JoinColumn(name = "starting_team_id")) 
    private List<StartingTeam> startingTeams;

  /**
   * Constructs a new player with the given name, position, and teams.
   *
   * @param firstName the first name of the player
   * @param lastName the last name of the player
   * @param positions the positions the player can play
   * @param teams the teams the player belongs to
   * @param startingTeams the starting teams the player belongs to
   */
  public Player(
      String firstName,
      String lastName,
      PlayerPosition position,
      List<Team> teams,
      List<StartingTeam> startingTeams) {

    super(firstName, lastName);
    this.position = position;
    this.teams = teams;
    this.startingTeams = startingTeams;
  }

  /** No-argument constructor. */
  public Player() {
    this.teams = new ArrayList<>();
    this.startingTeams = new ArrayList<>();
  }

  /**
   * Adds the player to an team.
   *
   * @param team the team to be added
   */
  public void addTeam(Team team) {
    if (team != null) this.teams.add(team);
  }

  /**
   * Removes the player of the team.
   *
   * @param team the team to be removed
   */
  public void removeTeam(Team team) {
    if (team != null) this.teams.remove(team);
  }

  /**
   * Adds the player to an starting team.
   *
   * @param team the starting team to be added
   */
  public void addTeam(StartingTeam team) {
    if (team != null) this.startingTeams.add(team);
  }

  /**
   * Removes the player of the starting team.
   *
   * @param team the starting team to be added
   */
  public void removeStartingTeam(StartingTeam st) {
    if (st != null) this.startingTeams.remove(st);
  }

  // getters

  /**
   * Gets the list of the starting elevens the player belongs to
   *
   * @return the starting elevens the player belongs to
   */
  public List<StartingTeam> getStartingTeams() {
    return startingTeams;
  }

  /**
   * Gets the position the player can play.
   *
   * @return the position the player can play
   */
  public PlayerPosition getPosition() {
    return position;
  }

  /**
   * Gets the list of teams the player belongs to.
   *
   * @return the list of teams
   */
  public List<Team> getTeams() {
    return teams;
  }

  // setters

  /**
   * Sets the position the player can play.
   *
   * @param positions the position
   */
  public void setPosition(PlayerPosition position) {
    this.position = position;
  }

  /**
   * Sets the list of teams the player belongs to.
   *
   * @param teams the list of teams
   */
  public void setTeams(List<Team> teams) {
    this.teams = teams;
  }

  public void addStartingTeam(StartingTeam team) {
    if (startingTeams == null) startingTeams = new ArrayList<>();
    if (!startingTeams.contains(team)) startingTeams.add(team);
  }

  /**
   * Sets the list of the starting elevens the player belongs to
   *
   * @param startingElevens the starting elevens the player belongs to
   */
  public void setStartingTeams(List<StartingTeam> startingTeams) {
    this.startingTeams = startingTeams;
  }

  /**
   * Returns a string representation of this player.
   *
   * @return a string representation of this player
   */
  @Override
  public String toString() {
    return "Jogador[ id = "
        + getId()
        + ", nome = "
        + getFirstName()
        + " "
        + getLastName()
        + ", posicao = "
        + getPosition()
        + ']';
  }
}
