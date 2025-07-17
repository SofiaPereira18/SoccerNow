package pt.ul.fc.css.soccernow.model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Presentation model for a Player.
 *
 * @author Ana Morgado 56957
 * @author João Azevedo 56913
 * @author Sofia Pereira 56352
 */
public class Player {

  private final LongProperty id = new SimpleLongProperty();
  private final StringProperty firstName = new SimpleStringProperty();
  private final StringProperty lastName = new SimpleStringProperty();
  private final StringProperty position = new SimpleStringProperty();
  private final ListProperty<Long> teamsIds =
      new SimpleListProperty<>(FXCollections.observableArrayList());
  private final ListProperty<Long> startingTeamsIds =
      new SimpleListProperty<>(FXCollections.observableArrayList());
  private final BooleanProperty isActive = new SimpleBooleanProperty(true); // comeca a true

  /** Default constructor. */
  public Player() {}

  /**
   * Constructor with player information.
   *
   * @param id The player's identifier
   * @param firstName The player's first name
   * @param lastName The player's last name
   * @param position The player's position (enum)
   * @param teamsIds List of team IDs the player belongs to
   * @param startingTeamsIds List of starting team IDs the player belongs to
   */
  public Player(
      Long id,
      String firstName,
      String lastName,
      String position,
      ObservableList<Long> teamsIds,
      ObservableList<Long> startingTeamsIds) {
    setId(id);
    setFirstName(firstName);
    setLastName(lastName);
    setPosition(position);
    setTeamsIds(teamsIds);
    setStartingTeamsIds(startingTeamsIds);
  }

  /**
   * Gets the player's ID.
   *
   * @return The player ID
   */
  public long getId() {
    return id.get();
  }

  /**
   * Sets the player's ID.
   *
   * @param id The ID to set
   */
  public void setId(long id) {
    this.id.set(id);
  }

  /**
   * Gets the ID property for data binding.
   *
   * @return The ID property
   */
  public LongProperty idProperty() {
    return id;
  }

  /**
   * Gets the player's first name.
   *
   * @return The first name
   */
  public String getFirstName() {
    return firstName.get();
  }

  /**
   * Sets the player's first name.
   *
   * @param firstName The first name to set
   */
  public void setFirstName(String firstName) {
    this.firstName.set(firstName);
  }

  /**
   * Gets the first name property for data binding.
   *
   * @return The first name property
   */
  public StringProperty firstNameProperty() {
    return firstName;
  }

  /**
   * Gets the player's last name.
   *
   * @return The last name
   */
  public String getLastName() {
    return lastName.get();
  }

  /**
   * Sets the player's last name.
   *
   * @param lastName The last name to set
   */
  public void setLastName(String lastName) {
    this.lastName.set(lastName);
  }

  /**
   * Gets the last name property for data binding.
   *
   * @return The last name property
   */
  public StringProperty lastNameProperty() {
    return lastName;
  }

  /**
   * Gets the player's position.
   *
   * @return The player position
   */
  public String getPosition() {
    return position.get();
  }

  /**
   * Sets the player's position.
   *
   * @param position The position to set
   */
  public void setPosition(String position) {
    this.position.set(position);
  }

  /**
   * Gets the position property for data binding.
   *
   * @return The position property
   */
  public StringProperty positionProperty() {
    return position;
  }

  /**
   * Gets the list of team IDs the player belongs to.
   *
   * @return Observable list of team IDs
   */
  public ObservableList<Long> getTeamsIds() {
    return teamsIds.get();
  }

  /**
   * Sets the list of team IDs the player belongs to.
   *
   * @param teamsIds The list of team IDs to set
   */
  public void setTeamsIds(ObservableList<Long> teamsIds) {
    this.teamsIds.set(teamsIds);
  }

  /**
   * Gets the team IDs property for data binding.
   *
   * @return The team IDs property
   */
  public ListProperty<Long> teamsIdsProperty() {
    return teamsIds;
  }

  /**
   * Gets the list of starting team IDs the player belongs to.
   *
   * @return Observable list of starting team IDs
   */
  public ObservableList<Long> getStartingTeamsIds() {
    return startingTeamsIds.get();
  }

  /**
   * Sets the list of starting team IDs the player belongs to.
   *
   * @param startingTeamsIds The list of starting team IDs to set
   */
  public void setStartingTeamsIds(ObservableList<Long> startingTeamsIds) {
    this.startingTeamsIds.set(startingTeamsIds);
  }

  /**
   * Gets the starting team IDs property for data binding.
   *
   * @return The starting team IDs property
   */
  public ListProperty<Long> startingTeamsIdsProperty() {
    return startingTeamsIds;
  }

  /**
   * Gets whether the player is active.
   *
   * @return true if active, false otherwise
   */
  public boolean isIsActive() {
    return isActive.get();
  }

  /**
   * Sets whether the player is active.
   *
   * @param isActive The active status to set
   */
  public void setIsActive(boolean isActive) {
    this.isActive.set(isActive);
  }

  /**
   * Gets the isActive property for data binding.
   *
   * @return The isActive property
   */
  public BooleanProperty isActiveProperty() {
    return isActive;
  }

  /**
   * Adds a team ID to the player's team.
   *
   * @param teamId The team ID to add (ignored if null)
   */
  public void addTeamId(Long teamId) {
    if (teamId != null) {
      this.teamsIds.add(teamId);
    }
  }

  /**
   * Removes a team ID from the player's team.
   *
   * @param teamId The team ID to remove
   */
  public void removeTeamId(Long teamId) {
    this.teamsIds.remove(teamId);
  }

  /**
   * Adds a starting team ID to the player's starting team.
   *
   * @param teamId The starting team ID to add
   */
  public void addStartingTeamId(Long teamId) {
    if (teamId != null) {
      this.startingTeamsIds.add(teamId);
    }
  }

  /**
   * Removes a starting team ID from the player's starting team.
   *
   * @param teamId The starting team ID to remove
   */
  public void removeStartingTeamId(Long teamId) {
    this.startingTeamsIds.remove(teamId);
  }
}
