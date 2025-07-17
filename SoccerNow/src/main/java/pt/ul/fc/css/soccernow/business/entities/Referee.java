package pt.ul.fc.css.soccernow.business.entities;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an referee.
 *
 * @author Ana Morgado 56957
 * @author João Azevedo 56913
 * @author Sofia Pereira 56352
 */
@Entity
public class Referee extends User {

  private Boolean hasCertificate;

  @ManyToMany(mappedBy = "referees")
  private List<RefereeTeam> refereeTeams;

  /**
   * Constructs a referee with the given name and certification status.
   *
   * @param firstName the first name of the referee
   * @param lastName the last name of the referee
   * @param hasCertificate if has certificate or not
   * @param refereeTeams the teams the referee referees
   */
  public Referee(
      String firstName, String lastName, Boolean hasCertificate, List<RefereeTeam> refereeTeams) {
    super(firstName, lastName);
    this.hasCertificate = hasCertificate;
    this.refereeTeams = refereeTeams;
  }

  /** No-argument constructor. */
  public Referee() {
    this.refereeTeams = new ArrayList<>();
  }

  /**
   * Adds a referee team to the referee's list of teams.
   *
   * @param refereeTeam the referee team to add
   */
  public void addRefereeTeam(RefereeTeam refereeTeam) {
    if (refereeTeam != null) this.refereeTeams.add(refereeTeam);
  }

  /**
   * Removes a referee team to the referee's list of teams.
   *
   * @param refereeTeam the referee team to remove
   */
  public void removeRefereeTeam(RefereeTeam refereeTeam) {
    if (refereeTeam != null) this.refereeTeams.remove(refereeTeam);
  }

  // getters

  /**
   * Returns whether the referee is certified.
   *
   * @return true if the referee has a certificate, false otherwise
   */
  public boolean hasCertificate() {
    return hasCertificate;
  }

  /**
   * Gets the list of referee teams the referee is assigned to.
   *
   * @return the list of referee teams
   */
  public List<RefereeTeam> getRefereeTeams() {
    return refereeTeams;
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
   * Sets the list of referee teams the referee is part of.
   *
   * @param refereeTeams the list of referee teams
   */
  public void setRefereeTeams(List<RefereeTeam> refereeTeams) {
    this.refereeTeams = refereeTeams;
  }

  /**
   * Returns a string representation of this referee.
   *
   * @return a string representation of this referee
   */
  @Override
  public String toString() {
    return "Arbitro[ id = "
        + getId()
        + ", nome = "
        + getFirstName()
        + " "
        + getLastName()
        + ", tem certificado? = "
        + hasCertificate()
        + ", pertence às equipas = "
        + getRefereeTeams().toString()
        + ']';
  }
}
