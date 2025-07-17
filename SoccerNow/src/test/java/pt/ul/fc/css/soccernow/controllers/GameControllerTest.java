package pt.ul.fc.css.soccernow.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import pt.ul.fc.css.soccernow.business.controllers.GameController;
import pt.ul.fc.css.soccernow.business.dtos.GameRequestDTO;
import pt.ul.fc.css.soccernow.business.entities.Game;
import pt.ul.fc.css.soccernow.business.entities.GameLocation;
import pt.ul.fc.css.soccernow.business.entities.Stats;
import pt.ul.fc.css.soccernow.business.handlers.GameHandler;
import pt.ul.fc.css.soccernow.business.repositories.GameRepository;

@ActiveProfiles("test")
@WebMvcTest(GameController.class)
public class GameControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private GameRepository gameRepository;

  @MockBean private GameHandler gameHandler;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @BeforeEach
  public void setup() {
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
  }

  @Test
  void testGetAllGames() throws Exception {
    Game game = new Game();
    game.setId(1L);
    game.setStartingTeams(new ArrayList<>());
    game.setLocation(new GameLocation("Rua", "Lisbon")); 
    List<Game> games = List.of(game);

    when(gameHandler.getAllGames()).thenReturn(games);

    mockMvc
        .perform(get("/api/games"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(1L));
  }

  @Test
  void testGetGameById_found() throws Exception {
    Long gameId = 1L;
    Game game = new Game();
    game.setId(gameId);
    game.setStartingTeams(new ArrayList<>());
    game.setLocation(new GameLocation("Estadio Benfica", "Lisbon")); 

    when(gameHandler.getGameById(gameId)).thenReturn(game);

    mockMvc
        .perform(get("/api/games/{gameId}", gameId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(gameId));
  }

  @Test
  void testGetGameById_notFound() throws Exception {
    Long gameId = 2L;
    when(gameHandler.getGameById(gameId)).thenReturn(null);

    mockMvc.perform(get("/api/games/{gameId}", gameId)).andExpect(status().isNotFound());
  }

  @Test
  void testCreateGame_success() throws Exception {
    GameRequestDTO dto = new GameRequestDTO();
    Game game = new Game();
    game.setId(1L);
    game.setStartingTeams(new ArrayList<>());
    game.setLocation(new GameLocation("Rua", "Lisbon")); 

    when(gameHandler.createGame(any(GameRequestDTO.class), eq(Game.GameType.FRIENDLY)))
        .thenReturn(game);

    mockMvc
        .perform(
            post("/api/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(dto)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1L));
  }

  @Test
  void testCreateGame_badRequest() throws Exception {
    GameRequestDTO dto = new GameRequestDTO();

    when(gameHandler.createGame(any(GameRequestDTO.class), eq(Game.GameType.FRIENDLY)))
        .thenReturn(null);

    mockMvc
        .perform(
            post("/api/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(dto)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void testDeleteGame_success() throws Exception {
    Long gameId = 1L;
    when(gameHandler.removeGame(gameId)).thenReturn(true);

    mockMvc.perform(delete("/api/games/{gameId}", gameId)).andExpect(status().isNoContent());
  }

  @Test
  void testDeleteGame_notFound() throws Exception {
    Long gameId = 2L;
    when(gameHandler.removeGame(gameId)).thenReturn(false);

    mockMvc.perform(delete("/api/games/{gameId}", gameId)).andExpect(status().isNotFound());
  }

  @Test
  void testEndGame_Sucess() throws Exception {
    Long gameId = 1L;
    Stats.GameResult result = Stats.GameResult.HOME_TEAM;
    when(gameHandler.registerResult(gameId)).thenReturn(result);

    mockMvc
        .perform(get("/api/games/{gameId}/stats/result", gameId))
        .andExpect(status().isOk())
        .andExpect(content().string("\"HOME_TEAM\""));
  }

  @Test
  void testEndGame_Failed() throws Exception {
    Long gameId = 2L;
    when(gameHandler.registerResult(gameId)).thenReturn(null);

    mockMvc
        .perform(get("/api/games/{gameId}/stats/result", gameId))
        .andExpect(status().isNotFound());
  }

  @Test
  void testGetGamesByLocation_found() throws Exception {
    String address = "Rua";
    String city = "Lisbon";
    Game game = new Game();
    game.setId(1L);
    game.setStartingTeams(new ArrayList<>());
    game.setLocation(new GameLocation("Rua", "Lisbon")); 
    List<Game> games = List.of(game);

    when(gameHandler.getGamesByLocation(any(GameLocation.class))).thenReturn(games);

    mockMvc
        .perform(get("/api/games/location/{address}/{city}", address, city))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(1L));
  }

  @Test
  void testGetGamesByLocation_notFound() throws Exception {
    String address = "Rua";
    String city = "Lisbon";
    when(gameHandler.getGamesByLocation(any(GameLocation.class))).thenReturn(null);

    mockMvc
        .perform(get("/api/games/location/{address}/{city}", address, city))
        .andExpect(status().isNotFound());
  }

  @Test
  void testGetGamesByType_found() throws Exception {
    Game game = new Game();
    game.setId(1L);
    game.setStartingTeams(new ArrayList<>());
    game.setLocation(new GameLocation("Rua", "Lisbon")); 
    List<Game> games = List.of(game);

    when(gameHandler.getGamesByGameType(Game.GameType.FRIENDLY)).thenReturn(games);

    mockMvc
        .perform(get("/api/games/type/{type}", "FRIENDLY"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(1L));
  }

  @Test
  void testGetGamesByType_notFound() throws Exception {
    when(gameHandler.getGamesByGameType(Game.GameType.FRIENDLY)).thenReturn(null);

    mockMvc.perform(get("/api/games/type/{type}", "FRIENDLY")).andExpect(status().isNotFound());
  }

  @Test
  void testAddGoalToGame() throws Exception {
    Long gameId = 1L;
    Long startingTeamId = 2L;
    Long playerId = 3L;

    // Mock the handler to return true (goal added successfully)
    when(gameHandler.addGoalToGame(gameId, startingTeamId, playerId)).thenReturn(true);

    mockMvc
        .perform(
            put(
                    "/api/games/{gameId}/goals/{startingTeamId}/{playerId}",
                    gameId,
                    startingTeamId,
                    playerId)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    // Mock the handler to return false (goal not added)
    when(gameHandler.addGoalToGame(gameId, startingTeamId, playerId)).thenReturn(false);

    mockMvc
        .perform(
            put(
                    "/api/games/{gameId}/goals/{startingTeamId}/{playerId}",
                    gameId,
                    startingTeamId,
                    playerId)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  void testGetGameStats_found() throws Exception {
    Long gameId = 1L;
    Stats stats = new Stats();
    stats.setId(1L);

    when(gameHandler.getStats(gameId)).thenReturn(stats);

    mockMvc
        .perform(get("/api/games/{gameId}/stats", gameId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L));
  }

  @Test
  void testGetGameStats_notFound() throws Exception {
    Long gameId = 2L;

    when(gameHandler.getStats(gameId)).thenReturn(null);

    mockMvc.perform(get("/api/games/{gameId}/stats", gameId)).andExpect(status().isNotFound());
  }

  @Test
  void testGetGamesByGoals_found() throws Exception {
    int intGoals = 3;
    Game game = new Game();
    game.setId(1L);
    game.setStartingTeams(new ArrayList<>());
    game.setLocation(new GameLocation("Rua", "Lisbon")); 

    List<Game> games = List.of(game);

    when(gameHandler.getGameByGoals(intGoals)).thenReturn(games);

    mockMvc
        .perform(get("/api/games/goals/{intGoals}", intGoals))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(1L));
  }

  @Test
  void testGetGamesByGoals_notFound() throws Exception {
    int intGoals = 5;

    when(gameHandler.getGameByGoals(intGoals)).thenReturn(null);

    mockMvc.perform(get("/api/games/goals/{intGoals}", intGoals)).andExpect(status().isNotFound());
  }

  @Test
  void testGetAllFinishedGames_found() throws Exception {
    Game game = new Game();
    game.setId(1L);
    game.setStartingTeams(new ArrayList<>());
    game.setLocation(new GameLocation("Rua", "Lisbon")); 
    List<Game> games = List.of(game);

    when(gameHandler.getAllFinishedGames()).thenReturn(games);

    mockMvc
        .perform(get("/api/games/finished"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(1L));
  }

  @Test
  void testGetAllFinishedGames_notFound() throws Exception {
    when(gameHandler.getAllFinishedGames()).thenReturn(null);

    mockMvc.perform(get("/api/games/finished")).andExpect(status().isNotFound());
  }

  @Test
  void testGetAllUnfinishedGames_found() throws Exception {
    Game game = new Game();
    game.setId(2L);
    game.setStartingTeams(new ArrayList<>());
    game.setLocation(new GameLocation("Rua", "Lisbon")); 
    List<Game> games = List.of(game);

    when(gameHandler.getAllUnfinishedGames()).thenReturn(games);

    mockMvc
        .perform(get("/api/games/Unfinished"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(2L));
  }

  @Test
  void testGetAllUnfinishedGames_notFound() throws Exception {
    when(gameHandler.getAllUnfinishedGames()).thenReturn(null);

    mockMvc.perform(get("/api/games/Unfinished")).andExpect(status().isNotFound());
  }

  @Test
  void testGetAllGamesFromGameShift_found() throws Exception {
    Game game = new Game();
    game.setId(3L);
    game.setStartingTeams(new ArrayList<>());
    game.setLocation(new GameLocation("Rua", "Lisbon")); 
    List<Game> games = List.of(game);

    when(gameHandler.getGamesByGameShift(Game.GameShift.MORNING)).thenReturn(games);

    mockMvc
        .perform(get("/api/games/gameshift/{gameshift}", "MORNING"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(3L));
  }

  @Test
  void testGetAllGamesFromGameShift_notFound() throws Exception {
    when(gameHandler.getGamesByGameShift(Game.GameShift.NIGHT)).thenReturn(null);

    mockMvc
        .perform(get("/api/games/gameshift/{gameshift}", "NIGHT"))
        .andExpect(status().isNotFound());
  }
}
