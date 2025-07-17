package pt.ul.fc.css.soccernow.dtos;

public class PodiumPositionDTO {

  public enum Position {
    FIRST,
    SECOND,
    THIRD
  }

  private long id;
  private Position pos;
  private long teamId;
  private long championshipId;
  private int points;

  public PodiumPositionDTO() {}

  public PodiumPositionDTO(long id, Position pos, long teamId, long championshipId, int points) {
    this.id = id;
    this.pos = pos;
    this.teamId = teamId;
    this.championshipId = championshipId;
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

  public long getTeamId() {
    return teamId;
  }

  public void setTeamId(long teamId) {
    this.teamId = teamId;
  }

  public long getChampionshipId() {
    return championshipId;
  }

  public void setChampionshipId(long championshipId) {
    this.championshipId = championshipId;
  }

  public int getPoints() {
    return points;
  }

  public void setPoints(int points) {
    this.points = points;
  }
}
