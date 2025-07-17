package pt.ul.fc.css.soccernow.business.dtos;

import java.time.LocalDateTime;
import java.util.List;
import pt.ul.fc.css.soccernow.business.entities.Game;
import pt.ul.fc.css.soccernow.business.entities.GameLocation;

/**
 * Represents the game stats.
 *
 * @author Ana Morgado 56957
 * @author João Azevedo 56913
 * @author Sofia Pereira 56352
 */
public class GameDTO {

  private Long id;
  private LocalDateTime dateTime;
  private String city;
  private String stadium;
  private Game.GameType gameType;
  private Long refereeTeamId;
  private Long statsId;
  private List<Long> teamsIds;
  private Long championshipId;
  private boolean isFinished;
  private Game.GameShift gameShift;

  public GameDTO() {}

  public GameDTO(
      Long id,
      LocalDateTime dateTime,
      String city,
      String stadium,
      Game.GameType gameType,
      List<Long> teamsIds,
      Long refereeTeamId,
      Long statsId,
      Long championshipId,
      boolean isFinished,
      Game.GameShift gameShift) {
    this.championshipId = championshipId;
    this.id = id;
    this.dateTime = dateTime;
    this.city = city;
    this.stadium = stadium;
    this.gameType = gameType;
    this.refereeTeamId = refereeTeamId;
    this.statsId = statsId;
    this.teamsIds = teamsIds;
    this.isFinished = isFinished;
    this.gameShift = gameShift;
  }

  public Game.GameShift getGameShift() {
    return gameShift;
  }

  public void setGameShift(Game.GameShift gameShift) {
    this.gameShift = gameShift;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public LocalDateTime getDateTime() {
    return dateTime;
  }

  public void setDateTime(LocalDateTime dateTime) {
    this.dateTime = dateTime;
  }

  public Game.GameType getGameType() {
    return gameType;
  }

  public void setGameType(Game.GameType gameType) {
    this.gameType = gameType;
  }

  public Long getRefereeTeamId() {
    return refereeTeamId;
  }

  public void setRefereeTeamId(Long refereeTeamId) {
    this.refereeTeamId = refereeTeamId;
  }

  public Long getStatsId() {
    return statsId;
  }

  public void setStatsId(Long statsId) {
    this.statsId = statsId;
  }

  public List<Long> getTeamsIds() {
    return teamsIds;
  }

  public void setTeamsIds(List<Long> teamsIds) {
    this.teamsIds = teamsIds;
  }

  public Long getChampionshipId() {
    return championshipId;
  }

  public void setChampionshipId(Long championshipId) {
    this.championshipId = championshipId;
  }

  public boolean isFinished() {
    return isFinished;
  }

  public void setFinished(boolean isFinished) {
    this.isFinished = isFinished;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getStadium() {
    return stadium;
  }

  public void setStadium(String stadium) {
    this.stadium = stadium;
  }

  @Override
  public String toString() {
    return "GameDTO [id="
        + id
        + ", dateTime="
        + dateTime
        + ", location="
        + city + " " + stadium
        + ", gameType="
        + gameType
        + ", refereeTeamId="
        + refereeTeamId
        + ", statsId="
        + statsId
        + ", startingteamsIds="
        + teamsIds
        + "]";
  }
}
