package pt.ul.fc.css.soccernow.business.entities;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an refereeTeam.
 *
 * @author Ana Morgado 56957
 * @author João Azevedo 56913
 * @author Sofia Pereira 56352
 */
@Entity
public class RefereeTeam {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(nullable = false)
  private boolean isChampionshipGame;

  @ManyToOne private Referee mainReferee;

  @ManyToMany private List<Referee> referees;

  @OneToMany(mappedBy = "refereeTeam")
  private List<Game> games;

  @Column(name = "is_active")
  private Boolean isActive;

  /**
   * Constructs a RefereeTeam with the specified parameters.
   *
   * @param isChampionshipGame whether the referee team is assigned to a championship game
   * @param mainReferee the main referee of the team
   * @param referees the list of assistant referees
   * @param games the games the referee team is going to be assigned
   */
  public RefereeTeam(
      boolean isChampionshipGame, Referee mainReferee, List<Referee> referees, List<Game> games) {
    this.isChampionshipGame = isChampionshipGame;
    this.mainReferee = mainReferee;
    this.referees = referees;
    this.games = games;
    this.isActive = true;
  }

  /** No-argument constructor. */
  public RefereeTeam() {
    this.isActive = true;
    this.games = new ArrayList<>();
    this.referees = new ArrayList<>();
  }

  /**
   * Adds an assistant referee to the team.
   *
   * @param referee the referee to add
   */
  public void addReferee(Referee referee) {
    if (referee != null) this.referees.add(referee);
  }

  /**
   * Removes an assistant referee from the team.
   *
   * @param referee the referee to remove
   */
  public void removeReferee(Referee referee) {
    if (referee != null) this.referees.remove(referee);
  }

  /**
   * Adds an game.
   *
   * @param referee the game to add
   */
  public void addGame(Game game) {
    if (game != null) this.games.add(game);
  }

  /**
   * Removes an game.
   *
   * @param referee the game to remove
   */
  public void removeGame(Game game) {
    if (game != null) this.games.remove(game);
  }

  // getters

  /**
   * Returns the ID of the referee team.
   *
   * @return the ID
   */
  public Long getId() {
    return id;
  }

  /**
   * Returns whether the team is assigned to a championship game.
   *
   * @return true if it's a championship game, false otherwise
   */
  public boolean isChampionshipGame() {
    return isChampionshipGame;
  }

  /**
   * Returns the main referee of the team.
   *
   * @return the main referee
   */
  public Referee getMainReferee() {
    return mainReferee;
  }

  /**
   * Returns the list of assistant referees.
   *
   * @return list of referees
   */
  public List<Referee> getReferees() {
    return referees;
  }

  /**
   * Returns the game assigned to this referee team.
   *
   * @return the game
   */
  public List<Game> getGames() {
    return games;
  }

  /**
   * Checks if referee team is active.
   *
   * @return true if referee team is active, false otherwise
   */
  public boolean getIsActive() {
    return isActive;
  }

  // setters

  /**
   * Sets the id of the user.
   *
   * @param id the id of the user
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * Sets whether the referee team is assigned to a championship game.
   *
   * @param isChampionshipGame true if it is, false otherwise
   */
  public void setIsChampionshipGame(boolean isChampionshipGame) {
    this.isChampionshipGame = isChampionshipGame;
  }

  /**
   * Sets the main referee for this team.
   *
   * @param mainReferee the main referee to assign
   */
  public void setMainReferee(Referee mainReferee) {
    this.mainReferee = mainReferee;
  }

  /**
   * Sets the list of assistant referees for this referee team.
   *
   * @param mainReferee the list of assistant referees.
   */
  public void setReferees(List<Referee> referees) {
    this.referees = referees;
  }

  /**
   * Sets the games for this referee team.
   *
   * @param games the games to assign
   */
  public void setGames(List<Game> games) {
    this.games = games;
  }

  /**
   * Sets if the referee team is active.
   *
   * @param isActive if the referee team is active
   */
  public void setIsActive(Boolean isActive) {
    this.isActive = isActive;
  }

  /**
   * Returns a string representation of this RefereeTeam.
   *
   * @return a string representation of this RefereeTeam
   */
  @Override
  public String toString() {
    return "Equipa de arbitros[ id = "
        + getId()
        + ", é jogo de campeonato? = "
        + isChampionshipGame()
        + ", árbitro principal = "
        + getMainReferee().toString()
        + ", árbitros = "
        + getReferees().toString()
        + "]";
  }
}
