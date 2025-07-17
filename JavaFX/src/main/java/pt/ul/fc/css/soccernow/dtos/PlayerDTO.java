package pt.ul.fc.css.soccernow.dtos;

import java.util.List;

/**
 * Represents an PlayerDTO.
 *
 * @author Ana Morgado 56957
 * @author João Azevedo 5691
 * @author Sofia Pereira 56352
 */
public class PlayerDTO extends UserDTO {

  private String position;
  private List<Long> teamsIds;
  private List<Long> startingTeamsIds;

  private boolean isActive;

  /** No-argument constructor. */
  public PlayerDTO() {
    this.isActive = true;
  }

  /**
   * Constructs a RefereeTeamDTO with the specified parameters.
   *
   * @param id the id of the player
   * @param firstName the first name of the player
   * @param lastName the last name of the player
   * @param positions the positions the player can play
   * @param teamsIds the teamsIds the user plays in
   */
  public PlayerDTO(
      Long id,
      String firstName,
      String lastName,
      String position,
      List<Long> teamsIds,
      List<Long> startingTeamsIds,
      List<Long> cardsIds) {
    super(id, firstName, lastName);
    this.position = position;
    this.teamsIds = teamsIds;
    this.startingTeamsIds = startingTeamsIds;
    this.isActive = true;
  }

  // getters

  /**
   * Returns the list of positions the player can play.
   *
   * @return the list of positions
   */
  public String getPosition() {
    return position;
  }

  /**
   * Returns the list of teamsIds the player plays in.
   *
   * @return the list of teamsIds the player plays in
   */
  public List<Long> getTeams() {
    return teamsIds;
  }

  /**
   * Returns the list of starting teamsIds the player is part of.
   *
   * @return the list of starting teamsIds
   */
  public List<Long> getStartingTeams() {
    return startingTeamsIds;
  }

  /**
   * Checks if player is active.
   *
   * @return true if player is active, false otherwise
   */
  public boolean getIsActive() {
    return isActive;
  }

  // setters

  /**
   * Sets the list of positions the player can play.
   *
   * @param positions the list of positions
   */
  public void setPosition(String position) {
    this.position = position;
  }

  /**
   * Sets the list of teamsIds the player plays in.
   *
   * @param teamsIds the list of teamsIds the player plays in
   */
  public void setTeams(List<Long> teamsIds) {
    this.teamsIds = teamsIds;
  }

  /**
   * Sets the list of starting teamsIds the player is part of.
   *
   * @param startingTeams the list of starting teamsIds
   */
  public void setStartingTeams(List<Long> startingTeamsIds) {
    this.startingTeamsIds = startingTeamsIds;
  }

  /**
   * Sets if the player is active.
   *
   * @param isActive if the player is active
   */
  public void setIsActive(boolean isActive) {
    this.isActive = isActive;
  }
}
