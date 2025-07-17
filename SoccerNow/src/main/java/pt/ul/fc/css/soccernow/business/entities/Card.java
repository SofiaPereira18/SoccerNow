package pt.ul.fc.css.soccernow.business.entities;

import jakarta.persistence.*;

/**
 * Represents a card.
 *
 * @author Ana Morgado 56957
 * @author João Azevedo 56913
 * @author Sofia Pereira 56352
 */
@Entity
public class Card {

  public enum CardType {
    RED,
    YELLOW
  }

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ManyToOne private Player player;

  @Enumerated(EnumType.STRING)
  private CardType type;

  public Card() {}

  public Card(Player player, CardType type) {
    this.player = player;
    this.type = type;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }

  public Player getPlayer() {
    return player;
  }

  public void setPlayer(Player player) {
    this.player = player;
  }

  public CardType getType() {
    return type;
  }

  public void setType(CardType type) {
    this.type = type;
  }
}
