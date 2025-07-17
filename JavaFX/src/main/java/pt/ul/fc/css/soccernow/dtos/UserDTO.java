package pt.ul.fc.css.soccernow.dtos;

/**
 * Represents an UserDTO.
 *
 * @author Ana Morgado 56957
 * @author João Azevedo 5691
 * @author Sofia Pereira 56352
 */
public class UserDTO {

  private Long id;
  private String firstName;
  private String lastName;

  /** No-argument constructor. */
  public UserDTO() {
  }

  /**
   * Constructs a UserDTO with the specified parameters.
   *
   * @param id        the id of the user
   * @param firstName the first name of the user
   * @param lastName  the last name of the user
   */
  public UserDTO(Long id, String firstName, String lastName) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
  }

  // getters

  /**
   * Returns the ID.
   *
   * @return the user's ID
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
}
