package pt.ul.fc.css.soccernow.business.handlers;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import pt.ul.fc.css.soccernow.business.dtos.GameRequestDTO;
import pt.ul.fc.css.soccernow.business.entities.Card;
import pt.ul.fc.css.soccernow.business.entities.Championship;
import pt.ul.fc.css.soccernow.business.entities.Game;
import pt.ul.fc.css.soccernow.business.entities.GameLocation;
import pt.ul.fc.css.soccernow.business.entities.Player;
import pt.ul.fc.css.soccernow.business.entities.Referee;
import pt.ul.fc.css.soccernow.business.entities.RefereeTeam;
import pt.ul.fc.css.soccernow.business.entities.StartingTeam;
import pt.ul.fc.css.soccernow.business.entities.Stats;
import pt.ul.fc.css.soccernow.business.repositories.CardRepository;
import pt.ul.fc.css.soccernow.business.repositories.ChampionshipRepository;
import pt.ul.fc.css.soccernow.business.repositories.GameRepository;
import pt.ul.fc.css.soccernow.business.repositories.StatsRepository;

@Component
public class GameHandler {

  private GameRepository gameRepo;
  private StatsRepository statsRepo;
  private CardRepository cardRepository;

  private RefereeTeamHandler refereeTeamHandler;
  private StartingTeamHandler startTeamHandler;
  private ChampionshipHandler championshipHandler;

  public GameHandler(
      GameRepository gameRepository,
      StatsRepository statsRepository,
      ChampionshipRepository championshipRepository,
      CardRepository cardRepository,
      @Lazy RefereeTeamHandler refereeTeamHandler,
      @Lazy StartingTeamHandler startTeamHandler,
      @Lazy ChampionshipHandler championshipHandler) {
    this.championshipHandler = championshipHandler;
    this.refereeTeamHandler = refereeTeamHandler;
    this.startTeamHandler = startTeamHandler;
    this.gameRepo = gameRepository;
    this.statsRepo = statsRepository;
    this.cardRepository = cardRepository;
  }

  public List<Game> getAllGames() {
    return gameRepo.findByIsActiveTrue();
  }

  public Game getGameById(Long gameId) {
    Optional<Game> optionalGame = gameRepo.findActiveById(gameId);
    if (optionalGame.isPresent()) {
      return optionalGame.get();
    }
    return null;
  }

  public List<Game> getAllFinishedGames() {
    return gameRepo.findAllFinished();
  }

  public List<Game> getAllUnfinishedGames() {
    return gameRepo.findAllUnfinished();
  }

  public Game createGame(GameRequestDTO gameRequestDTO, Game.GameType gameType) {

    Long refereeTeamId = gameRequestDTO.getRefereeTeamId();

    if (refereeTeamId == null) {
      throw new IllegalArgumentException("Referee team ID must not be null.");
    }

    RefereeTeam refereeTeam = refereeTeamHandler.getRefereeTeamById(refereeTeamId);

    List<Long> teamIds = gameRequestDTO.getTeamsIds();
    if (teamIds == null || teamIds.size() < 2) {
      throw new IllegalArgumentException("teamsIds must contain at least two team IDs.");
    }
    StartingTeam team1 = fetchAndValidateTeam(teamIds.get(0));
    StartingTeam team2 = fetchAndValidateTeam(teamIds.get(1));

    GameLocation location = new GameLocation(gameRequestDTO.getCity(), gameRequestDTO.getStadium());

    if (hasConflicts(
        team1,
        team2,
        refereeTeam,
        new GameLocation(gameRequestDTO.getStadium(), gameRequestDTO.getCity()),
        gameRequestDTO.getDateTime())) {

      throw new IllegalStateException("Schedule or location conflict detected.");
    }

    Stats stats = statsRepo.save(new Stats());
    List<StartingTeam> teams = new ArrayList<>(Arrays.asList(team1, team2));

    Championship championship = null;

    Game initialGame =
        new Game(
            gameRequestDTO.getDateTime(),
            new GameLocation(gameRequestDTO.getStadium(), gameRequestDTO.getCity()),
            teams,
            refereeTeam,
            championship);

    initialGame.setGameType(gameType);

    initialGame.setStats(stats);
    Game savedGame = gameRepo.save(initialGame);

    refereeTeam.addGame(savedGame);
    refereeTeamHandler.save(refereeTeam);

    teams.forEach(t -> t.addGame(savedGame));
    for (StartingTeam team : teams) {
      startTeamHandler.save(team);
    }

    return savedGame;
  }

  private StartingTeam fetchAndValidateTeam(Long teamId) {
    StartingTeam team =
        startTeamHandler
            .findById(teamId)
            .orElseThrow(
                () -> new EntityNotFoundException("Team with id " + teamId + " not found"));
    if (team.getPlayers().isEmpty()) {
      throw new EntityNotFoundException("Team with id " + teamId + " has no players");
    }
    return team;
  }

  private boolean hasConflicts(
      StartingTeam t1,
      StartingTeam t2,
      RefereeTeam ref,
      GameLocation location,
      LocalDateTime dateTime) {

    return !t1.isActive()
        || !t2.isActive()
        || !ref.getIsActive()
        || fromSameTeams(t1, t2)
        || !allAvailable(t1, dateTime)
        || !allAvailable(t2, dateTime)
        || !allAvailable(ref, dateTime)
        || !locationAvailable(location, dateTime)
        || hasSamePlayers(t1, t2);
  }

  private boolean locationAvailable(GameLocation location, LocalDateTime dateTime) {
    List<Game> games = gameRepo.findByLocation(location);
    if (games != null && !games.isEmpty()) {
      for (Game game : games) {
        LocalDateTime otherTime = game.getDateTime();
        if (!dateTime.isBefore(otherTime.minusHours(1))
            && !dateTime.isAfter(otherTime.plusHours(1))) {
          return false;
        }
      }
    }
    return true;
  }

  private boolean allAvailable(StartingTeam team, LocalDateTime dateTime) {
    for (Player player : team.getPlayers()) {
      for (StartingTeam st : player.getStartingTeams()) {
        List<Game> games = st.getGames();
        if (games == null || games.size() == 0) {
          continue;
        }
        for (Game game : games) {
          LocalDateTime otherTime = game.getDateTime();
          if (!dateTime.isBefore(otherTime.minusHours(1))
              && !dateTime.isAfter(otherTime.plusHours(1))) {
            return false;
          }
        }
      }
    }
    return true;
  }

  private boolean allAvailable(RefereeTeam team, LocalDateTime dateTime) {
    for (Referee referee : team.getReferees()) {
      for (RefereeTeam rt : referee.getRefereeTeams()) {
        for (Game game : rt.getGames()) {
          LocalDateTime otherTime = game.getDateTime();
          if (!dateTime.isBefore(otherTime.minusHours(1))
              && !dateTime.isAfter(otherTime.plusHours(1))) {
            return false;
          }
        }
      }
    }
    return true;
  }

  private boolean fromSameTeams(StartingTeam team1, StartingTeam team2) {
    return team1.getTeam().getId() == team2.getTeam().getId();
  }

  private boolean hasSamePlayers(StartingTeam team1, StartingTeam team2) {
    for (Player player : team1.getPlayers()) {
      if (team2.getPlayers().contains(player)) {
        return true;
      }
    }
    return false;
  }

  public boolean removeGame(Long gameId) {
    Optional<Game> optionalGame = gameRepo.findById(gameId);
    if (optionalGame.isPresent()) {
      Game game = optionalGame.get();
      game.setActive(false);
      gameRepo.save(game);
      return true;
    }
    return false;
  }

  public Stats.GameResult registerResult(Long gameId) {
    Optional<Game> optionalGame = gameRepo.findById(gameId);
    Stats.GameResult result = null;
    if (optionalGame.isPresent()) {
      Game game = optionalGame.get();
      Stats stats = game.getStats();
      if (stats != null) {
        Map<Long, Integer> goalsHomeTeam = stats.getGoalsHomeTeam();
        Map<Long, Integer> goalsAwayTeam = stats.getGoalsAwayTeam();
        int homeGoals = game.getStats().getTotalGoals(goalsHomeTeam);
        int awayGoals = game.getStats().getTotalGoals(goalsAwayTeam);
        if (homeGoals > awayGoals) {
          result = Stats.GameResult.HOME_TEAM;
        } else if (homeGoals < awayGoals) {
          result = Stats.GameResult.AWAY_TEAM;
        } else {
          result = Stats.GameResult.DRAW;
        }
        stats.setResult(result);
        statsRepo.save(stats);
      } else {
        Stats newStats = new Stats();
        newStats.setResult(Stats.GameResult.DRAW);
        statsRepo.save(newStats);
        game.setStats(newStats);
      }

      game.setFinished(true);
      gameRepo.save(game);

      if (game.getGameType() == Game.GameType.CHAMPIONSHIP) {
        Championship championship = game.getChampionship();
        System.out.println("Championship: " + championship);
        return championshipHandler.addPointsToTeam(championship.getId(), game) ? result : null;
      }
      return result;
    }
    return null;
  }

  public List<Game> getGamesByLocation(GameLocation location) {
    return gameRepo.findByLocation(location);
  }

  public List<Game> getGamesByGameType(Game.GameType gameType) {
    return gameRepo.findByGameType(gameType);
  }

  public void save(Game game) {
    gameRepo.save(game);
  }

  public List<Stats> getAllStats() {
    return statsRepo.findAll();
  }

  public void saveStats(Stats stats) {
    statsRepo.save(stats);
  }

  public void deleteCard(Card card) {
    cardRepository.delete(card);
  }

  public Optional<Card> findCardById(Long id) {
    return cardRepository.findById(id);
  }

  public List<Card> findAllCards() {
    return cardRepository.findAll();
  }

  public List<Card> findAllCardsById(List<Long> ids) {
    return cardRepository.findAllById(ids);
  }

  public List<Game> findAllGamesById(List<Long> ids) {
    return gameRepo.findAllById(ids);
  }

  public Card saveCard(Card card) {
    return cardRepository.save(card);
  }

  public List<Card> findCardByPlayer(Player player) {
    return cardRepository.findByPlayer(player);
  }

  public boolean addGoalToGame(Long gameId, Long startingTeamId, Long playerId) {

    Game game = gameRepo.findActiveById(gameId).get();
    if (game == null || !game.isActive() || game.isFinished()) {
      return false;
    }

    List<StartingTeam> startingTeams = game.getStartingTeams();

    if (startingTeams == null || startingTeams.isEmpty()) {
      return false;
    }

    if (startingTeams.get(0).getId() != startingTeamId
        && startingTeams.get(1).getId() != startingTeamId) {
      return false;
    }

    List<Long> players = null;

    Boolean isHomeTeam = startingTeams.get(0).getId() == startingTeamId;

    if (isHomeTeam) {
      players = startingTeams.get(0).getPlayers().stream().map(Player::getId).toList();
    } else {
      players = startingTeams.get(1).getPlayers().stream().map(Player::getId).toList();
    }

    if (!players.contains(playerId)) {
      return false;
    }

    Stats stats = game.getStats();

    if (stats == null) {
      stats = new Stats();
    }

    if (isHomeTeam) {
      stats.addGoalToHomeTeam(playerId);
    } else {
      stats.addGoalToAwayTeam(playerId);
    }

    Stats statsSaved = statsRepo.save(stats);
    game.setStats(statsSaved);
    gameRepo.save(game);

    return true;
  }

  public Stats getStats(Long gameId) {
    Game game = gameRepo.findActiveById(gameId).get();

    if (game == null) {
      return null;
    }

    return game.getStats();
  }

  public List<Game> getGameByGoals(int goals) {
    List<Game> games = gameRepo.findAllFinished();

    List<Game> result = new ArrayList<>();

    for (Game game : games) {

      Map<Long, Integer> goalsH = game.getStats().getGoalsHomeTeam();
      Map<Long, Integer> goalsA = game.getStats().getGoalsAwayTeam();

      if (game.getStats().getTotalGoals(goalsA) + game.getStats().getTotalGoals(goalsH) == goals) {

        result.add(game);
      }
    }
    return result;
  }

  public List<Game> getGamesByGameShift(Game.GameShift gameShift) {
    return gameRepo.findGameByGameShift(gameShift);
  }
}
