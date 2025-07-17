package pt.ul.fc.css.soccernow.business.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.List;

@Entity
public class ChampionshipByQualifiers extends Championship {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  /** Default constructor for the ChampionshipByQualifiers class. */
  public ChampionshipByQualifiers() {}

  /**
   * Constructs a new championship by qualifiers.
   *
   * @param name the name of the championship
   * @param pointsPerWin the number of points awarded for a win
   * @param pointsPerDraw the number of points awarded for a draw
   * @param pointsPerLoss the number of points awarded for a loss
   */
  public ChampionshipByQualifiers(
      String name,
      List<PodiumPosition> podiumPositions,
      List<Game> championshipGames,
      List<Team> teams) {
    super(name, teams);
  }
}
