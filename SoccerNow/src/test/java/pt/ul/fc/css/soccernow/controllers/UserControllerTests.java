package pt.ul.fc.css.soccernow.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import pt.ul.fc.css.soccernow.business.controllers.UserController;
import pt.ul.fc.css.soccernow.business.dtos.PlayerDTO;
import pt.ul.fc.css.soccernow.business.dtos.RefereeDTO;
import pt.ul.fc.css.soccernow.business.dtos.UserDTO;
import pt.ul.fc.css.soccernow.business.entities.Player;
import pt.ul.fc.css.soccernow.business.entities.Player.PlayerPosition;
import pt.ul.fc.css.soccernow.business.handlers.RefereeTeamHandler;
import pt.ul.fc.css.soccernow.business.handlers.UserHandler;
import pt.ul.fc.css.soccernow.business.repositories.PlayerRepository;
import pt.ul.fc.css.soccernow.business.repositories.UserRepository;

@ActiveProfiles("test")
@WebMvcTest(UserController.class)
public class UserControllerTests {

  @Autowired private MockMvc mockMvc;

  @MockBean private UserHandler userHandler;

  @MockBean private UserRepository userRepository;

  @MockBean private PlayerRepository playerRepository;

  @MockBean private RefereeTeamHandler refereeTeamHandler;

  private final ObjectMapper objectMapper = new ObjectMapper();

  // post

  @Test
  void testCreatePlayer() throws Exception {
    PlayerDTO dto =
        new PlayerDTO(
            1L,
            "Ana",
            "Morgado",
            PlayerPosition.OTHER,
            new ArrayList<>(),
            new ArrayList<>(),
            new ArrayList<>());
    when(userHandler.createPlayer(any(PlayerDTO.class))).thenReturn(1L);

    mockMvc
        .perform(
            post("/api/players")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isCreated())
        .andExpect(content().string("1"));
  }

  @Test
  void testCreateReferee() throws Exception {
    RefereeDTO dto = new RefereeDTO(1L, "John", "Doe", true, new ArrayList<>());
    when(userHandler.createReferee(any(RefereeDTO.class))).thenReturn(1L);

    mockMvc
        .perform(
            post("/api/referees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isCreated())
        .andExpect(content().string("1"));
  }

  // delete

  @Test
  void testDeletePlayer() throws Exception {
    when(userHandler.deletePlayer(1L)).thenReturn(true);

    mockMvc
        .perform(delete("/api/players/1"))
        .andExpect(status().isOk())
        .andExpect(content().string("true"));
  }

  /** Tests the deletion of a referee by ID. Should return HTTP 200 if the referee is deleted. */
  @Test
  void testDeleteReferee() throws Exception {
    when(userHandler.deleteReferee(1L)).thenReturn(true);

    mockMvc
        .perform(delete("/api/referees/1"))
        .andExpect(status().isOk())
        .andExpect(content().string("true"));
  }

  // put - update*/

  @Test
  void testUpdatePlayer() throws Exception {
    PlayerDTO dto =
        new PlayerDTO(
            1L,
            "John",
            "Doe",
            PlayerPosition.OTHER,
            new ArrayList<>(),
            new ArrayList<>(),
            new ArrayList<>());

    when(userHandler.updatePlayer(any(PlayerDTO.class))).thenReturn(1L);

    mockMvc
        .perform(
            put("/api/players")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isOk())
        .andExpect(content().string("1"));
  }

  /** Tests the update of a referee. */

  // get

  /**
   * Tests the retrieval of a user by ID. Verifies that a valid user is returned with HTTP 200
   * status, and the user's first and last name are correctly present in the JSON response.
   */
  @Test
  void testGetUserById() throws Exception {
    Player player = new Player("John", "Doe", PlayerPosition.GOALKEEPER, null, null);
    UserDTO userDTO = new UserDTO(player.getId(), player.getFirstName(), player.getLastName());

    when(userHandler.getUserById(1L)).thenReturn(Optional.of(userDTO));

    mockMvc
        .perform(get("/api/user/{id}", 1L))
        .andExpect(status().isOk()) // espera o status HTTP 200
        .andExpect(jsonPath("$.firstName").value("John"))
        .andExpect(jsonPath("$.lastName").value("Doe"));
  }

  /**
   * Tests the retrieval of all users. Verifies that the returned list contains the expected users
   * with correct names, and that the HTTP status is 200 (OK).
   */
  @Test
  void testGetAllUsers() throws Exception {
    PlayerDTO player1 =
        new PlayerDTO(1L, "John", "Doe", PlayerPosition.GOALKEEPER, null, null, null);
    PlayerDTO player2 = new PlayerDTO(2L, "Jane", "Doe", PlayerPosition.OTHER, null, null, null);

    List<UserDTO> playersList = List.of(player1, player2);

    when(userHandler.getAllUsers()).thenReturn(playersList);

    mockMvc
        .perform(get("/api/users"))
        .andExpect(status().isOk()) // espera o codigo HTTP 200
        .andExpect(jsonPath("$[0].firstName").value("John"))
        .andExpect(jsonPath("$[1].firstName").value("Jane"));
  }

  @Test
  void testGetAverageGoalsPerGame() throws Exception {
    when(userHandler.getAverageGoalsPerGameByName("John", "Doe")).thenReturn(0.5);

    mockMvc
        .perform(
            get("/api/players/goals-per-game").param("firstName", "John").param("lastName", "Doe"))
        .andExpect(status().isOk())
        .andExpect(content().string("0.5"));
  }

  @Test
  void testGetMostActiveReferee() throws Exception {
    RefereeDTO mostActiveReferee = new RefereeDTO(1L, "John", "Doe", true, new ArrayList<>());
    when(userHandler.getMostActiveReferee()).thenReturn(Optional.of(mostActiveReferee));

    mockMvc
        .perform(get("/api/referees/most-active"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.firstName").value("John"))
        .andExpect(jsonPath("$.lastName").value("Doe"));
  }

  @Test
  void testGetPlayersWithMostRedCards() throws Exception {
    List<PlayerDTO> players =
        List.of(
            new PlayerDTO(
                1L,
                "John",
                "Doe",
                PlayerPosition.OTHER,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()));
    when(userHandler.getPlayersWithMostRedCards()).thenReturn(players);

    mockMvc
        .perform(get("/api/players/most-red-cards"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].firstName").value("John"))
        .andExpect(jsonPath("$[0].lastName").value("Doe"));
  }
}
