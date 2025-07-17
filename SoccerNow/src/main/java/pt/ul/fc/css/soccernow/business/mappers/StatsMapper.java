package pt.ul.fc.css.soccernow.business.mappers;

import pt.ul.fc.css.soccernow.business.dtos.StatsDTO;
import pt.ul.fc.css.soccernow.business.entities.Stats;
import pt.ul.fc.css.soccernow.business.repositories.StatsRepository;

public class StatsMapper {

  public static StatsDTO toDTO(Stats stats) {
    if (stats == null) return null;
    StatsDTO dto = new StatsDTO();
    dto.setId(stats.getId());
    dto.setGoalsHomeTeam(stats.getGoalsHomeTeam());
    dto.setGoalsAwayTeam(stats.getGoalsAwayTeam());
    if (stats.getCards() != null) {
      dto.setCardIds(
          stats.getCards().stream()
              .map(card -> card.getId())
              .collect(java.util.stream.Collectors.toList()));
    } else {
      dto.setCardIds(java.util.Collections.emptyList());
    }
    dto.setResult(stats.getResult());
    return dto;
  }

  public static Stats toEntity(StatsDTO dto, StatsRepository statsRepository) {
    if (dto == null) return null;
    Stats stats;
    if (dto.getId() != null) {
      stats = statsRepository.findById(dto.getId()).orElse(new Stats());
    } else {
      stats = new Stats();
    }
    return stats;
  }
}
