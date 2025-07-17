package pt.ul.fc.css.soccernow.business.dtos;

import java.time.LocalDateTime;
import java.util.List;

public class GameRequestDTO {

  private LocalDateTime dateTime;

  private String city;
  private String stadium;

  private Long refereeTeamId;
  private List<Long> teamsIds;

  public GameRequestDTO() {}

  public GameRequestDTO(
      LocalDateTime dateTime,
      String city,
      String stadium,
      Long refereeTeamId,
      List<Long> teamsIds) {

    this.dateTime = dateTime;
    this.city = city;
    this.stadium = stadium;
    this.refereeTeamId = refereeTeamId;
    this.teamsIds = teamsIds;
  }

  public LocalDateTime getDateTime() {
    return dateTime;
  }

  public void setDateTime(LocalDateTime dateTime) {
    this.dateTime = dateTime;
  }

  public String getLocation() {
    return city + ", " + stadium;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getCity() {
    return city;
  }

  public String getStadium() {
    return stadium;
  }

  public void setStadium(String stadium) {
    this.stadium = stadium;
  }

  public Long getRefereeTeamId() {
    return refereeTeamId;
  }

  public void setRefereeTeamId(Long refereeTeamId) {
    this.refereeTeamId = refereeTeamId;
  }

  public List<Long> getTeamsIds() {
    return teamsIds;
  }

  public void setTeamsIds(List<Long> teamsIds) {
    this.teamsIds = teamsIds;
  }
}
