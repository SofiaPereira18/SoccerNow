package pt.ul.fc.css.soccernow.business.services;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.ul.fc.css.soccernow.business.entities.Game;
import pt.ul.fc.css.soccernow.business.entities.Game.GameShift;
import pt.ul.fc.css.soccernow.business.entities.Stats.GameResult;
import pt.ul.fc.css.soccernow.business.handlers.GameHandler;

@Service
public class GameService {

  @Autowired private GameHandler gameHandler;

  public List<Game> filterGames(
      String status, Integer goals, String city, String address, String shift) {
    List<Game> filterGames = gameHandler.getAllGames();

    if (status != null && !status.isEmpty()) {
      List<Game> statusFilter = new ArrayList<>();
      if (status.equalsIgnoreCase("finished")) {
        statusFilter = gameHandler.getAllFinishedGames();
      } else if (status.equalsIgnoreCase("unfinished")) {
        statusFilter = gameHandler.getAllUnfinishedGames();
      }
      filterGames.retainAll(statusFilter);
    }

    if (goals != null) {
      List<Game> goalsFilter = gameHandler.getGameByGoals(goals);
      filterGames.retainAll(goalsFilter);
    }

    if (city != null && !city.isEmpty()) {
      filterGames.removeIf(game -> !game.getLocation().getCity().equals(city));
    }

    if (address != null && !address.isEmpty()) {
      filterGames.removeIf(game -> !game.getLocation().getAddress().equals(address));
    }

    if (shift != null && !shift.isEmpty()) {
      List<Game> shiftFilter = new ArrayList<>();
      if (shift.equals("morning")) {
        shiftFilter = gameHandler.getGamesByGameShift(GameShift.MORNING);
      } else if (shift.equals("afternoon")) {
        shiftFilter = gameHandler.getGamesByGameShift(GameShift.AFTERNOON);
      } else if (shift.equals("night")) {
        shiftFilter = gameHandler.getGamesByGameShift(GameShift.NIGHT);
      }
      filterGames.retainAll(shiftFilter);
    }

    return filterGames;
  }

  public Game getGameByID(Long searchId) {
    return gameHandler.getGameById(searchId);
  }

  public boolean addGoal(Long gameId, Long teamId, Long playerId) {
    return gameHandler.addGoalToGame(gameId, teamId, playerId);
  }

  public GameResult gameEnd(Long gameId) {
    return gameHandler.registerResult(gameId);
  }
}
