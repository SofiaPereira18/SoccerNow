package pt.ul.fc.css.soccernow.business.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a game.
 *
 * @author Ana Morgado 56957
 * @author João Azevedo 56913
 * @author Sofia Pereira 56352
 */
@Entity
public class Game {

  public enum GameType {
    FRIENDLY,
    CHAMPIONSHIP
  }

  public enum GameShift {
    MORNING,
    AFTERNOON,
    NIGHT
  }

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "local_date_time", columnDefinition = "TIMESTAMP")
  private LocalDateTime dateTime;

  @Embedded private GameLocation location;

  @Enumerated(EnumType.STRING)
  private GameType gameType;

  @Enumerated(EnumType.STRING)
  private GameShift gameShift;

  @ManyToMany
  @JoinTable(
      name = "game_starting_team",
      joinColumns = @JoinColumn(name = "game_id"),
      inverseJoinColumns = @JoinColumn(name = "starting_team_id"))
  private List<StartingTeam> startingTeams;

  @ManyToOne private RefereeTeam refereeTeam;

  @OneToOne private Stats stats;

  @ManyToOne private Championship championship;

  private boolean isActive;

  private boolean isFinished;

  public Game() {
    this.isActive = true;
    this.isFinished = false;
  }

  public Game(
      LocalDateTime dateTime,
      GameLocation location,
      List<StartingTeam> teams,
      RefereeTeam refereeTeam,
      Championship championship) {
    setDateTime(dateTime);
    this.location = location;
    this.startingTeams = teams;
    this.refereeTeam = refereeTeam;
    this.stats = new Stats();
    this.championship = championship;
    this.isActive = true;
    this.isFinished = false;
  }

  public Championship getChampionship() {
    return championship;
  }

  public void setChampionship(Championship championship) {
    this.championship = championship;
  }

  public void removeChampionship() {
    this.championship = null;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public GameLocation getLocation() {
    return location;
  }

  public void setLocation(GameLocation location) {
    this.location = location;
  }

  public GameType getGameType() {
    return gameType;
  }

  public void setGameType(GameType gameType) {
    this.gameType = gameType;
  }

  public LocalDateTime getDateTime() {
    return dateTime;
  }

  public void setDateTime(LocalDateTime dateTime) {
    this.dateTime = dateTime;
    if (dateTime != null) {
      int hour = dateTime.getHour();
      if (hour >= 6 && hour < 12) {
        setGameShift(GameShift.MORNING);
      } else if (hour < 20) {
        setGameShift(GameShift.AFTERNOON);
      } else {
        setGameShift(GameShift.NIGHT);
      }
    }
  }

  public List<StartingTeam> getStartingTeams() {
    return startingTeams;
  }

  public void setStartingTeams(List<StartingTeam> teams) {
    this.startingTeams = teams;
  }

  public RefereeTeam getRefereeTeam() {
    return refereeTeam;
  }

  public void setRefereeTeam(RefereeTeam refereeTeam) {
    this.refereeTeam = refereeTeam;
  }

  public Stats getStats() {
    return stats == null ? new Stats() : stats;
  }

  public void setStats(Stats stats) {
    this.stats = stats;
  }

  public boolean isActive() {
    return isActive;
  }

  public void setActive(boolean isActive) {
    this.isActive = isActive;
  }

  public boolean isFinished() {
    return isFinished;
  }

  public void setFinished(boolean isFinished) {
    this.isFinished = isFinished;
  }

  public GameShift getGameShift() {
    return gameShift;
  }

  public void setGameShift(GameShift gameShift) {
    this.gameShift = gameShift;
  }
}
