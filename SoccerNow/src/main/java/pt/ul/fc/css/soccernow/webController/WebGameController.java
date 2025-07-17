package pt.ul.fc.css.soccernow.webController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import pt.ul.fc.css.soccernow.business.entities.Game;
import pt.ul.fc.css.soccernow.business.entities.Player;
import pt.ul.fc.css.soccernow.business.entities.StartingTeam;
import pt.ul.fc.css.soccernow.business.entities.Stats;
import pt.ul.fc.css.soccernow.business.handlers.UserHandler;
import pt.ul.fc.css.soccernow.business.services.GameService;
import pt.ul.fc.css.soccernow.business.services.TeamService;

@Controller
public class WebGameController {
  @Autowired GameService gameService;
  @Autowired UserHandler userHandler;
  @Autowired TeamService teamService;

  private Long gameId = 0L;

  public WebGameController() {
    super();
  }

  @GetMapping("/games/search")
  public String searchGameById(
      @RequestParam(value = "searchId", required = false) Long searchId, Model model) {
    getGames(searchId, model);
    return "games";
  }

  private void getGames(Long searchId, Model model) {
    List<Game> games = new ArrayList<>();
    if (searchId != null) {
      gameId = searchId;
      Game game = gameService.getGameByID(searchId);
      if (game != null) {
        games.add(game);

        if (game.getStats() != null) {
          Map<Long, String> players = new HashMap<>();

          // Combine home and away player IDs
          Set<Long> ids = new HashSet<>();
          if (game.getStats().getGoalsHomeTeam() != null) {

            ids.addAll(game.getStats().getGoalsHomeTeam().keySet());
          }
          if (game.getStats().getGoalsAwayTeam() != null) {
            ids.addAll(game.getStats().getGoalsAwayTeam().keySet());
          }

          // Populate map with full player names
          for (Long id : ids) {
            Player player = userHandler.getPlayerById(id); // Avoid multiple calls
            if (player != null) {
              players.put(id, player.getFirstName() + " " + player.getLastName());
            }
          }

          model.addAttribute("players", players);
          int totalHomeGoals = game.getStats().getTotalGoals(game.getStats().getGoalsHomeTeam());
          int totalAwayGoals = game.getStats().getTotalGoals(game.getStats().getGoalsAwayTeam());

          model.addAttribute("totalHomeGoals", totalHomeGoals);
          model.addAttribute("totalAwayGoals", totalAwayGoals);
        }
      }
    }
    model.addAttribute("games", games);
  }

  @GetMapping("/games/filter")
  public String filterGames(
      @RequestParam(value = "status", required = false) String status,
      @RequestParam(value = "goals", required = false) Integer goals,
      @RequestParam(value = "city", required = false) String city,
      @RequestParam(value = "address", required = false) String address,
      @RequestParam(value = "shift", required = false) String shift,
      Model model) {

    List<Game> filteredGames = gameService.filterGames(status, goals, city, address, shift);

    Map<Long, Map<Long, String>> playersMap = new HashMap<>();
    Map<Long, Integer> homeGoalsMap = new HashMap<>();
    Map<Long, Integer> awayGoalsMap = new HashMap<>();

    for (Game game : filteredGames) {
      if (game.getStats() != null) {
        Map<Long, String> players = new HashMap<>();
        Set<Long> ids = new HashSet<>();

        if (game.getStats().getGoalsHomeTeam() != null) {
          ids.addAll(game.getStats().getGoalsHomeTeam().keySet());
        }

        if (game.getStats().getGoalsAwayTeam() != null) {
          ids.addAll(game.getStats().getGoalsAwayTeam().keySet());
        }

        for (Long id : ids) {
          Player player = userHandler.getPlayerById(id);
          if (player != null) {
            players.put(id, player.getFirstName() + " " + player.getLastName());
          }
        }

        playersMap.put(game.getId(), players);

        homeGoalsMap.put(
            game.getId(), game.getStats().getTotalGoals(game.getStats().getGoalsHomeTeam()));
        awayGoalsMap.put(
            game.getId(), game.getStats().getTotalGoals(game.getStats().getGoalsAwayTeam()));
      }
    }

    model.addAttribute("filteredGames", filteredGames);
    model.addAttribute("playersMap", playersMap);
    model.addAttribute("homeGoalsMap", homeGoalsMap);
    model.addAttribute("awayGoalsMap", awayGoalsMap);
    model.addAttribute("filteredGames", filteredGames);
    return "games";
  }

  @GetMapping("/games/addGoal")
  public String addGoal(
    @RequestParam(value = "team", required = false) Long teamId,
    @RequestParam(value = "player", required = false) Long playerId,
    Model model
  ){
    if(teamId!=null && playerId!=null){
      boolean goalAdd = gameService.addGoal(gameId,teamId,playerId);
      if(goalAdd){
        model.addAttribute("error", null);
      }else{
        model.addAttribute("error", "Erro a adicionar golo");
      }
    }else{
      model.addAttribute("error", "Tem de prencher todos os campos");
    }
    getGames(gameId, model);
    return "games";
  }

  @GetMapping("/games/startingTeamPlayers")
  @ResponseBody
  public List<Map<String, Object>> getStartingTeamPlayers(@RequestParam("startingTeamId") Long startingTeamId) {
      List<Map<String, Object>> playersList = new ArrayList<>();
      // Supondo que tens acesso ao handler ou service para buscar a StartingTeam
      StartingTeam st = teamService.getStartingTeamById(startingTeamId);
      if (st != null) {
          for (Player p : st.getPlayers()) {
            if(p.getId()!=st.getGoalkeeper().getId()){
              Map<String, Object> playerMap = new HashMap<>();
              playerMap.put("id", p.getId());
              playerMap.put("name", p.getFirstName() + " " + p.getLastName());
              playersList.add(playerMap);
            }
          }
          // Adiciona o guarda-redes também, se quiseres
          if (st.getGoalkeeper() != null) {
              Player gk = st.getGoalkeeper();
              Map<String, Object> gkMap = new HashMap<>();
              gkMap.put("id", gk.getId());
              gkMap.put("name", gk.getFirstName() + " " + gk.getLastName() + " (GR)");
              playersList.add(gkMap);
          }
      }
      return playersList;
  }

  @GetMapping("/games/end")
  public String endGame(Model model){
    Stats.GameResult result = gameService.gameEnd(gameId);
    if(result!=null){
      model.addAttribute("error", null);
    }else{
      model.addAttribute("error", "Erro a acabar o jogo");
    }
    getGames(gameId, model);
    return "games";
  }

}
