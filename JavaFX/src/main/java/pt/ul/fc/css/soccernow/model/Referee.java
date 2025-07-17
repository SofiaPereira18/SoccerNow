package pt.ul.fc.css.soccernow.model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Presentation model for a referee.
 *
 * @author Ana Morgado 56957
 * @author João Azevedo 56913
 * @author Sofia Pereira 56352
 */
public class Referee {

  private final LongProperty id = new SimpleLongProperty();
  private final StringProperty firstName = new SimpleStringProperty();
  private final StringProperty lastName = new SimpleStringProperty();
  private final BooleanProperty hasCertificate = new SimpleBooleanProperty();
  private final ListProperty<Long> refereeTeamsIds =
      new SimpleListProperty<>(FXCollections.observableArrayList());
  private final BooleanProperty isActive = new SimpleBooleanProperty(true); // comeca a true

  /** Default constructor. */
  public Referee() {}

  /**
   * Constructor with referee information.
   *
   * @param id The referee's identifier
   * @param firstName The referee's first name
   * @param lastName The referee's last name
   * @param hasCertificate Whether the referee has certification
   */
  public Referee(Long id, String firstName, String lastName, boolean hasCertificate) {
    this(id, firstName, lastName, hasCertificate, FXCollections.observableArrayList());
  }

  /**
   * Full constructor with all referee information.
   *
   * @param id The referee's identifier
   * @param firstName The referee's first name
   * @param lastName The referee's last name
   * @param hasCertificate Whether the referee has certification
   * @param refereeTeamsIds List of team IDs the referee is assigned to
   */
  public Referee(
      Long id,
      String firstName,
      String lastName,
      boolean hasCertificate,
      ObservableList<Long> refereeTeamsIds) {
    setId(id);
    setFirstName(firstName);
    setLastName(lastName);
    setHasCertificate(hasCertificate);
    setRefereeTeamsIds(refereeTeamsIds);
  }

  /**
   * Gets the referee's ID.
   *
   * @return The referee ID
   */
  public long getId() {
    return id.get();
  }

  /**
   * Sets the referee's ID.
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
   * Gets the referee's first name.
   *
   * @return The first name
   */
  public String getFirstName() {
    return firstName.get();
  }

  /**
   * Sets the referee's first name.
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
   * Gets the referee's last name.
   *
   * @return The last name
   */
  public String getLastName() {
    return lastName.get();
  }

  /**
   * Sets the referee's last name.
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
   * Gets the certification status property for data binding.
   *
   * @return The certification property
   */
  public BooleanProperty hasCertificateProperty() {
    return hasCertificate;
  }

  /**
   * Checks if the referee has certification.
   *
   * @return true if certified, false otherwise
   */
  public boolean hasCertificate() {
    return hasCertificate.get();
  }

  /**
   * Sets the referee's certification status.
   *
   * @param hasCertificate The certification status to set
   */
  public void setHasCertificate(boolean hasCertificate) {
    this.hasCertificate.set(hasCertificate);
  }

  /**
   * Gets the list of team IDs the referee is assigned to.
   *
   * @return Observable list of team IDs
   */
  public ObservableList<Long> getRefereeTeamsIds() {
    return refereeTeamsIds.get();
  }

  /**
   * Sets the list of team IDs the referee is assigned to.
   *
   * @param refereeTeamsIds The list of team IDs to set
   */
  public void setRefereeTeamsIds(ObservableList<Long> refereeTeamsIds) {
    this.refereeTeamsIds.set(refereeTeamsIds);
  }

  /**
   * Gets the team IDs property for data binding.
   *
   * @return The team IDs property
   */
  public ListProperty<Long> refereeTeamsIdsProperty() {
    return refereeTeamsIds;
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
   * Adds a team ID to the referee's list.
   *
   * @param teamId The team ID to add
   */
  public void addRefereeTeamId(Long teamId) {
    if (teamId != null) {
      this.refereeTeamsIds.add(teamId);
    }
  }

  /**
   * Removes a team ID from the referee's list.
   *
   * @param teamId The team ID to remove
   */
  public void removeRefereeTeamId(Long teamId) {
    this.refereeTeamsIds.remove(teamId);
  }

  /**
   * Returns a string representation of the referee.
   *
   * @return String containing name and certification status
   */
  @Override
  public String toString() {
    return "Referee: "
        + getFirstName()
        + " "
        + getLastName()
        + (hasCertificate() ? " (Certified)" : " (Not certified)");
  }
}
