package pt.ul.fc.css.soccernow.business.controllers;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.ul.fc.css.soccernow.business.dtos.GameDTO;
import pt.ul.fc.css.soccernow.business.dtos.PlayerDTO;
import pt.ul.fc.css.soccernow.business.dtos.StartingTeamDTO;
import pt.ul.fc.css.soccernow.business.dtos.StartingTeamRequestDTO;
import pt.ul.fc.css.soccernow.business.dtos.TeamDTO;
import pt.ul.fc.css.soccernow.business.entities.Game;
import pt.ul.fc.css.soccernow.business.entities.Player;
import pt.ul.fc.css.soccernow.business.entities.StartingTeam;
import pt.ul.fc.css.soccernow.business.entities.Team;
import pt.ul.fc.css.soccernow.business.handlers.TeamHandler;
import pt.ul.fc.css.soccernow.business.mappers.GameMapper;
import pt.ul.fc.css.soccernow.business.mappers.PlayerMapper;
import pt.ul.fc.css.soccernow.business.mappers.StartingTeamMapper;
import pt.ul.fc.css.soccernow.business.mappers.TeamMapper;

@RestController
@RequestMapping("/api/team")
@Api(value = "Team API", tags = "Teams")
public class TeamController {

  @Autowired private TeamHandler tHandler;

  @Operation(
      summary = "Gets all games of a team.",
      description = "Return a list of GamesDTO using teamId.")
  @GetMapping("/{teamId}/games")
  public ResponseEntity<List<GameDTO>> getGames(@PathVariable("teamId") Long team_id) {
    List<Game> games = tHandler.getAllGames(team_id);
    if (games != null) {
      List<GameDTO> dtos =
          games.stream().map(g -> GameMapper.toDTO(g)).collect(Collectors.toList());
      return ResponseEntity.ok(dtos);
    }
    return ResponseEntity.notFound().build();
  }

  @Operation(
      summary = "Gets all finished games of a team teams.",
      description = "Return a list of GameDTO using teamId.")
  @GetMapping("/{teamId}/games/finished")
  public ResponseEntity<List<GameDTO>> getFinishedGames(@PathVariable("teamId") Long team_id) {
    List<Game> games = tHandler.getAllFinishedGames(team_id);
    if (games != null) {
      List<GameDTO> dtos =
          games.stream().map(g -> GameMapper.toDTO(g)).collect(Collectors.toList());
      return ResponseEntity.ok(dtos);
    }
    return ResponseEntity.notFound().build();
  }

  @Operation(summary = "Gets all teams", description = "Return all existing teamsDTO.")
  @GetMapping
  public ResponseEntity<List<TeamDTO>> getAllTeam() {
    List<Team> team = tHandler.getAllTeams();
    if (team != null) {
      List<TeamDTO> dtos = team.stream().map(t -> TeamMapper.toDTO(t)).collect(Collectors.toList());
      return ResponseEntity.ok(dtos);
    }
    return ResponseEntity.notFound().build();
  }

  @Operation(
      summary = "Create a new Team",
      description = "Create a empty team with a name and return its DTO.")
  @PostMapping("/name/{name}")
  public ResponseEntity<TeamDTO> registerTeam(@PathVariable("name") String name) {
    System.out.println("Received TeamDTO: " + name);
    Team teamCreated = tHandler.registerTeam(name);
    if (teamCreated != null) {
      URI location = URI.create("/api/team/" + teamCreated.getName());
      return ResponseEntity.created(location).body(TeamMapper.toDTO(teamCreated));
    }
    return ResponseEntity.badRequest().build();
  }

  @Operation(
      summary = "Gets all StartingTeams of a team",
      description = "Return a list of StartingTeamDTO.")
  @GetMapping("/{teamId}/starting-team")
  public ResponseEntity<List<StartingTeamDTO>> getStartingTeam(
      @PathVariable("teamId") Long team_id) {
    List<StartingTeam> stList = tHandler.getStartingTeam(team_id);
    if (stList != null) {
      List<StartingTeamDTO> dtos =
          stList.stream().map(st -> StartingTeamMapper.toDTO(st)).collect(Collectors.toList());
      return ResponseEntity.ok(dtos);
    }
    return ResponseEntity.notFound().build();
  }

  @Operation(summary = "Gets teams by name", description = "Return TeamDTO of team with name.")
  @GetMapping("/name/{name}")
  public ResponseEntity<TeamDTO> getTeamByName(@PathVariable("name") String name) {
    Team t = tHandler.getTeamByName(name);
    if (t != null) {
      TeamDTO dto = TeamMapper.toDTO(t);
      return ResponseEntity.ok(dto);
    }
    return ResponseEntity.notFound().build();
  }

  @Operation(summary = "Add player to a team", description = "Return TeamDTO updated.")
  @PostMapping("/{teamId}/{playerId}")
  public ResponseEntity<TeamDTO> addPlayer(
      @PathVariable("teamId") Long teamId, @PathVariable("playerId") Long playerId) {
    System.out.println("Adding player with ID: " + playerId + " to team with ID: " + teamId);
    if (teamId == null || playerId == null) {
      return ResponseEntity.badRequest().build();
    }
    Team t = tHandler.addPlayer(teamId, playerId);
    if (t != null) {
      TeamDTO dto = TeamMapper.toDTO(t);
      return ResponseEntity.ok(dto);
    }
    return ResponseEntity.notFound().build();
  }

  @Operation(summary = "Gets all players of a teams", description = "Return a list of PlayerDTO.")
  @GetMapping("/{teamId}/players")
  public ResponseEntity<List<PlayerDTO>> getPlayers(@PathVariable("teamId") Long teamId) {
    List<Player> players = tHandler.getPlayers(teamId);

    if (players != null) {
      List<PlayerDTO> dtos =
          players.stream().map(player -> PlayerMapper.toDTO(player)).collect(Collectors.toList());
      return ResponseEntity.ok(dtos);
    }
    return ResponseEntity.notFound().build();
  }

  @Operation(summary = "Gets team by id", description = "Return TeamDTO.")
  @GetMapping("/id/{id}")
  public ResponseEntity<TeamDTO> getTeamById(@PathVariable("id") Long id) {
    Team t = tHandler.getTeamById(id);
    if (t != null) {
      TeamDTO dto = TeamMapper.toDTO(t);
      return ResponseEntity.ok(dto);
    }
    return ResponseEntity.notFound().build();
  }

  @Operation(summary = "Delete team by id", description = "Delete team if it doesn't have games.")
  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteTeam(@PathVariable("id") Long id) {
    boolean deleted = tHandler.removeTeam(id);
    if (deleted) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.notFound().build();
  }

  @Operation(summary = "Remove player of the team.", description = "Remove player of a team.")
  @DeleteMapping("/{teamId}/player/{playerId}")
  public ResponseEntity<Boolean> removePlayer(
      @PathVariable("teamId") Long teamId, @PathVariable("playerId") Long playerId) {
    boolean removed = tHandler.removePlayer(teamId, playerId);
    if (removed) {
      return ResponseEntity.ok(true);
    }
    return ResponseEntity.status(404).body(false);
  }

  @Operation(
      summary = "Removes StartingTeam of a team",
      description = "Remove StartingTeam if it doesn't have games.")
  @DeleteMapping("/{teamId}/StartingTeam/{StartingteamId}")
  public ResponseEntity<Boolean> removeStartingTeam(
      @PathVariable("teamId") Long teamId, @PathVariable("StartingteamId") Long stId) {
    boolean removed = tHandler.removeStartingTeam(teamId, stId);
    if (removed) {
      return ResponseEntity.ok(true);
    }
    return ResponseEntity.status(404).body(false);
  }

  @Operation(
      summary = "Creates a StartingTeam with player.",
      description = "Return the StartingTeam created.")
  @PostMapping("/{teamId}/starting-team/players")
  public ResponseEntity<StartingTeamDTO> createStartingTeamWithPlayer(
      @PathVariable("teamId") Long teamId, @RequestBody StartingTeamRequestDTO stRDTO) {
    if ((stRDTO.getPlayers() != null && stRDTO.getPlayers().size() == 4)
        && stRDTO.getGoalKeeper() != null) {
      StartingTeam st =
          tHandler.createStartingTeamWithPlayer(
              teamId, stRDTO.getPlayers(), stRDTO.getGoalKeeper());
      if (st != null) {
        return ResponseEntity.ok(StartingTeamMapper.toDTO(st));
      }
    }

    return ResponseEntity.notFound().build();
  }

  @Operation(summary = "Gets all StartingTeams", description = "Return a list of StartingTeamDTO.")
  @GetMapping("/starting-team")
  public ResponseEntity<List<StartingTeamDTO>> getAllStartingTeams() {
    List<StartingTeam> stList = tHandler.getAllStartingTeams();
    if (stList != null) {
      List<StartingTeamDTO> dtos =
          stList.stream().map(st -> StartingTeamMapper.toDTO(st)).collect(Collectors.toList());
      return ResponseEntity.ok(dtos);
    }
    return ResponseEntity.notFound().build();
  }

  @Operation(
      summary = "Gets StartingTeam by id",
      description = "Return StartingTeamDTO of the team with id.")
  @GetMapping("/starting-team/id/{id}")
  public ResponseEntity<StartingTeamDTO> getStartingTeamById(@PathVariable("id") Long id) {
    StartingTeam st = tHandler.getStartingTeamById(id);
    if (st != null) {
      StartingTeamDTO dto = StartingTeamMapper.toDTO(st);
      return ResponseEntity.ok(dto);
    }
    return ResponseEntity.notFound().build();
  }
}
