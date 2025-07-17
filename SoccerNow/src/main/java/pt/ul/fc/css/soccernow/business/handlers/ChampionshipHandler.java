package pt.ul.fc.css.soccernow.business.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import pt.ul.fc.css.soccernow.business.dtos.ChampionshipRequestDTO;
import pt.ul.fc.css.soccernow.business.dtos.GameRequestDTO;
import pt.ul.fc.css.soccernow.business.entities.Championship;
import pt.ul.fc.css.soccernow.business.entities.ChampionshipByPoints;
import pt.ul.fc.css.soccernow.business.entities.Game;
import pt.ul.fc.css.soccernow.business.entities.Game.GameType;
import pt.ul.fc.css.soccernow.business.entities.PodiumPosition;
import pt.ul.fc.css.soccernow.business.entities.Stats;
import pt.ul.fc.css.soccernow.business.entities.Team;
import pt.ul.fc.css.soccernow.business.repositories.ChampionshipRepository;
import pt.ul.fc.css.soccernow.business.repositories.PodiumPositionRepository;

@Component
public class ChampionshipHandler {

  private final ChampionshipRepository championshipRepository;
  private final PodiumPositionRepository podiumPositionRepository;

  private TeamHandler teamsHandler;
  private GameHandler gameHandler;

  @Autowired
  public ChampionshipHandler(
      ChampionshipRepository championshipRepository,
      PodiumPositionRepository podiumPositionRepository,
      @Lazy TeamHandler teamsHandler,
      @Lazy GameHandler gameHandler) {
    this.teamsHandler = teamsHandler;
    this.gameHandler = gameHandler;
    this.championshipRepository = championshipRepository;
    this.podiumPositionRepository = podiumPositionRepository;
  }

  public ChampionshipByPoints createChampionshipByPoints(ChampionshipRequestDTO championshipDTO) {

    // Create the ChampionshipByPoints object
    ChampionshipByPoints championship =
        new ChampionshipByPoints(championshipDTO.getName(), new ArrayList<>());

    List<Team> teams = new ArrayList<>();
    if (championshipDTO.getTeams() != null) {
      for (Long teamId : championshipDTO.getTeams()) {
        Team team = teamsHandler.getTeamById(teamId);
        teams.add(team);
        team.getChampionships().add(championship); // Associate the championship with the team
      }
      championship.setTeams(teams);
    }

    List<PodiumPosition> podiumPositions = new ArrayList<>();
    championship.setPodiumPositions(podiumPositions);

    List<Game> games = new ArrayList<>();
    championship.setGames(games);

    // Save the championship and associated teams
    championshipRepository.save(championship);
    teams.forEach(teamsHandler::save);

    return championship;
  }

  public boolean addGameToChampionship(Long championshipId, GameRequestDTO gameDTO) {

    Championship championship = championshipRepository.findById(championshipId).orElse(null);

    if (championship == null) {
      return false;
    }

    Game game = gameHandler.createGame(gameDTO, GameType.CHAMPIONSHIP);
    game.setChampionship(championship);

    List<Team> championshipTeams = championship.getTeams();
    List<Team> gameTeams = game.getStartingTeams().stream().map(st -> st.getTeam()).toList();

    for (Team gameTeam : gameTeams) {
      if (!championshipTeams.contains(gameTeam)) {
        return false;
      }
    }

    championship.addGame(game);
    game.setChampionship(championship);

    championshipRepository.save(championship);
    gameHandler.save(game);
    return true;
  }

  public List<Championship> getAllChampionships() {
    return championshipRepository.findByIsActiveTrue();
  }

  public Championship getChampionshipById(Long id) {
    Optional<Championship> optionalChampionship = championshipRepository.findActiveById(id);
    return optionalChampionship.isPresent() ? optionalChampionship.get() : null;
  }

  public boolean addTeamToChampionship(Long championshipId, Long teamId) {
    Championship championship =
        (ChampionshipByPoints) championshipRepository.findActiveById(championshipId).orElse(null);
    if (championship == null) {
      return false;
    }
    Team team = teamsHandler.getTeamById(teamId);
    if (team == null) {
      return false;
    }
    if (championship.getTeams().contains(team)) {
      return false;
    }
    championship.addTeam(team);
    team.getChampionships().add(championship);
    championshipRepository.save(championship);
    teamsHandler.save(team);
    return true;
  }

  public boolean addTeamsToChampionship(Long championshipId, List<Long> teamIds) {
    for (Long teamId : teamIds) {
      if (!addTeamToChampionship(championshipId, teamId)) {
        return false;
      }
    }
    return true;
  }

  public List<Championship> getChampionshipsByTeamId(Long teamId) {
    if (teamsHandler.getTeamById(teamId) != null)
      return championshipRepository.findByTeams_Id(teamId);
    return null;
  }

  public boolean removeTeamFromChampionship(Long championshipId, Long teamId) {
    ChampionshipByPoints championship =
        (ChampionshipByPoints) championshipRepository.findById(championshipId).orElse(null);
    if (championship == null) {
      return false;
    }
    Team team = teamsHandler.getTeamById(teamId);
    if (team == null) {
      return false;
    }
    if (!championship.getTeams().contains(team)) {
      return false;
    }
    if (championship.getGames().stream()
        .anyMatch(
            game ->
                game.getStartingTeams().stream()
                    .anyMatch(startingTeam -> startingTeam.getTeam().equals(team)))) {
      return false;
    }
    championship.getTeams().remove(team);
    team.getChampionships().remove(championship);
    championshipRepository.save(championship);
    teamsHandler.save(team);
    return true;
  }

  public boolean removeGameFromChampionship(Long championshipId, Long gameId) {
    Championship championship =
        (ChampionshipByPoints) championshipRepository.findActiveById(championshipId).orElse(null);

    if (championship == null) {
      return false;
    }

    Game game = gameHandler.getGameById(gameId);
    if (game == null) {
      return false;
    }
    if (!championship.getGames().contains(game)) {
      return false;
    }

    championship.getGames().remove(game);
    game.setChampionship(null);
    championshipRepository.save(championship);
    gameHandler.save(game);
    return true;
  }

  public boolean generatePodiumPositions(Long championshipId) {
    ChampionshipByPoints championship =
        (ChampionshipByPoints) championshipRepository.findActiveById(championshipId).orElse(null);
    if (championship == null) {
      return false;
    }

    Long firstTeamId = championship.getWinner();
    Long secondTeamId = championship.getSecond();
    Long thirdTeamId = championship.getThird();

    Team firstTeam = teamsHandler.getTeamById(firstTeamId);
    Team secondTeam = teamsHandler.getTeamById(secondTeamId);
    Team thirdTeam = teamsHandler.getTeamById(thirdTeamId);

    List<PodiumPosition> podiumPositions = new ArrayList<>();

    Map<Long, Integer> allPoints = getChampionshipPoints(championshipId);

    PodiumPosition firstPodiumPosition =
        new PodiumPosition(
            PodiumPosition.Position.FIRST, firstTeam, championship, allPoints.get(firstTeamId));
    firstPodiumPosition.setChampionship(championship);
    firstPodiumPosition.setTeam(firstTeam);
    firstTeam.getPodiumPositions().add(firstPodiumPosition);
    podiumPositions.add(firstPodiumPosition);

    PodiumPosition secondPodiumPosition =
        new PodiumPosition(
            PodiumPosition.Position.SECOND, secondTeam, championship, allPoints.get(secondTeamId));
    secondPodiumPosition.setChampionship(championship);
    secondPodiumPosition.setTeam(secondTeam);
    secondTeam.getPodiumPositions().add(secondPodiumPosition);
    podiumPositions.add(secondPodiumPosition);

    PodiumPosition thirdPodiumPosition =
        new PodiumPosition(
            PodiumPosition.Position.THIRD, thirdTeam, championship, allPoints.get(thirdTeamId));
    thirdPodiumPosition.setChampionship(championship);
    thirdPodiumPosition.setTeam(thirdTeam);
    thirdTeam.getPodiumPositions().add(thirdPodiumPosition);
    podiumPositions.add(thirdPodiumPosition);

    podiumPositionRepository.save(firstPodiumPosition);
    podiumPositionRepository.save(secondPodiumPosition);
    podiumPositionRepository.save(thirdPodiumPosition);

    teamsHandler.save(firstTeam);
    teamsHandler.save(secondTeam);
    teamsHandler.save(thirdTeam);

    championship.setPodiumPositions(podiumPositions);
    championshipRepository.save(championship);

    return true;
  }

  public boolean deleteChampionship(Long championshipId) {
    ChampionshipByPoints championship =
        (ChampionshipByPoints) championshipRepository.findActiveById(championshipId).orElse(null);
    if (championship == null) {
      return false;
    }

    championship.setActive(false);
    championshipRepository.save(championship);
    return true;
  }

  public boolean addPointsToTeam(Long championshipId, Game game) {
    ChampionshipByPoints championship =
        (ChampionshipByPoints) championshipRepository.findActiveById(championshipId).orElse(null);
    if (championship == null || championship.isActive() == false) {
      return false;
    }

    if (!championship.getGames().contains(game)) {
      return false;
    }

    Stats.GameResult result = game.getStats().getResult();

    if (result == null) {
      return false;
    }

    if (result == Stats.GameResult.HOME_TEAM) {
      championship.addPointsToTeam(game.getStartingTeams().get(0).getTeam().getId(), 3);
      championship.addPointsToTeam(game.getStartingTeams().get(1).getTeam().getId(), 0);
    } else if (result == Stats.GameResult.AWAY_TEAM) {
      championship.addPointsToTeam(game.getStartingTeams().get(0).getTeam().getId(), 0);
      championship.addPointsToTeam(game.getStartingTeams().get(1).getTeam().getId(), 3);
    } else if (result == Stats.GameResult.DRAW) {
      championship.addPointsToTeam(game.getStartingTeams().get(0).getTeam().getId(), 1);
      championship.addPointsToTeam(game.getStartingTeams().get(1).getTeam().getId(), 1);
    }
    championshipRepository.save(championship);
    return true;
  }

  public Map<Long, Integer> getChampionshipPoints(Long championshipId) {
    ChampionshipByPoints championship =
        (ChampionshipByPoints) championshipRepository.findById(championshipId).orElse(null);
    if (championship == null) {
      return null;
    }
    return championship.getPointsByTeam();
  }

  public List<PodiumPosition> getChampionshipPodiumPositions(Long championshipId) {
    Championship championship = championshipRepository.findActiveById(championshipId).orElse(null);
    if (championship == null) {
      return null;
    }
    return championship.getPodiumPositions();
  }

  public PodiumPosition getPodiumPositionById(long id) {
    return podiumPositionRepository.findById(id).orElse(null);
  }

  public boolean finishChampionship(Long championshipId) {
    ChampionshipByPoints championship =
        (ChampionshipByPoints) championshipRepository.findActiveById(championshipId).orElse(null);
    if (championship == null) {
      return false;
    }
    if (championship.getGamesToPlay().isEmpty()) {
      generatePodiumPositions(championshipId);
      championship.setFinished(true);
      championshipRepository.save(championship);
      return true;
    }
    return false;
  }

  public List<Championship> getChampionshipsByName(String name) {
    return championshipRepository.findActiveByName(name);
  }

  public List<Championship> getChampionshipByNumberOfFinishedGames(int number) {
    List<Championship> championships = championshipRepository.findByIsActiveTrue();
    List<Championship> result = new ArrayList<>();

    for (Championship championship : championships) {
      List<Game> games = championship.getGames();
      int numberGames = 0;
      for (Game game : games) {
        numberGames = game.isFinished() ? numberGames + 1 : numberGames;
      }
      if (numberGames == number) {
        result.add(championship);
      }
    }
    return result;
  }

  public List<Championship> getChampionshipByNumberOfUnfinishedGames(int number) {
    List<Championship> championships = championshipRepository.findByIsActiveTrue();
    List<Championship> result = new ArrayList<>();

    for (Championship championship : championships) {
      List<Game> games = championship.getGames();
      int numberGames = 0;
      for (Game game : games) {
        numberGames = !game.isFinished() ? numberGames + 1 : numberGames;
      }
      if (numberGames == number) {
        result.add(championship);
      }
    }
    return result;
  }

  public List<Championship> getFinishedChampionships() {
    return championshipRepository.findAllFinished();
  }

  public List<Championship> getUnfinishedChampionships() {
    return championshipRepository.findAllUnfinished();
  }
}
