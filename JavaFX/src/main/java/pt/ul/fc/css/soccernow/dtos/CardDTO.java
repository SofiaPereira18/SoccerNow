package pt.ul.fc.css.soccernow.dtos;

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
  private CardType type;

  public CardDTO(Long id, Long playerId, CardType type) {
    this.id = id;
    this.playerId = playerId;
    this.type = type;
  }

  // public CardDTO(Card card) {
  // this.id = card.getId();
  // this.playerId = card.getPlayer().getId();
  // this.type = card.getType();
  // }

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

  public CardType getType() {
    return type;
  }

  public void setType(CardType type) {
    this.type = type;
  }
}
