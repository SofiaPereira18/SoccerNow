package pt.ul.fc.css.soccernow.business.controllers;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pt.ul.fc.css.soccernow.business.dtos.PlayerDTO;
import pt.ul.fc.css.soccernow.business.dtos.RefereeDTO;
import pt.ul.fc.css.soccernow.business.dtos.RefereeTeamDTO;
import pt.ul.fc.css.soccernow.business.dtos.UserDTO;
import pt.ul.fc.css.soccernow.business.dtos.UserDetailsDTO;
import pt.ul.fc.css.soccernow.business.entities.Player;
import pt.ul.fc.css.soccernow.business.entities.Referee;
import pt.ul.fc.css.soccernow.business.entities.RefereeTeam;
import pt.ul.fc.css.soccernow.business.handlers.RefereeTeamHandler;
import pt.ul.fc.css.soccernow.business.handlers.UserHandler;
import pt.ul.fc.css.soccernow.business.mappers.PlayerMapper;
import pt.ul.fc.css.soccernow.business.mappers.RefereeMapper;
import pt.ul.fc.css.soccernow.business.mappers.RefereeTeamMapper;

/**
 * REST controller responsible for handling all HTTP requests.
 *
 * @author Ana Morgado 56957
 * @author João Azevedo 56913
 * @author Sofia Pereira 56352
 */
@RestController
@RequestMapping("/api")
@Api(value = "User API", tags = "Users")
public class UserController {

  private final UserHandler userHandler;
  private final RefereeTeamHandler refereeTeamHandler;

  /**
   * Constructs a new UserController with the given UserHandler.
   *
   * @param userHandler the handler that manages user operations
   */
  public UserController(UserHandler userHandler, RefereeTeamHandler refereeTeamHandler) {
    this.userHandler = userHandler;
    this.refereeTeamHandler = refereeTeamHandler;
  }

  // create

  /**
   * Endpoint for registering a new player.
   *
   * @param player the player to save
   * @return the saved player's id
   */
  @Operation(
      summary = "Create a new player",
      description =
          "Creates a new player account with the provided information. The player position can be"
              + " GOALKEEPER or OTHER.")
  @PostMapping("/players")
  public ResponseEntity<Long> createPlayer(@RequestBody PlayerDTO dto) {
    Long id = userHandler.createPlayer(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(id);
  }

  /**
   * Endpoint for registering a new referee.
   *
   * @param referee the referee to save
   * @return the saved referee's id
   */
  @Operation(
      summary = "Create a new referee",
      description = "Creates a new referee account with the provided information.")
  @PostMapping("/referees")
  public ResponseEntity<Long> createReferee(@RequestBody RefereeDTO dto) {
    if (dto.getRefereeTeams() == null) {
      return ResponseEntity.badRequest().build();
    }

    Long refereeId = userHandler.createReferee(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(refereeId);
  }

  /**
   * Endpoint for registering a new referee team.
   *
   * @param referee the referee team to save
   * @return the saved referee team's id
   */
  @Operation(
      summary = "Create a new referee team",
      description = "Creates a new referee team account with the provided information.")
  @PostMapping("/refereeTeams")
  public ResponseEntity<Long> createRefereeTeam(@RequestBody RefereeTeamDTO dto) {
    Long refereeTeamId = refereeTeamHandler.createRefereeTeam(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(refereeTeamId);
  }

  /**
   * Endpoint for getting information about user.
   *
   * @param id the ID of the user to verify
   * @return ResponseEntity with information about the user or a not active message
   */
  @Operation(
      summary = "Checks every information of user.",
      description = "Checks every information of user.")
  @GetMapping("/users/{id}/check")
  public ResponseEntity<?> verifyUser(@PathVariable Long id) {
    UserDetailsDTO userDetails = userHandler.verifyUser(id);
    if (userDetails != null) {
      return ResponseEntity.ok(userDetails);
    } else {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User is not active.");
    }
  }

  // delete

  /**
   * Endpoint for deleting a player.
   *
   * @param id the ID of the player to delete
   * @return ResponseEntity with success message (HTTP 200) if deletion succeeds, or not found
   *     message (HTTP 404) if player doesn't exist
   */
  @Operation(
      summary = "Deletes a player",
      description = "Removes the player with the specified ID from the system.")
  @DeleteMapping("/players/{id}")
  public ResponseEntity<Boolean> deletePlayer(@PathVariable Long id) {
    if (userHandler.deletePlayer(id)) {
      return ResponseEntity.ok(true);
    } else {
      return ResponseEntity.status(404).body(false);
    }
  }

  /**
   * Endpoint for deleting a referee.
   *
   * @param id the ID of the referee to delete
   * @return ResponseEntity with success message (HTTP 200) if deletion succeeds, or not found
   *     message (HTTP 404) if referee doesn't exist
   */
  @Operation(
      summary = "Deletes a referee",
      description = "Removes the referee with the specified ID from the system.")
  @DeleteMapping("/referees/{id}")
  public ResponseEntity<Boolean> deleteReferee(@PathVariable Long id) {
    if (userHandler.deleteReferee(id)) {
      return ResponseEntity.ok(true);
    } else {
      return ResponseEntity.status(404).body(false);
    }
  }

  /**
   * Endpoint for deleting a refereeTeam.
   *
   * @param id the ID of the referee to delete
   * @return ResponseEntity with success message (HTTP 200) if deletion succeeds, or not found
   *     message (HTTP 404) if referee doesn't exist
   */
  @Operation(
      summary = "Deletes a refereeTeam",
      description = "Removes the refereeTeam with the specified ID from the system.")
  @DeleteMapping("/refereeTeams/{id}")
  public ResponseEntity<String> deleteRefereeTeam(@PathVariable Long id) {
    if (refereeTeamHandler.deleteRefereeTeam(id)) {
      return ResponseEntity.ok("RefereeTeam deleted successfully.");
    } else {
      return ResponseEntity.status(404).body("RefereeTeam not found or not deleted.");
    }
  }

  // update

  /**
   * Updates an existing player's information.
   *
   * @param player the player to update
   * @return the updated player's ID
   */
  @Operation(
      summary = "Update player",
      description = "Updates an existing player. Returns the ID of the updated player.")
  @PutMapping("/players")
  public ResponseEntity<Long> updatePlayer(@RequestBody PlayerDTO dto) {
    return ResponseEntity.ok(userHandler.updatePlayer(dto));
  }

  /**
   * Updates an existing referee's information.
   *
   * @param referee the referee to update
   * @return the updated referee's ID
   */
  @Operation(
      summary = "Update referee",
      description = "Updates an existing referee. Returns the ID of the updated referee.")
  @PutMapping("/referees")
  public ResponseEntity<Long> updateReferee(@RequestBody RefereeDTO dto) {
    return ResponseEntity.ok(userHandler.updateReferee(dto));
  }

  /**
   * Updates an existing refereeTeam's information.
   *
   * @param refereeTeam the referee to update
   * @return the updated refereeTeam's ID
   */
  @Operation(
      summary = "Update refereeTeam",
      description = "Updates an existing refereeTeam. Returns the ID of the updated refereeTeam.")
  @PutMapping("/refereeTeams")
  public ResponseEntity<Long> updateRefereeTeam(@RequestBody RefereeTeamDTO dto) {
    return ResponseEntity.ok(refereeTeamHandler.updateRefereeTeam(dto));
  }

  // get

  /**
   * Endpoint for retrieving a user by ID.
   *
   * @param id the ID of the user to retrieve
   * @return ResponseEntity containing the user DTO (HTTP 200) if found, or HTTP 404 if user is not
   *     found
   */
  @Operation(
      summary = "Gets a user by ID",
      description = "Retrieves the user with the specified ID.")
  @GetMapping("/user/{id}")
  public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
    return userHandler
        .getUserById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.status(404).build());
  }

  /**
   * Endpoint for retrieving a player by ID.
   *
   * @param id the ID of the player to retrieve
   * @return ResponseEntity containing the player DTO (HTTP 200) if found and is a player, or HTTP
   *     404 if not found or not a player
   */
  @Operation(
      summary = "Gets a player by ID",
      description = "Retrieves the player with the specified ID.")
  @GetMapping("/player/{id}")
  public ResponseEntity<PlayerDTO> getPlayerById(@PathVariable Long id) {
    try {
      Player player = userHandler.getPlayerById(id);
      return ResponseEntity.ok(PlayerMapper.toDTO(player));
    } catch (EntityNotFoundException ex) {
      return ResponseEntity.status(404).build();
    }
  }

  /**
   * Endpoint for retrieving a referee by ID.
   *
   * @param id the ID of the referee to retrieve
   * @return ResponseEntity containing the referee DTO (HTTP 200) if found and is a referee, or HTTP
   *     404 if not found or not a referee
   */
  @Operation(
      summary = "Gets a referee by ID",
      description = "Retrieves the referee with the specified ID.")
  @GetMapping("/referee/{id}")
  public ResponseEntity<RefereeDTO> getRefereeById(@PathVariable Long id) {
    try {
      Referee referee = userHandler.getRefereeById(id);
      return ResponseEntity.ok(RefereeMapper.toDTO(referee));
    } catch (EntityNotFoundException ex) {
      return ResponseEntity.status(404).build();
    }
  }

  /**
   * Endpoint for retrieving a referee team by ID.
   *
   * @param id the ID of the refereeTeam to retrieve
   * @return ResponseEntity containing the refereeTeam DTO (HTTP 200) if found and is a refereeTeam,
   *     or HTTP 404 if not found or not a referee
   */
  @Operation(
      summary = "Gets a referee team by ID",
      description = "Retrieves the referee team with the specified ID.")
  @GetMapping("/refereeTeam/{id}")
  public ResponseEntity<RefereeTeamDTO> getRefereeTeamById(@PathVariable Long id) {
    try {
      RefereeTeam refTeam = refereeTeamHandler.getRefereeTeamById(id);
      return ResponseEntity.ok(RefereeTeamMapper.toDTO(refTeam));
    } catch (EntityNotFoundException ex) {
      return ResponseEntity.status(404).build();
    }
  }

  /**
   * Endpoint for retrieving all users.
   *
   * @return List of all user DTOs in the system
   */
  @Operation(
      summary = "Gets all users",
      description = "Retrieves a list of all registered users in the system.")
  @GetMapping("/users")
  public List<UserDTO> getAllUsers() {
    return userHandler.getAllUsers();
  }

  /**
   * Endpoint for retrieving all players.
   *
   * @return List of all player DTOs in the system
   */
  @Operation(
      summary = "Gets all players",
      description = "Retrieves a list of all registered players in the system.")
  @GetMapping("/players")
  public List<PlayerDTO> getAllPlayers() {
    return userHandler.getAllPlayers();
  }

  /**
   * Endpoint for retrieving all referees.
   *
   * @return List of all referee DTOs in the system
   */
  @Operation(
      summary = "Gets all referees",
      description = "Retrieves a list of all registered referees in the system.")
  @GetMapping("/referees")
  public List<RefereeDTO> getAllReferees() {
    return userHandler.getAllReferees();
  }

  /**
   * Endpoint for retrieving all referee teams.
   *
   * @return List of all referee team DTOs in the system
   */
  @Operation(
      summary = "Gets all referee teams",
      description =
          "Retrieves a list of all registered referee teams in the system, including their referees"
              + " and associated games.")
  @GetMapping("/referee-teams")
  public List<RefereeTeamDTO> getAllRefereeTeams() {
    return refereeTeamHandler.getAllRefereeTeams();
  }

  /**
   * Retrieves the average number of goals per game for players with a given name.
   *
   * @param name the first or last name of the players to include in the average
   * @return the average goals per game for players matching the name
   */
  @Operation(
      summary = "Get average goals per game by players name",
      description =
          "Returns the average number of goals per game for all players whose first or last name"
              + " matches the given name.")
  @GetMapping("/players/goals-per-game")
  public double getAverageGoalsPerGameByName(
      @RequestParam String firstName, @RequestParam String lastName) {
    return userHandler.getAverageGoalsPerGameByName(firstName, lastName);
  }

  /**
   * Retrieves the referee who officiated the most matches.
   *
   * @return an Optional containing the referee with the most matches, or empty if no referees exist
   */
  @Operation(
      summary = "Get most active referee",
      description = "Returns the referee who has officiated the highest number of matches.")
  @GetMapping("/referees/most-active")
  public Optional<RefereeDTO> getMostActiveReferee() {
    return userHandler.getMostActiveReferee();
  }

  /**
   * Retrieves a list of players with the most red cards.
   *
   * @return a list of PlayerDTOs representing the top 5 players with the most red cards
   */
  @Operation(
      summary = "Get players with most red cards",
      description = "Returns a list of players who have received the highest number of red cards.")
  @GetMapping("/players/most-red-cards")
  public List<PlayerDTO> getPlayersWithMostRedCards() {
    return userHandler.getPlayersWithMostRedCards();
  }
}
