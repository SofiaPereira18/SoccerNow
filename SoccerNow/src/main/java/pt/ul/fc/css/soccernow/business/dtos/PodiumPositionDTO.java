package pt.ul.fc.css.soccernow.business.dtos;

public class PodiumPositionDTO {

  private long id;
  private String pos;
  private long teamId;
  private long championshipId;
  private int points;

  public PodiumPositionDTO() {}

  public PodiumPositionDTO(long id, String pos, long teamId, long championshipId, int points) {
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

  public String getPos() {
    return pos;
  }

  public void setPos(String pos) {
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
