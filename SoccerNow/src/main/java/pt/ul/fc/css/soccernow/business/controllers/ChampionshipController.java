package pt.ul.fc.css.soccernow.business.controllers;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import java.net.URI;
import java.util.List;
import java.util.Map;
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
import pt.ul.fc.css.soccernow.business.dtos.ChampionshipByPointsDTO;
import pt.ul.fc.css.soccernow.business.dtos.ChampionshipDTO;
import pt.ul.fc.css.soccernow.business.dtos.ChampionshipRequestDTO;
import pt.ul.fc.css.soccernow.business.dtos.GameRequestDTO;
import pt.ul.fc.css.soccernow.business.dtos.PodiumPositionDTO;
import pt.ul.fc.css.soccernow.business.entities.Championship;
import pt.ul.fc.css.soccernow.business.entities.ChampionshipByPoints;
import pt.ul.fc.css.soccernow.business.entities.PodiumPosition;
import pt.ul.fc.css.soccernow.business.handlers.ChampionshipHandler;
import pt.ul.fc.css.soccernow.business.mappers.ChampionshipMapper;
import pt.ul.fc.css.soccernow.business.mappers.PodiumPositionMapper;

@RestController
@RequestMapping("/api/championships")
@Api(value = "Championship API", tags = "Championship")
public class ChampionshipController {

  private ChampionshipHandler championshipHandler;

  @Autowired
  public ChampionshipController(ChampionshipHandler championshipHandler) {
    this.championshipHandler = championshipHandler;
  }

  @Operation(summary = "Gets all champiomships", description = "Gets all championships")
  @GetMapping
  public ResponseEntity<List<ChampionshipDTO>> getAllChampionships() {
    List<Championship> championships = championshipHandler.getAllChampionships();
    List<ChampionshipDTO> championshipDTOs =
        championships.stream().map(c -> ChampionshipMapper.toDTO(c)).collect(Collectors.toList());
    return ResponseEntity.ok(championshipDTOs);
  }

  @Operation(summary = "Get a championship by id", description = "Gets a championship by id")
  @GetMapping("/id/{id}")
  public ResponseEntity<ChampionshipDTO> getChampionshipById(@PathVariable Long id) {
    Championship championship = championshipHandler.getChampionshipById(id);
    if (championship == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(ChampionshipMapper.toDTO(championship));
  }

  @Operation(summary = "Get championship by name", description = "Gets championships by name")
  @GetMapping("/name/{name}")
  public ResponseEntity<List<ChampionshipDTO>> getChampionshipsByName(@PathVariable String name) {

    List<Championship> championships = championshipHandler.getChampionshipsByName(name);
    if (championships != null) {
      List<ChampionshipDTO> responseDTO =
          championships.stream().map(ChampionshipMapper::toDTO).toList();
      return ResponseEntity.ok(responseDTO);
    }

    return ResponseEntity.notFound().build();
  }

  @Operation(
      summary = "Get championship by number of finished games",
      description = "Gets championships by number of finished games")
  @GetMapping("/finished/number/{number}")
  public ResponseEntity<List<ChampionshipDTO>> getChampionshipsByFinishedGames(
      @PathVariable int number) {

    List<Championship> championships =
        championshipHandler.getChampionshipByNumberOfFinishedGames(number);
    if (championships != null) {
      List<ChampionshipDTO> responseDTO =
          championships.stream().map(ChampionshipMapper::toDTO).toList();
      return ResponseEntity.ok(responseDTO);
    }

    return ResponseEntity.notFound().build();
  }

  @Operation(
      summary = "Get championship by number of unfinished games",
      description = "Gets championships by number of unfinished games")
  @GetMapping("/unfinished/number/{number}")
  public ResponseEntity<List<ChampionshipDTO>> getChampionshipsByNumberOfUnfinishedGames(
      @PathVariable int number) {

    List<Championship> championships =
        championshipHandler.getChampionshipByNumberOfUnfinishedGames(number);
    if (championships != null) {
      List<ChampionshipDTO> responseDTO =
          championships.stream().map(ChampionshipMapper::toDTO).toList();
      return ResponseEntity.ok(responseDTO);
    }

    return ResponseEntity.notFound().build();
  }

  @Operation(
      summary = "Get championship by team id",
      description = "finds championships by team id")
  @GetMapping("team/{teamId}")
  public ResponseEntity<List<ChampionshipDTO>> getChampionshipsByTeamId(@PathVariable Long teamId) {
    List<Championship> championships = championshipHandler.getChampionshipsByTeamId(teamId);
    if (championships != null) {
      List<ChampionshipDTO> responseDTO =
          championships.stream().map(ChampionshipMapper::toDTO).toList();
      return ResponseEntity.ok(responseDTO);
    }
    return ResponseEntity.notFound().build();
  }

  @Operation(
      summary = "Create a new championship by points",
      description = "Creates a new championship by points.")
  @PostMapping
  public ResponseEntity<ChampionshipByPointsDTO> createChampionshipByPoints(
      @RequestBody ChampionshipRequestDTO championshipDTO) {
    ChampionshipByPoints championship =
        championshipHandler.createChampionshipByPoints(championshipDTO);
    return ResponseEntity.created(URI.create("/api/championshipsByPoints/" + championship.getId()))
        .body(new ChampionshipByPointsDTO(championship));
  }

  @Operation(
      summary = "Add a game to a championship",
      description = "Adds a game to a championship")
  @PostMapping("/{championshipId}/games/add")
  public ResponseEntity<?> addGameToChampionship(
      @PathVariable Long championshipId, @RequestBody GameRequestDTO gameDTO) {
    if (championshipHandler.addGameToChampionship(championshipId, gameDTO)) {
      return ResponseEntity.ok().build();
    }
    return ResponseEntity.badRequest().build();
  }

  @Operation(summary = "Add teams to a championship", description = "Adds teams to a championship")
  @PostMapping("/{championshipId}/teams/add")
  public ResponseEntity<?> addTeamsToChampionship(
      @PathVariable Long championshipId, @RequestBody List<Long> teamIds) {
    if (championshipHandler.addTeamsToChampionship(championshipId, teamIds)) {
      return ResponseEntity.ok().build();
    }
    return ResponseEntity.badRequest().build();
  }

  @Operation(
      summary = "Remove a team from a championship",
      description = "Removes a team from a championship")
  @PostMapping("/{championshipId}/teams/{teamId}/remove")
  public ResponseEntity<?> removeTeamFromChampionship(
      @PathVariable Long championshipId, @PathVariable Long teamId) {
    if (championshipHandler.removeTeamFromChampionship(championshipId, teamId)) {
      return ResponseEntity.ok().build();
    }
    return ResponseEntity.badRequest().build();
  }

  @Operation(
      summary = "Remove a game from a championship",
      description = "Removes a game from a championship")
  @PostMapping("/{championshipId}/games/{gameId}/remove")
  public ResponseEntity<?> removeGameFromChampionship(
      @PathVariable Long championshipId, @PathVariable Long gameId) {
    if (championshipHandler.removeGameFromChampionship(championshipId, gameId)) {
      return ResponseEntity.ok().build();
    }
    return ResponseEntity.badRequest().build();
  }

  @Operation(summary = "Delete a championship", description = "Deletes a championship")
  @DeleteMapping("/{championshipId}/delete")
  public ResponseEntity<?> deleteChampionship(@PathVariable Long championshipId) {
    if (championshipHandler.deleteChampionship(championshipId)) {
      return ResponseEntity.ok().build();
    }
    return ResponseEntity.badRequest().build();
  }

  @Operation(summary = "Show championship points", description = "Shows championship points")
  @GetMapping("/{championshipId}/points")
  public ResponseEntity<?> showChampionshipPoints(@PathVariable Long championshipId) {
    Map<Long, Integer> points = championshipHandler.getChampionshipPoints(championshipId);
    if (points == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(points);
  }

  @Operation(
      summary = "Finish a championship",
      description = "Patch a championship to finish it and generate the podium positions")
  @PostMapping("/{championshipId}/finish")
  public ResponseEntity<List<PodiumPositionDTO>> finishChampionship(
      @PathVariable Long championshipId) {
    if (championshipHandler.finishChampionship(championshipId)) {
      List<PodiumPosition> podiumPositions =
          championshipHandler.getChampionshipPodiumPositions(championshipId);
      if (podiumPositions == null) {
        return ResponseEntity.notFound().build();
      }
      return ResponseEntity.ok(
          podiumPositions.stream()
              .map(p -> PodiumPositionMapper.toDTO(p))
              .collect(Collectors.toList()));
    }
    return ResponseEntity.badRequest().build();
  }

  @Operation(summary = "Get a podiumPosition", description = "Get a PodiumPositionDTO from id")
  @GetMapping("/podiumposition/{id}")
  public ResponseEntity<?> getPodiumPositionById(@PathVariable Long id) {
    PodiumPosition podiumPosition = championshipHandler.getPodiumPositionById(id);
    if (podiumPosition == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(PodiumPositionMapper.toDTO(podiumPosition));
  }
}
