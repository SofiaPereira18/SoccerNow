package pt.ul.fc.css.soccernow.dtos;

import java.util.List;

/**
 * Represents an RefereeDTO.
 *
 * @author Ana Morgado 56957
 * @author João Azevedo 5691
 * @author Sofia Pereira 56352
 */
public class RefereeDTO extends UserDTO {

  private boolean hasCertificate;
  private List<Long> refereeTeamsIds;
  private boolean isActive;

  /** No-argument constructor. */
  public RefereeDTO() {
    this.isActive = true;
  }

  /**
   * Constructs a RefereeDTO with the given name and certification status.
   *
   * @param id the id of the referee
   * @param firstName the first name of the referee
   * @param lastName the last name of the referee
   * @param hasCertificate if has certificate or not
   * @param refereeTeamsIds the teams the referee referees
   */
  public RefereeDTO(
      Long id,
      String firstName,
      String lastName,
      boolean hasCertificate,
      List<Long> refereeTeamsIds) {
    super(id, firstName, lastName);
    this.hasCertificate = hasCertificate;
    this.refereeTeamsIds = refereeTeamsIds;
    this.isActive = true;
  }

  // getters

  /**
   * Returns whether the referee is certified.
   *
   * @return true if the referee has a certificate, false otherwise
   */
  public boolean isHasCertificate() {
    return hasCertificate;
  }

  /**
   * Gets the list of referee refereeTeamsIds the referee is assigned to.
   *
   * @return the list of referee refereeTeamsIds
   */
  public List<Long> getRefereeTeams() {
    return refereeTeamsIds;
  }

  /**
   * Checks if referee is active.
   *
   * @return true if referee is active, false otherwise
   */
  public boolean getIsActive() {
    return isActive;
  }

  // setters

  /**
   * Sets the certification status of the referee.
   *
   * @param hasCertificate true if the referee is certified, false otherwise
   */
  public void setHasCertificate(boolean hasCertificate) {
    this.hasCertificate = hasCertificate;
  }

  /**
   * Sets the list of referee refereeTeamsIds the referee is part of.
   *
   * @param refereerefereeTeams the list of referee refereeTeamsIds
   */
  public void setRefereeTeams(List<Long> refereeTeamsIds) {
    this.refereeTeamsIds = refereeTeamsIds;
  }

  /**
   * Sets if the referee is active.
   *
   * @param isActive if the referee is active
   */
  public void setIsActive(boolean isActive) {
    this.isActive = isActive;
  }
}
