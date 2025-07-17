package pt.ul.fc.css.soccernow.business.dtos;

import java.util.List;
import java.util.Map;
import pt.ul.fc.css.soccernow.business.entities.Stats;

/**
 * Represents the game stats.
 *
 * @author Ana Morgado 56957
 * @author João Azevedo 56913
 * @author Sofia Pereira 56352
 */
public class StatsDTO {

  private Long id;
  private Map<Long, Integer> goalsHomeTeam;
  private Map<Long, Integer> goalsAwayTeam;
  private List<Long> cardIds;
  private Stats.GameResult result;

  public StatsDTO() {}

  public StatsDTO(
      Long id,
      Map<Long, Integer> goalsHomeTeam,
      Map<Long, Integer> goalsAwayTeam,
      Long gameId,
      List<Long> cardIds,
      Stats.GameResult result) {
    this.id = id;
    this.goalsHomeTeam = goalsHomeTeam;
    this.goalsAwayTeam = goalsAwayTeam;
    this.cardIds = cardIds;
    this.result = result;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Map<Long, Integer> getGoalsHomeTeam() {
    return goalsHomeTeam;
  }

  public void setGoalsHomeTeam(Map<Long, Integer> goalsHomeTeam) {
    this.goalsHomeTeam = goalsHomeTeam;
  }

  public Map<Long, Integer> getGoalsAwayTeam() {
    return goalsAwayTeam;
  }

  public void setGoalsAwayTeam(Map<Long, Integer> goalsAwayTeam) {
    this.goalsAwayTeam = goalsAwayTeam;
  }

  public List<Long> getCardIds() {
    return cardIds;
  }

  public void setCardIds(List<Long> cardIds) {
    this.cardIds = cardIds;
  }

  public Stats.GameResult getResult() {
    return result;
  }

  public void setResult(Stats.GameResult result) {
    this.result = result;
  }
}
