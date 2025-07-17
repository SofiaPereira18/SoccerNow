package pt.ul.fc.css.soccernow.business.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import pt.ul.fc.css.soccernow.business.entities.Game;
import pt.ul.fc.css.soccernow.business.entities.Player;
import pt.ul.fc.css.soccernow.business.entities.StartingTeam;
import pt.ul.fc.css.soccernow.business.entities.Team;
import pt.ul.fc.css.soccernow.business.repositories.TeamRepository;

@Component
public class TeamHandler {

  private TeamRepository tRepository;
  // private GameHandler gameHandler;
  private UserHandler playerHandler;
  private StartingTeamHandler stHandler;
  private UserHandler userHandler;

  @Autowired
  public TeamHandler(
      TeamRepository tRepository,
      @Lazy GameHandler gameHandler,
      @Lazy UserHandler playerHandler,
      @Lazy StartingTeamHandler stHandler) {
    // this.gameHandler = gameHandler;
    this.playerHandler = playerHandler;
    this.stHandler = stHandler;
    this.tRepository = tRepository;
  }

  public boolean removeTeam(Long id) {
    Optional<Team> teamFound = tRepository.findActiveById(id);
    if (teamFound.isPresent()) {
      Team team = teamFound.get();
      boolean hasGamesToPlay = false;
      boolean hasGames = false;
      boolean hasChampionshipsToPlay =
          (team.getChampionshipsNotFinished() != null
              && !team.getChampionshipsNotFinished().isEmpty());
      boolean hasChampionships =
          (team.getChampionships() != null && !team.getChampionships().isEmpty());

      List<StartingTeam> stList = team.getStartingTeam();
      for (int index = 0; (index < stList.size() && !hasGamesToPlay); index++) {
        StartingTeam st = stList.get(index);
        List<Game> games = st.getGames();
        if (games != null && !games.isEmpty()) {
          hasGames = true;
          for (int i = 0; i < games.size() && !hasGamesToPlay; i++) {
            Game g = games.get(index);
            hasGamesToPlay = g.isActive() && g.isFinished();
          }
        }
      }

      if (hasChampionshipsToPlay || hasGamesToPlay) {
        return false;

      } else if (hasChampionships || hasGames) {
        team.setActive(false);
        setAllSTActive(team.getStartingTeam());
        tRepository.save(team);
        return true;

      } else {
        // Remove all players from the team
        List<Player> players = team.getPlayers();
        for (Player player : players) {
          player.removeTeam(team);
          playerHandler.save(player);
        }
        // Remove all starting teams from the team
        List<StartingTeam> sTeams = team.getStartingTeam();
        for (StartingTeam st : sTeams) {
          if (st.getGoalkeeper() != null) {
            Player gk = st.getGoalkeeper();
            gk.removeStartingTeam(st);
            playerHandler.save(gk);
          }
          for (Player p : st.getPlayers()) {
            p.removeStartingTeam(st);
            playerHandler.save(p);
          }
          st.setTeam(null);
          stHandler.delete(st);
        }
        tRepository.delete(team);
        return true;
      }
    }
    return false;
  }

  private void setAllSTActive(List<StartingTeam> stList) {
    if (stList != null && !stList.isEmpty()) {
      for (StartingTeam st : stList) {
        if (st.isActive()) {
          st.setActive(false);
          stHandler.save(st);
        }
      }
    }
  }

  public List<Team> getAllTeams() {
    List<Team> teamsFound = tRepository.findAllTeams();
    return teamsFound;
  }

  public Team getTeamByName(String name) {
    Optional<Team> teamFound = tRepository.findByName(name);
    if (teamFound.isPresent()) {
      return teamFound.get();
    }
    return null;
  }

  public Team registerTeam(String name) {
    Optional<Team> teamFound = tRepository.findByName(name);
    if (teamFound.isPresent()) {
      return null;
    }

    Team team = new Team(name);
    return tRepository.save(team);
  }

  public Team addPlayer(Long teamId, Long playerId) {
    Optional<Team> teamfound = tRepository.findActiveById(teamId);
    Player player = playerHandler.getPlayerById(playerId);

    if (teamfound.isPresent() && player != null) {
      Team team = teamfound.get();

      if (team.getPlayers().contains(player) || player.getTeams().contains(team)) {
        return null;
      }

      team.addPlayer(player);
      tRepository.save(team);

      player.addTeam(team);
      playerHandler.save(player);

      return team;
    }

    return null;
  }

  public List<Game> getAllGames(Long team_id) {
    Optional<Team> teamfound = tRepository.findActiveById(team_id);

    if (teamfound.isPresent()) {
      // Get all starting teams from the team
      List<StartingTeam> stList = teamfound.get().getStartingTeam();
      if (stList == null || stList.size() == 0) {
        return null;
      }
      // Get all games from the starting teams
      List<Game> gamesAll = new ArrayList<>();
      for (StartingTeam st : stList) {
        List<Game> games = st.getGames();
        if (games != null && games.size() != 0) {
          for (Game game : games) {
            if (game.isActive()) {
              gamesAll.add(game);
            }
          }
        }
      }
      return gamesAll;
    }
    return null;
  }

  public List<StartingTeam> getStartingTeam(Long team_id) {
    Optional<Team> teamfound = tRepository.findActiveById(team_id);
    if (teamfound.isPresent()) {
      return teamfound.get().getStartingTeam();
    }
    return null;
  }

  public Team getTeamById(Long id) {
    Optional<Team> teamFound = tRepository.findActiveById(id);
    if (teamFound.isPresent()) {
      return teamFound.get();
    }
    return null;
  }

  public boolean removePlayer(Long teamId, Long playerId) {
    boolean removedPlayer = false;
    Player player = playerHandler.getPlayerById(playerId);
    Optional<Team> teamFound = tRepository.findActiveById(teamId);
    if (player != null && teamFound.isPresent()) {
      Team team = teamFound.get();

      if (team.getPlayers().contains(player) && player.getTeams().contains(team)) {
        List<StartingTeam> stList = player.getStartingTeams();
        if (stList != null && !stList.isEmpty()) {
          for (StartingTeam st : stList) {

            // verificar se o st esta na equipa e se isActive
            if (team.getStartingTeam().contains(st) && st.isActive()) {

              // verificar se o st tem jogos independente se isActive
              if (st.getGames() != null && st.getGames().size() != 0) {

                // encontrar os jogos que ainda não acabaram (que são obrigatóriamente isActive)
                List<Game> gamesToPlay = st.getAllToPlayGames();

                // se a lista for vazia ou não existir, fazer
                // que o st fique isActive=false e tirar o player da team
                if (gamesToPlay == null || gamesToPlay.isEmpty()) {
                  st.setActive(false);
                  player.removeTeam(team);
                  team.removePlayer(player);
                  playerHandler.save(player);
                  stHandler.save(st);
                  tRepository.save(team);
                  removedPlayer = true;
                }

                // se nao tiver jogos remover o st da equipa
              } else {
                removeStartingTeam(team.getId(), st.getId());
                removedPlayer = true;
              }
            }
          }
          if (!removedPlayer) {
            team.removePlayer(player);
            player.removeTeam(team);

            playerHandler.save(player);
            tRepository.save(team);
            removedPlayer = true;
          }

          // se o jogador não tiver st simplesmente tirar-lo da team
        } else {
          team.removePlayer(player);
          player.removeTeam(team);

          playerHandler.save(player);
          tRepository.save(team);
          removedPlayer = true;
        }
      }
    }
    return removedPlayer;
  }

  public List<Player> getPlayers(Long teamId) {
    Optional<Team> teamFound = tRepository.findActiveById(teamId);
    if (teamFound.isPresent()) {
      Team t = teamFound.get();
      return t.getPlayers();
    }
    return null;
  }

  public boolean removeStartingTeam(Long teamId, Long stId) {
    Optional<Team> teamFound = tRepository.findActiveById(teamId);
    Optional<StartingTeam> stFound = stHandler.findById(stId);
    if (teamFound.isPresent() && stFound.isPresent()) {
      // verificar se st isActive e se esta na equipa
      if (stFound.get().isActive() && teamFound.get().getStartingTeam().contains(stFound.get())) {
        StartingTeam st = stFound.get();
        Team team = teamFound.get();
        List<Game> games = st.getGames();
        // verificar se tem jogos independentemente se isActive e se isFinished
        if (games != null && games.size() != 0) {
          List<Game> gamesToPlay = st.getAllToPlayGames();
          // se tiver jogos para jogar não deixar remover a equipa
          if (gamesToPlay != null && gamesToPlay.size() != 0) {
            return false;
          } else {
            st.setActive(false);
            stHandler.save(st);
            return true;
          }
        }
        // se não tiver jogos remover todas as ligaçoes em st e dar delete
        if (st.getGoalkeeper() != null) {
          Player gk = st.getGoalkeeper();
          gk.removeStartingTeam(st);
          playerHandler.save(gk);
        }
        for (Player p : st.getPlayers()) {
          p.removeStartingTeam(st);
          playerHandler.save(p);
        }
        team.removeStartingTeam(st);
        tRepository.save(team);
        stHandler.delete(st);
        return true;
      }
    }
    return false;
  }

  public StartingTeam createStartingTeamWithPlayer(
      Long teamId, List<Long> playersId, Long goalKeeperId) {
    Optional<Team> teamFound = tRepository.findActiveById(teamId);
    Player gk = playerHandler.getPlayerById(goalKeeperId);
    List<Player> players = new ArrayList<>();
    for (Long id : playersId) {
      Player player = playerHandler.getPlayerById(id);
      if (player != null) {
        players.add(player);
      }
    }

    if (teamFound.isPresent() && gk != null) {

      Team team = teamFound.get();

      for (Player player : players) {
        if (!player.getTeams().contains(team)) {
          return null;
        }
      }

      if (!gk.getTeams().contains(team)) {
        return null;
      }
      StartingTeam st = new StartingTeam(team, players, gk);

      stHandler.save(st);

      for (Player p : players) {
        p.addStartingTeam(st);
        playerHandler.save(p);
      }
      gk.addStartingTeam(st);
      playerHandler.save(gk);

      team.addStartingTeam(st);
      tRepository.save(team);
      return st;
    }

    return null;
  }

  public StartingTeam getStartingTeamById(Long id) {
    Optional<StartingTeam> stFound = stHandler.findById(id);
    if (stFound.isPresent()) {
      return stFound.get();
    }
    return null;
  }

  public List<Team> findTeamsByPlayerId(Long id) {
    return tRepository.findTeamsByPlayerId(id);
  }

  public void save(Team team) {
    tRepository.save(team);
  }

  public List<Team> findAllById(List<Long> ids) {
    return tRepository.findAllById(ids);
  }

  public List<StartingTeam> getAllStartingTeams() {
    List<StartingTeam> stList = stHandler.findAll();
    if (stList != null && stList.size() != 0) {
      return stList;
    }
    return null;
  }

  public List<Game> getAllFinishedGames(Long team_id) {
    Optional<Team> teamfound = tRepository.findActiveById(team_id);
    if (teamfound.isPresent()) {
      // Get all starting teams from the team
      List<StartingTeam> stList = teamfound.get().getStartingTeam();
      if (stList == null || stList.size() == 0) {
        return null;
      }
      // Get all games from the starting teams
      List<Game> gamesAll = new ArrayList<>();
      for (StartingTeam st : stList) {
        if (st.isActive()) {
          List<Game> games = st.getGames();
          if (games != null && games.size() != 0) {
            for (Game game : games) {
              if (game.isActive() && !game.isFinished()) {
                gamesAll.add(game);
              }
            }
          }
        }
      }
      return gamesAll;
    }
    return null;
  }
}
