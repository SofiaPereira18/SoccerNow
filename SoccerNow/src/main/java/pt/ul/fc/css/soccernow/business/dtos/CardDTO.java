package pt.ul.fc.css.soccernow.business.dtos;

import pt.ul.fc.css.soccernow.business.entities.Card;

/**
 * Represents a CardDTO.
 *
 * @author Ana Morgado 56957
 * @author João Azevedo 56913
 * @author Sofia Pereira 56352
 */
public class CardDTO {

  public enum CardType {
    RED,
    YELLOW
  }

  private Long id;
  private Long playerId;
  private Card.CardType type;

  public CardDTO(Long id, Long playerId, Card.CardType type) {
    this.id = id;
    this.playerId = playerId;
    this.type = type;
  }

  public CardDTO(Card card) {
    this.id = card.getId();
    this.playerId = card.getPlayer().getId();
    this.type = card.getType();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getPlayerId() {
    return playerId;
  }

  public void setPlayerId(Long playerId) {
    this.playerId = playerId;
  }

  public Card.CardType getType() {
    return type;
  }

  public void setType(Card.CardType type) {
    this.type = type;
  }
}
