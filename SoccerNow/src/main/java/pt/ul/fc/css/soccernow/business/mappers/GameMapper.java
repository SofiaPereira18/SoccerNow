package pt.ul.fc.css.soccernow.business.mappers;

import pt.ul.fc.css.soccernow.business.dtos.GameDTO;
import pt.ul.fc.css.soccernow.business.entities.Game;
import pt.ul.fc.css.soccernow.business.handlers.GameHandler;

public class GameMapper {

  private static GameHandler gHandler;

  public GameMapper(GameHandler gHandler) {
    GameMapper.gHandler = gHandler;
  }

  public static GameDTO toDTO(Game game) {
    if (game == null) {
      return null;
    }
    return new GameDTO(
        game.getId(),
        game.getDateTime(),
        game.getLocation().getCity()!=null? game.getLocation().getCity() : "-",
        game.getLocation().getAddress()!=null? game.getLocation().getAddress() : "-",
        game.getGameType(),
        game.getStartingTeams().stream().map(team -> team.getId()).toList(),
        game.getRefereeTeam() != null ? game.getRefereeTeam().getId() : null,
        game.getStats() != null ? game.getStats().getId() : null,
        game.getChampionship() != null ? game.getChampionship().getId() : null,
        game.isFinished(),
        game.getGameShift());
  }

  public static Game toEntity(GameDTO gameDTO) {
    if (gameDTO == null) {
      return null;
    }
    return gHandler.getGameById(gameDTO.getId());
  }
}
