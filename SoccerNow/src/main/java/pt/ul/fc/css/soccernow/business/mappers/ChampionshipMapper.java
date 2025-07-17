package pt.ul.fc.css.soccernow.business.mappers;

import pt.ul.fc.css.soccernow.business.dtos.ChampionshipDTO;
import pt.ul.fc.css.soccernow.business.entities.Championship;
import pt.ul.fc.css.soccernow.business.handlers.ChampionshipHandler;

public class ChampionshipMapper {

  private static ChampionshipHandler championshipHandler;

  public static ChampionshipDTO toDTO(Championship championship) {
    ChampionshipDTO championshipDTO =
        new ChampionshipDTO(
            championship.getId(),
            championship.getName(),
            championship.getPodiumPositions().stream().map(p -> p.getId()).toList(),
            championship.getGames().stream().map(g -> g.getId()).toList(),
            championship.getTeams().stream().map(t -> t.getId()).toList(),
            championship.isFinished());
    return championshipDTO;
  }

  public static Championship toEntity(ChampionshipDTO championshipDTO) {
    return championshipHandler.getChampionshipById(championshipDTO.getId());
  }
}
