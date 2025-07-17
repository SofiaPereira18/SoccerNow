package pt.ul.fc.css.soccernow.business.entities;

import jakarta.persistence.Embeddable;
import pt.ul.fc.css.soccernow.business.dtos.GameLocationDTO;

/**
 * Represents a game location.
 *
 * @author Ana Morgado 56957
 * @author João Azevedo 56913
 * @author Sofia Pereira 56352
 */
@Embeddable
public class GameLocation {

  private String address;

  private String city;

  public GameLocation(String address, String city) {
    this.address = address;
    this.city = city;
  }

  public GameLocation(GameLocationDTO dto) {
    this.address = dto.getAddress();
    this.city = dto.getCity();
  }

  public GameLocation() {}

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  @Override
  public String toString() {
    return "GameLocation{" + "address='" + address + '\'' + ", city='" + city + '\'' + '}';
  }
}
