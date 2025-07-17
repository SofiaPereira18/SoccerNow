package pt.ul.fc.css.soccernow.business.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class PodiumPosition {

  public enum Position {
    FIRST,
    SECOND,
    THIRD
  }

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @Enumerated(EnumType.STRING)
  private Position pos;

  @ManyToOne private Team team;

  @ManyToOne private Championship championship;

  private int points;

  public PodiumPosition() {}

  public PodiumPosition(Position pos, Team team, Championship championship, int points) {
    this.championship = championship;
    this.pos = pos;
    this.team = team;
    this.points = points;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public Position getPos() {
    return pos;
  }

  public void setPos(Position pos) {
    this.pos = pos;
  }

  public Team getTeam() {
    return team;
  }

  public void setTeam(Team team) {
    this.team = team;
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

  public int getPoints() {
    return points;
  }

  public void setPoints(int points) {
    this.points = points;
  }
}
