package pt.ul.fc.css.soccernow.business.dtos;

import java.util.List;

/**
 * Represents an RefereeTeamDTO.
 *
 * @author Ana Morgado 56957
 * @author João Azevedo 5691
 * @author Sofia Pereira 56352
 */
public class RefereeTeamDTO {

  private Long id;
  private boolean isChampionshipGame;
  private Long mainRefereeId;
  private List<Long> refereesIds;
  private List<Long> gamesIds;
  private boolean isActive;

  /** No-argument constructor. */
  public RefereeTeamDTO() {
    this.isActive = true;
  }

  /**
   * Constructs a RefereeTeamDTO with the specified parameters.
   *
   * @param id the id of the referee team
   * @param isChampionshipGame whether the referee team is assigned to a championship game
   * @param mainRefereeId the main referee of the team
   * @param refereesIds the list of assistant refereesIds
   * @param gamesIds the gamesIds the referee team is going to be assigned
   */
  public RefereeTeamDTO(
      Long id,
      boolean isChampionshipGame,
      Long mainRefereeId,
      List<Long> refereesIds,
      List<Long> gamesIds) {
    this.id = id;
    this.isChampionshipGame = isChampionshipGame;
    this.mainRefereeId = mainRefereeId;
    this.refereesIds = refereesIds;
    this.gamesIds = gamesIds;
    this.isActive = true;
  }

  /**
   * Adds an game.
   *
   * @param id the id game's to add
   */
  public void addGame(Long id) {
    this.gamesIds.add(id);
  }

  /**
   * Removes an game.
   *
   * @param id the id game's to remove
   */
  public void removeGame(Long id) {
    this.gamesIds.remove(id);
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
  public Long getMainReferee() {
    return mainRefereeId;
  }

  /**
   * Returns the list of assistant refereesIds.
   *
   * @return list of refereesIds
   */
  public List<Long> getReferees() {
    return refereesIds;
  }

  /**
   * Returns the gamess associated with this referee team.
   *
   * @return the gamess DTO
   */
  public List<Long> getGames() {
    return gamesIds;
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
   * Sets the id of the referee team.
   *
   * @param id the id of the referee team
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
   * @param mainRefereeId the main referee to assign
   */
  public void setMainReferee(Long mainRefereeId) {
    this.mainRefereeId = mainRefereeId;
  }

  /**
   * Sets if the referee team is active.
   *
   * @param isActive if the referee team is active
   */
  public void setIsActive(boolean isActive) {
    this.isActive = isActive;
  }

  /**
   * Sets the list of assistant refereesIds for this team.
   *
   * @param mainRefereeId the list of assistant refereesIds.
   */
  public void setReferees(List<Long> refereesIds) {
    this.refereesIds = refereesIds;
  }

  /**
   * Sets the games associated with this referee team.
   *
   * @param gamesIds the games DTO to associate
   */
  public void setGames(List<Long> gamesIds) {
    this.gamesIds = gamesIds;
  }
}
