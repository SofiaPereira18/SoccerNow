package pt.ul.fc.css.soccernow.business.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pt.ul.fc.css.soccernow.business.dtos.*;
import pt.ul.fc.css.soccernow.business.entities.*;
import pt.ul.fc.css.soccernow.business.entities.Player.PlayerPosition;
import pt.ul.fc.css.soccernow.business.mappers.*;
import pt.ul.fc.css.soccernow.business.repositories.*;

/**
 * Handler for user operations.
 *
 * @author Ana Morgado 56957
 * @author João Azevedo 56913
 * @author Sofia Pereira 56352
 */
@Component
public class UserHandler {

  private final UserRepository userRepository;
  private final PlayerRepository playerRepository;
  private final RefereeRepository refereeRepository;

  private final TeamHandler teamHandler;
  private final GameHandler gameHandler;
  private final RefereeTeamHandler refereeTeamHandler;
  private final StartingTeamHandler startingTeamHandler;

  /**
   * Constructs a new UserHandler with the specified repositories.
   *
   * @param userRepository the user repository to be used for data access
   * @param playerRepository the player repository to be used for data access
   * @param refereeRepository the referee repository to be used for data access
   */
  public UserHandler(
      UserRepository userRepository,
      PlayerRepository playerRepository,
      RefereeRepository refereeRepository,
      @Lazy TeamHandler teamHandler,
      @Lazy StartingTeamHandler startingTeamHandler,
      @Lazy RefereeTeamHandler refereeTeamHandler,
      @Lazy GameHandler gameHandler) {

    this.userRepository = userRepository;
    this.playerRepository = playerRepository;
    this.refereeRepository = refereeRepository;

    this.startingTeamHandler = startingTeamHandler;
    this.refereeTeamHandler = refereeTeamHandler;
    this.teamHandler = teamHandler;
    this.gameHandler = gameHandler;
  }

  /**
   * Create a new player.
   *
   * @param player the player to create
   * @return the registered player's id
   */
  public Long createPlayer(PlayerDTO dto) {
    if (dto == null) {
      throw new IllegalArgumentException("PlayerDTO não pode ser nulo");
    }

    Player player = new Player();
    player.setId(dto.getId());
    player.setFirstName(dto.getFirstName());
    player.setLastName(dto.getLastName());
    player.setIsActive(dto.getIsActive());
    player.setPosition(dto.getPosition());

    if (dto.getTeams() != null && !dto.getTeams().isEmpty()) {
      List<Long> teamsIds = dto.getTeams();
      for (Long teamId : teamsIds) {
        Team team = teamHandler.getTeamById(teamId);
        if (team != null) player.addTeam(team);
      }
    }

    if (dto.getStartingTeams() != null && !dto.getStartingTeams().isEmpty()) {
      List<Long> startingTeamsIds = dto.getStartingTeams();
      for (Long startingTeamId : startingTeamsIds) {
        StartingTeam startingTeam = startingTeamHandler.findById(startingTeamId).get();
        if (startingTeam != null) player.addStartingTeam(startingTeam);
      }
    }

    Player saved = playerRepository.save(player);
    return saved.getId();
  }

  /**
   * Create a new referee.
   *
   * @param referee the referee to create
   * @return the registered referee's id
   */
  public Long createReferee(RefereeDTO dto) {
    if (dto == null) {
      throw new IllegalArgumentException("RefereeDTO não pode ser nulo");
    }

    Referee referee = new Referee();
    referee.setId(dto.getId());
    referee.setFirstName(dto.getFirstName());
    referee.setLastName(dto.getLastName());
    referee.setHasCertificate(dto.isHasCertificate());

    List<RefereeTeam> teams = new ArrayList<>();
    if (dto.getRefereeTeams() != null && !dto.getRefereeTeams().isEmpty()) {
      teams = refereeTeamHandler.findAllById(dto.getRefereeTeams());
      referee.setRefereeTeams(teams);
    }

    Referee saved = refereeRepository.save(referee);
    return saved.getId();
  }

  /**
   * Retrieves detailed information about the user such as the the player position, the matches
   * played, the number of goals, cards, and matches. If referee if has certificate.
   *
   * @param id the ID of the user to retrieve all information
   * @return detailed information about the user
   */
  public UserDetailsDTO verifyUser(Long id) {
    Optional<Player> p = playerRepository.findById(id);
    Optional<Referee> r = refereeRepository.findById(id);

    if (p.isPresent()) {
      Player player = p.get();
      if (player.getIsActive()) {
        List<Team> teams = teamHandler.findTeamsByPlayerId(id);
        List<TeamDTO> teamDTOs =
            teams.stream().map(team -> TeamMapper.toDTO(team)).collect(Collectors.toList());
        List<Card> cards = gameHandler.findCardByPlayer(player);

        double[] results = calculateTotalGamesAndGoals(player);

        return new UserDetailsDTO(
            player.getId(),
            player.getFirstName(),
            player.getLastName(),
            "PLAYER",
            player.getPosition(),
            teamDTOs,
            (int) results[1],
            cards.size(),
            (int) results[0],
            false, //  certificado e para arbitros
            new ArrayList<>() // nao pertence a equipas de arbitros
            );
      }

    } else if (r.isPresent()) {
      Referee referee = r.get();

      if (referee.getIsActive()) {
        boolean hasCertificate = referee.hasCertificate();
        return new UserDetailsDTO(
            referee.getId(),
            referee.getFirstName(),
            referee.getLastName(),
            "REFEREE",
            null, // nao tem posicao
            null, // nao pertence a equipas de jogadores
            0, // nao tem golos
            0, // nao recebe cartoes
            referee.getRefereeTeams().size(),
            hasCertificate,
            new ArrayList<>());
      }
    }

    return null;
  }

  /**
   * Deletes a player.
   *
   * @param id the ID of the player to delete
   * @return true if the player was successfully deleted, false otherwise
   */
  @Transactional
  public boolean deletePlayer(Long id) {
    Optional<Player> opt = playerRepository.findById(id);

    if (opt.isPresent()) {
      Player player = opt.get();
      if (!player.getIsActive()) return false;

      player.setIsActive(false);
      playerRepository.save(player);
      playerRepository.flush();

      return true;
    }
    return false;
  }

  /**
   * Deletes a referee.
   *
   * @param id the ID of the referee to delete
   * @return true if the referee was successfully deleted, false otherwise
   */
  @Transactional
  public boolean deleteReferee(Long refereeId) {
    Optional<Referee> opt = refereeRepository.findById(refereeId);

    if (opt.isPresent()) {
      Referee referee = opt.get();
      if (!referee.getIsActive()) return false;

      referee.setIsActive(false);
      refereeRepository.save(referee);
      refereeRepository.flush();
      return true;
    }

    return false;
  }

  /**
   * Updates an existing player's information.
   *
   * @param player the player to update
   * @return the updated player's id
   */
  @Transactional
  public Long updatePlayer(PlayerDTO dto) {
    if (dto == null) {
      throw new IllegalArgumentException("PlayerDTO não pode ser nulo");
    }

    Player player = getPlayerById(dto.getId());
    if (player == null) {
      throw new IllegalArgumentException("Jogador não encontrado para o id: " + dto.getId());
    }

    player.setFirstName(dto.getFirstName());
    player.setLastName(dto.getLastName());
    player.setPosition(dto.getPosition());
    player.setIsActive(dto.getIsActive());

    if (dto.getTeams() != null && !dto.getTeams().isEmpty()) {
      List<Team> teams = teamHandler.findAllById(dto.getTeams());
      player.setTeams(teams);
    }

    if (dto.getStartingTeams() != null && !dto.getStartingTeams().isEmpty()) {
      List<StartingTeam> startingTeams = startingTeamHandler.findAllById(dto.getStartingTeams());
      player.setStartingTeams(startingTeams);
    }

    Player saved = playerRepository.save(player);
    return saved.getId();
  }

  /**
   * Updates an existing referee's information.
   *
   * @param referee the referee to update
   * @return the updated referee's id
   */
  @Transactional
  public Long updateReferee(RefereeDTO dto) {
    if (dto == null) {
      throw new IllegalArgumentException("RefereeDTO não pode ser nulo");
    }

    Referee referee = getRefereeById(dto.getId());
    referee.setFirstName(dto.getFirstName());
    referee.setLastName(dto.getLastName());
    referee.setHasCertificate(dto.isHasCertificate());

    List<RefereeTeam> teams = new ArrayList<>();
    if (dto.getRefereeTeams() != null && !dto.getRefereeTeams().isEmpty()) {
      teams = refereeTeamHandler.findAllById(dto.getRefereeTeams());
      referee.setRefereeTeams(teams);
    }

    Referee saved = refereeRepository.save(referee);
    return saved.getId();
  }

  /**
   * Retrieves a user by their ID.
   *
   * @param id the ID of the user to retrieve
   * @return an Optional containing the user DTO if found, or an empty Optional if the user doesn't
   *     exist
   */
  public Optional<UserDTO> getUserById(Long id) {
    return userRepository
        .findById(id)
        .map(user -> new UserDTO(user.getId(), user.getFirstName(), user.getLastName()));
  }

  /**
   * Retrieves a player by their ID.
   *
   * @param id the ID of the player to retrieve
   * @return Optional containing the player DTO if found, or empty Optional otherwise
   */
  public Player getPlayerById(Long id) {
    Optional<Player> player = playerRepository.findById(id);
    if (player.isPresent()) {
      return player.get();
    }
    return null;
  }

  /**
   * Retrieves a referee by their ID.
   *
   * @param id the ID of the referee to retrieve
   * @return Optional containing the referee DTO if found, or empty Optional otherwise
   */
  public Referee getRefereeById(Long id) {
    Optional<Referee> referee = refereeRepository.findById(id);
    if (referee.isPresent()) {
      return referee.get();
    }
    return null;
  }

  /**
   * Retrieves a list of all registered users.
   *
   * @return list of all user DTOs
   */
  public List<UserDTO> getAllUsers() {
    List<UserDTO> users = new ArrayList<>();

    users.addAll(
        playerRepository.findAll().stream()
            .filter(Player::getIsActive)
            .map(PlayerMapper::toDTO)
            .collect(Collectors.toList()));

    users.addAll(
        refereeRepository.findAll().stream()
            .filter(Referee::getIsActive)
            .map(RefereeMapper::toDTO)
            .collect(Collectors.toList()));

    return users;
  }

  /**
   * Retrieves a list of all registered players.
   *
   * @return list of all player DTOs containing player information
   */
  public List<PlayerDTO> getAllPlayers() {
    return playerRepository.findAll().stream()
        .filter(Player::getIsActive)
        .map(PlayerMapper::toDTO)
        .collect(Collectors.toList());
  }

  public List<Player> getAllPlayersEntity() {
    return playerRepository.findAll().stream()
        .filter(Player::getIsActive)
        .collect(Collectors.toList());
  }

  /**
   * Retrieves a list of all registered referees.
   *
   * @return list of all referee DTOs containing referee information
   */
  public List<RefereeDTO> getAllReferees() {
    return refereeRepository.findByIsActiveTrue().stream()
        .filter(Referee::getIsActive)
        .map(RefereeMapper::toDTO)
        .collect(Collectors.toList());
  }

  public List<Referee> getAllRefs() {
    return refereeRepository.findByIsActiveTrue().stream()
        .filter(Referee::getIsActive)
        .collect(Collectors.toList());
  }

  /**
   * Retrieves a list of all registered players with the given ids.
   *
   * @return list of all players with the given ids
   */
  public List<Player> findAllPlayersById(List<Long> ids) {
    return playerRepository.findAllById(ids);
  }

  /**
   * Retrieves a list of all registered referees with the given ids.
   *
   * @return list of all referees with the given ids
   */
  public List<Referee> findAllRefereesById(List<Long> ids) {
    return refereeRepository.findAllById(ids);
  }

  /**
   * Gets the average of how many goals the player named X scores per game.
   *
   * @param firstName the first name of the player to search for
   * @param lastName the last name of the player to search for
   * @return the average of how many goals the player with name X scores per game.
   */
  public double getAverageGoalsPerGameByName(String firstName, String lastName) {
    Player player = playerRepository.findByFirstNameAndLastName(firstName, lastName);
    if (player == null) return 0.0;

    double[] results = calculateTotalGamesAndGoals(player);
    if (results[1] == 0) return 0.0;

    return results[1] / results[0];
  }

  /**
   * Gets the referee that has officiated the most games.
   *
   * @return the referee that has officiated the most games
   */
  public Optional<RefereeDTO> getMostActiveReferee() {
    List<Referee> referees = refereeRepository.findAll();

    if (referees.isEmpty()) return Optional.empty();

    RefereeDTO maxRef = RefereeMapper.toDTO(referees.get(0));
    int maxGames = 1;

    for (Referee referee : referees) {
      int numberGames = calculateRefereeGames(referee);

      if (maxGames < numberGames) {
        maxGames = numberGames;
        maxRef = RefereeMapper.toDTO(referee);
      }
    }

    return Optional.of(maxRef);
  }

  /**
   * Gets which players received the most red cards.
   *
   * @return top 5 players who received the most red cards.
   */
  public List<PlayerDTO> getPlayersWithMostRedCards() {
    List<Player> players = playerRepository.findAll();
    Map<Player, Integer> redCardCount = new HashMap<>();

    for (Player player : players) {
      List<Card> cards = gameHandler.findCardByPlayer(player);
      int redCards = 0;

      for (Card card : cards) {
        if (card.getType().toString().equals("RED")) {
          redCards += 1;
        }
      }

      if (redCards > 0) {
        redCardCount.put(player, redCards);
      }
    }

    // ordenar os top 5 jogadores com mais vermelhos
    List<Player> sortedPlayers = new ArrayList<>(redCardCount.keySet());
    sortedPlayers.sort(
        (p1, p2) -> redCardCount.get(p2) - redCardCount.get(p1)); // ordem decrescente

    List<PlayerDTO> topPlayers = new ArrayList<>();
    for (int i = 0; i < sortedPlayers.size() && i < 5; i++) {
      Player player = sortedPlayers.get(i);
      topPlayers.add(PlayerMapper.toDTO(player));
    }

    return topPlayers;
  }

  /**
   * Used to calculate the number of games and goals the given player has scored or played.
   *
   * @param player the player to search information about
   * @return the amount of games and goals
   */
  public double[] calculateTotalGamesAndGoals(Player player) {
    double totalGames = player.getStartingTeams().size();
    double totalGoals = 0;

    for (Stats stats : gameHandler.getAllStats()) {

      // nao sei se jogador joga fora ou em casa
      Integer goalsHome = stats.getGoalsHomeTeam().get(player);
      Integer goalsAway = stats.getGoalsAwayTeam().get(player);

      if (goalsHome != null) totalGoals += goalsHome;

      if (goalsAway != null) totalGoals += goalsAway;
    }

    return new double[] {totalGames, totalGoals};
  }

  /**
   * Calculates the number of cards the player has received.
   *
   * @param player the player to calculate the number of cards
   * @return the number of cards the player received
   */
  public int calculateNumberCards(Player player) {
    List<Card> cards = gameHandler.findCardByPlayer(player);
    int totalCards = 0;

    for (Card card : cards) {
      if (card.getPlayer().equals(player)) totalCards += 1;
    }

    return totalCards;
  }

  /**
   * Calculates how many cards the given referee gave during all games.
   *
   * @referee the referee to calculate how many cards has given
   * @return the number of cards the referee gave
   */
  public int calculateGivenCards(Referee referee) {
    List<Game> games = gameHandler.getAllGames();
    int givenCards = 0;

    for (Game game : games) {
      // se o main ref for o referee dado como argumento
      if (game.getRefereeTeam().getMainReferee().equals(referee)) {
        if (game.getStats() != null) {
          Stats stats = game.getStats();
          if (stats.getCards() != null) {
            List<Card> cards = stats.getCards();
            givenCards += cards.size();
          }
        }
      }
    }

    return givenCards;
  }

  /**
   * Calculates the number of games a referee was in.
   *
   * @param referee the referee to count the number of games was in
   * @return the number of games a referee was in
   */
  public int calculateRefereeGames(Referee referee) {
    int numberGames = 0;

    List<RefereeTeam> teams = referee.getRefereeTeams();
    for (RefereeTeam team : teams) {
      List<Game> games = team.getGames();
      numberGames += games.size();
    }

    return numberGames;
  }

  public void save(Player player) {
    playerRepository.save(player);
  }
  
}