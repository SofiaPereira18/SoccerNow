package pt.ul.fc.css.soccernow.business.controllers;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.ul.fc.css.soccernow.business.dtos.GameDTO;
import pt.ul.fc.css.soccernow.business.dtos.GameRequestDTO;
import pt.ul.fc.css.soccernow.business.dtos.StatsDTO;
import pt.ul.fc.css.soccernow.business.entities.Game;
import pt.ul.fc.css.soccernow.business.entities.GameLocation;
import pt.ul.fc.css.soccernow.business.entities.Stats;
import pt.ul.fc.css.soccernow.business.handlers.GameHandler;
import pt.ul.fc.css.soccernow.business.mappers.GameMapper;
import pt.ul.fc.css.soccernow.business.mappers.StatsMapper;

@RestController
@RequestMapping("/api/games")
@Api(value = "Game API", tags = "Game")
public class GameController {

  private GameHandler gameHandler;

  public GameController(GameHandler gameHandler) {
    this.gameHandler = gameHandler;
  }

  @Operation(summary = "Create a new friendly game", description = "Creates a new firendly game.")
  @PostMapping
  public ResponseEntity<GameDTO> createGame(@RequestBody GameRequestDTO gameRequestDTO) {
    Game game = gameHandler.createGame(gameRequestDTO, Game.GameType.FRIENDLY);

    if (game == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    GameDTO responseDTO = GameMapper.toDTO(game);

    return ResponseEntity.created(URI.create("/api/game/" + game.getId())).body(responseDTO);
  }

  @Operation(summary = "Get all games", description = "Returns a list of all games.")
  @GetMapping
  public ResponseEntity<List<GameDTO>> getAllGames() {
    List<Game> games = gameHandler.getAllGames();
    List<GameDTO> gamesDTO =
        games.stream().map(g -> GameMapper.toDTO(g)).collect(Collectors.toList());

    return ResponseEntity.ok(gamesDTO);
  }

  @Operation(summary = "Get game by Id", description = "Returns a game by its ID.")
  @GetMapping("/{gameId}")
  public ResponseEntity<GameDTO> getGameById(@PathVariable("gameId") Long id) {
    Game game = gameHandler.getGameById(id);
    if (game != null) {
      return ResponseEntity.ok(GameMapper.toDTO(game));
    }
    return ResponseEntity.notFound().build();
  }

  @Operation(
      summary = "Get game by address and city",
      description = "Returns a list of games for a specific location.")
  @GetMapping("/location/{address}/{city}")
  public ResponseEntity<List<GameDTO>> getGamesByLocation(
      @PathVariable String address, @PathVariable String city) {
    GameLocation location = new GameLocation(address, city);
    List<Game> games = gameHandler.getGamesByLocation(location);
    if (games != null) {

      List<GameDTO> dtos =
          games.stream().map(g -> GameMapper.toDTO(g)).collect(Collectors.toList());

      return ResponseEntity.ok(dtos);
    }
    return ResponseEntity.notFound().build();
  }

  @Operation(
      summary = "Get games by type",
      description = "Returns a list of games for a specific type.")
  @GetMapping("type/{type}")
  public ResponseEntity<List<GameDTO>> getGamesByType(@PathVariable Game.GameType type) {
    List<Game> games = gameHandler.getGamesByGameType(type);
    if (games != null) {

      List<GameDTO> dtos =
          games.stream().map(g -> GameMapper.toDTO(g)).collect(Collectors.toList());

      return ResponseEntity.ok(dtos);
    }
    return ResponseEntity.notFound().build();
  }

  @Operation(summary = "Show statistics of a game", description = "Shows statistcs of a game")
  @GetMapping("/{gameId}/stats")
  public ResponseEntity<StatsDTO> getGameStats(@PathVariable Long gameId) {
    Stats stats = gameHandler.getStats(gameId);
    if (stats != null) {
      return ResponseEntity.ok(StatsMapper.toDTO(stats));
    }
    return ResponseEntity.notFound().build();
  }

  @Operation(summary = "Get games by total of goals", description = "Gets games by total of goals")
  @GetMapping("/goals/{intGoals}")
  public ResponseEntity<List<GameDTO>> getGamesByGoals(@PathVariable int intGoals) {
    List<Game> games = gameHandler.getGameByGoals(intGoals);
    if (games != null) {
      List<GameDTO> dtos = games.stream().map(GameMapper::toDTO).collect(Collectors.toList());
      return ResponseEntity.ok(dtos);
    }
    return ResponseEntity.notFound().build();
  }

  @Operation(summary = "Get all finished games", description = "Shows all the finished games")
  @GetMapping("/finished")
  public ResponseEntity<List<GameDTO>> getAllFinishedGames() {
    List<Game> games = gameHandler.getAllFinishedGames();
    if (games != null) {
      List<GameDTO> dtoResponse = games.stream().map(game -> GameMapper.toDTO(game)).toList();
      return ResponseEntity.ok(dtoResponse);
    }
    return ResponseEntity.notFound().build();
  }

  @Operation(
      summary = "Get all unfinished games",
      description = "Shows all the games that are still playingor to play")
  @GetMapping("/Unfinished")
  public ResponseEntity<List<GameDTO>> getAllUnfinishedGames() {
    List<Game> games = gameHandler.getAllUnfinishedGames();
    if (games != null) {
      List<GameDTO> dtoResponse = games.stream().map(game -> GameMapper.toDTO(game)).toList();
      return ResponseEntity.ok(dtoResponse);
    }
    return ResponseEntity.notFound().build();
  }

  @Operation(
      summary = "Gets all games from a game shift",
      description = "Gets all games from a game shift")
  @GetMapping("/gameshift/{gameshift}")
  public ResponseEntity<List<GameDTO>> getAllGamesFromGameShift(
      @PathVariable Game.GameShift gameshift) {
    List<Game> games = gameHandler.getGamesByGameShift(gameshift);
    if (games != null) {
      List<GameDTO> dtoResponse = games.stream().map(GameMapper::toDTO).toList();
      return ResponseEntity.ok(dtoResponse);
    }
    return ResponseEntity.notFound().build();
  }

  @Operation(summary = "Add goal to game", description = "Adds goal to game")
  @PutMapping("/{gameId}/goals/{startingTeamId}/{playerId}")
  public ResponseEntity<?> addGoalToGame(
      @PathVariable Long gameId, @PathVariable Long startingTeamId, @PathVariable Long playerId) {
    if (gameHandler.addGoalToGame(gameId, startingTeamId, playerId)) {
      return ResponseEntity.ok().build();
    }
    return ResponseEntity.badRequest().build();
  }

  @Operation(summary = "End Game", description = "Ends a game, and returns the it's result")
  @GetMapping("/{gameId}/stats/result")
  public ResponseEntity<Stats.GameResult> endGame(@PathVariable Long gameId) {
    Stats.GameResult result = gameHandler.registerResult(gameId);
    if (result != null) {
      return ResponseEntity.ok(result);
    }
    return ResponseEntity.notFound().build();
  }

  @Operation(summary = "Delete game", description = "Deletes a game by its ID.")
  @DeleteMapping("/{gameId}")
  public ResponseEntity<Void> deleteGame(@PathVariable("gameId") Long id) {
    boolean deleted = gameHandler.removeGame(id);
    if (deleted) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.notFound().build();
  }
}
