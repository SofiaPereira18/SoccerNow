package pt.ul.fc.css.soccernow.business.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pt.ul.fc.css.soccernow.business.dtos.RefereeTeamDTO;
import pt.ul.fc.css.soccernow.business.entities.Game;
import pt.ul.fc.css.soccernow.business.entities.Referee;
import pt.ul.fc.css.soccernow.business.entities.RefereeTeam;
import pt.ul.fc.css.soccernow.business.mappers.RefereeTeamMapper;
import pt.ul.fc.css.soccernow.business.repositories.RefereeTeamRepository;

@Component
public class RefereeTeamHandler {

  private final RefereeTeamRepository refereeTeamRepository;
  private final GameHandler gameHandler;
  private final UserHandler userHandler;

  /**
   * Constructs a new RefereeTeamHandler with the specified repositories.
   *
   * @param refereeTeamRepository the referee team repository to be used for data access
   */
  public RefereeTeamHandler(
      RefereeTeamRepository refereeTeamRepository,
      @Lazy GameHandler gameHandler,
      @Lazy UserHandler userHandler) {
    this.refereeTeamRepository = refereeTeamRepository;
    this.gameHandler = gameHandler;
    this.userHandler = userHandler;
  }

  /**
   * Retrieves a refereeTeam by their ID.
   *
   * @param id the ID of the refereeTeam to retrieve
   * @return Optional containing the refereeTeam DTO if found, or empty Optional otherwise
   */
  public RefereeTeam getRefereeTeamById(Long id) {
    Optional<RefereeTeam> rt = refereeTeamRepository.findById(id);
    if (rt.isPresent()) {
      return rt.get();
    }
    return null;
  }

  /**
   * Create a new referee team.
   *
   * @param referee the referee team to create
   * @return the registered referee team's id
   */
  @Transactional
  public Long createRefereeTeam(RefereeTeamDTO dto) {
    if (dto == null) {
      throw new IllegalArgumentException("RefereeTeamDTO não pode ser nulo");
    }

    RefereeTeam refTeam = new RefereeTeam();
    refTeam.setId(dto.getId());
    refTeam.setIsChampionshipGame(dto.isChampionshipGame());

    Referee mainRef = userHandler.getRefereeById(dto.getId());
    refTeam.setMainReferee(mainRef);

    List<Referee> referees = new ArrayList<>();
    if (dto.getReferees() != null && !dto.getReferees().isEmpty()) {
      referees = userHandler.findAllRefereesById(dto.getReferees());
      refTeam.setReferees(referees);
    }

    List<Game> games = new ArrayList<>();
    if (dto.getGames() != null && !dto.getGames().isEmpty()) {
      games = gameHandler.findAllGamesById(dto.getGames());
      refTeam.setGames(games);
    }

    RefereeTeam saved = refereeTeamRepository.save(refTeam);
    return saved.getId();
  }

  /**
   * Updates an existing referee team's information.
   *
   * @param referee the referee team to update
   * @return the updated referee team's id
   */
  @Transactional
  public Long updateRefereeTeam(RefereeTeamDTO dto) {
    if (dto == null) {
      throw new IllegalArgumentException("RefereeTeamDTO não pode ser nulo");
    }

    RefereeTeam refTeam = new RefereeTeam();
    refTeam.setId(dto.getId());
    refTeam.setIsChampionshipGame(dto.isChampionshipGame());

    Referee mainRef = userHandler.getRefereeById(dto.getMainReferee());
    refTeam.setMainReferee(mainRef);

    List<Referee> referees = new ArrayList<>();
    if (dto.getReferees() != null && !dto.getReferees().isEmpty()) {
      referees = userHandler.findAllRefereesById(dto.getReferees());
      refTeam.setReferees(referees);
    }

    List<Game> games = new ArrayList<>();
    if (dto.getGames() != null && !dto.getGames().isEmpty()) {
      games = gameHandler.findAllGamesById(dto.getGames());
      refTeam.setGames(games);
    }

    RefereeTeam saved = refereeTeamRepository.save(refTeam);
    return saved.getId();
  }

  /**
   * Deletes a refereeTeam.
   *
   * @param id the ID of the refereeTeam to delete
   * @return true if the refereeTeam was successfully deleted, false otherwise
   */
  public boolean deleteRefereeTeam(Long id) {
    Optional<RefereeTeam> opt = refereeTeamRepository.findById(id);

    if (opt.isPresent()) {
      RefereeTeam refTeam = opt.get();
      if (!refTeam.getIsActive()) return false;

      refTeam.setIsActive(false);
      refereeTeamRepository.save(refTeam);
      return true;
    }
    return false;
  }

  /**
   * Retrieves a list of all registered referee teams.
   *
   * @return list of all referee team DTOs containing team information, including main referee,
   *     assistant referees, and associated game/stats
   */
  public List<RefereeTeamDTO> getAllRefereeTeams() {
    return refereeTeamRepository.findAll().stream()
        .filter(RefereeTeam::getIsActive)
        .map(RefereeTeamMapper::toDTO)
        .collect(Collectors.toList());
  }

  /**
   * Retrieves a list of all registered referee teams with the given ids.
   *
   * @return list of all referee teams with the given ids
   */
  public List<RefereeTeam> findAllById(List<Long> ids) {
    return refereeTeamRepository.findAllById(ids);
  }

  public void save(RefereeTeam refereeTeam) {
    refereeTeamRepository.save(refereeTeam);
  }
}
