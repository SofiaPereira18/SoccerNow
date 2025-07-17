package pt.ul.fc.css.soccernow.business.handlers;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;
import pt.ul.fc.css.soccernow.business.entities.StartingTeam;
import pt.ul.fc.css.soccernow.business.repositories.StartingTeamRepository;

/**
 * Handler for the starting team operations.
 *
 * @author Ana Morgado 56957
 * @author João Azevedo 56913
 * @author Sofia Pereira 56352
 */
@Component
public class StartingTeamHandler {

  private final StartingTeamRepository startingTeamRepository;

  /**
   * Constructs a new StartingTeamHandler with the specified repositories.
   *
   * @param startingTeamRepository the starting team repository to be used for data access
   */
  public StartingTeamHandler(StartingTeamRepository startingTeamRepository) {
    this.startingTeamRepository = startingTeamRepository;
  }

  /**
   * Deletes an starting team from the repository.
   *
   * @param startingTeam the starting team to delete
   */
  public void delete(StartingTeam startingTeam) {
    startingTeamRepository.delete(startingTeam);
  }

  /**
   * Finds an starting team from the repository.
   *
   * @param id the id of the starting team to delete
   * @return the Optional with the starting team if found, otherwise returns empty optional
   */
  public Optional<StartingTeam> findById(Long id) {
    return startingTeamRepository.findActiveById(id);
  }

  /**
   * Finds all the starting teams from the repository.
   *
   * @return an list with all the starting teams from the repository
   */
  public List<StartingTeam> findAll() {
    return startingTeamRepository.findActiveAll();
  }

  /**
   * Finds a specific starting team from the repository.
   *
   * @param id the id of the specific starting team to be found
   */
  public List<StartingTeam> findAllById(List<Long> ids) {
    return startingTeamRepository.findAllActiveById(ids);
  }

  /**
   * Saves an starting team in the repository.
   *
   * @param startingTeam the starting team to save
   */
  public StartingTeam save(StartingTeam startingTeam) {
    return startingTeamRepository.save(startingTeam);
  }
}
