package pt.ul.fc.css.soccernow.business.entities;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyJoinColumn;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Represents the game stats.
 *
 * @author Ana Morgado 56957
 * @author João Azevedo 56913
 * @author Sofia Pereira 56352
 */
@Entity
public class Stats {

  public enum GameResult {
    DRAW,
    HOME_TEAM,
    AWAY_TEAM
  }

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ElementCollection
  @CollectionTable(name = "home_players", joinColumns = @JoinColumn(name = "home_id"))
  @MapKeyJoinColumn(name = "player_id")
  @Column(name = "score")
  private Map<Long, Integer> goalsHomeTeam;

  @ElementCollection
  @CollectionTable(name = "away_players", joinColumns = @JoinColumn(name = "away_id"))
  @MapKeyJoinColumn(name = "player_id")
  @Column(name = "score")
  private Map<Long, Integer> goalsAwayTeam;

  @OneToMany private List<Card> cards;

  @Enumerated(EnumType.STRING)
  private GameResult result;

  public Stats() {
    goalsHomeTeam = new HashMap<Long, Integer>();
    goalsAwayTeam = new HashMap<Long, Integer>();
    cards = new ArrayList<Card>();
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

  public void addGoalToHomeTeam(Long playerId) {
    Integer currentGoals = goalsHomeTeam.get(playerId);
    if (currentGoals == null) {
      currentGoals = 0;
    }
    goalsHomeTeam.put(playerId, currentGoals + 1);
  }

  public void addGoalToAwayTeam(Long playerId) {
    Integer currentGoals = goalsAwayTeam.get(playerId);
    if (currentGoals == null) {
      currentGoals = 0;
    }
    goalsAwayTeam.put(playerId, currentGoals + 1);
  }

  public int getTotalGoals(Map<Long, Integer> goals) {
    if (goals != null) {
      Set<Long> playerIds = goals.keySet();
      int gls = 0;
      for (Long id : playerIds) {
        gls += goals.get(id);
      }
      return gls;
    }
    return 0;
  }

  public List<Card> getCards() {
    return cards;
  }

  public void setCards(List<Card> cards) {
    this.cards = cards;
  }

  public void addCard(Card card) {
    this.cards.add(card);
  }

  public GameResult getResult() {
    return result;
  }

  public void setResult(GameResult result) {
    this.result = result;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}
