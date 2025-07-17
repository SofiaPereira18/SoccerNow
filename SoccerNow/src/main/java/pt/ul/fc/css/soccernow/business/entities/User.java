package pt.ul.fc.css.soccernow.business.entities;

import jakarta.persistence.*;

/**
 * Represents an user.
 *
 * @author Ana Morgado 56957
 * @author João Azevedo 5691
 * @author Sofia Pereira 56352
 */
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String firstName;

  private String lastName;

  @Column(name = "is_active")
  private Boolean isActive;

  /**
   * Constructs a new user.
   *
   * @param firstName the first name of the user
   * @param lastName the last name of the user
   */
  public User(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.isActive = true;
  }

  /** No-argument constructor. */
  public User() {
    this.isActive = true;
  }

  // getters

  /**
   * Gets the id of the user.
   *
   * @return the id of the user
   */
  public Long getId() {
    return id;
  }

  /**
   * Returns the first name of the user.
   *
   * @return the user's first name
   */
  public String getFirstName() {
    return firstName;
  }

  /**
   * Returns the last name of the user.
   *
   * @return the user's last name
   */
  public String getLastName() {
    return lastName;
  }

  /**
   * Checks if user is active.
   *
   * @return true if user is active, false otherwise
   */
  public Boolean getIsActive() {
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
   * Sets the first name of the user.
   *
   * @param firstName the first name of the user
   */
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * Sets the last name of the user.
   *
   * @param lastName the last name of the user
   */
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /**
   * Sets if the user is active.
   *
   * @param isActive if the user is active
   */
  public void setIsActive(Boolean isActive) {
    this.isActive = isActive;
  }

  /**
   * Returns a string representation of this user.
   *
   * @return a string representation of this user
   */
  @Override
  public String toString() {
    return "Utilizador[ id = "
        + getId()
        + ", primeiro nome = "
        + getFirstName()
        + ", ultimo nome = "
        + getLastName()
        + "esta ativo = "
        + (getIsActive() ? "sim" : "nao")
        + ']';
  }
}
