package pt.ul.fc.css.soccernow.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pt.ul.fc.css.soccernow.business.entities.Referee;
import pt.ul.fc.css.soccernow.business.entities.RefereeTeam;
import pt.ul.fc.css.soccernow.business.repositories.RefereeRepository;
import pt.ul.fc.css.soccernow.business.repositories.RefereeTeamRepository;

/**
 * Tests for the referee team repository.
 *
 * @author Ana Morgado 56957
 * @author João Azevedo 5691
 * @author Sofia Pereira 56352
 */
@DataJpaTest
public class RefereeTeamRepositoriesTests {

  @Autowired private RefereeRepository refereeRepository;

  @Autowired private RefereeTeamRepository refereeTeamRepository;

  private Referee mainReferee;
  private Referee assistantReferee1;
  private Referee assistantReferee2;

  private RefereeTeam refereeTeam;

  /**
   * Setup method to prepare data before each test. Creates referees and a referee team for the
   * tests.
   */
  @BeforeEach
  void setup() {
    // criar arbitros principais e os assistentes
    mainReferee = refereeRepository.save(new Referee("Carlos", "Costa", true, null));
    assistantReferee1 = refereeRepository.save(new Referee("Ricardo", "Paiva", false, null));
    assistantReferee2 = refereeRepository.save(new Referee("Sara", "Ribeiro", true, null));

    // criar a equipa de arbitros
    List<Referee> assistantReferees = new ArrayList<>();
    assistantReferees.add(assistantReferee1);
    assistantReferees.add(assistantReferee2);

    refereeTeam = new RefereeTeam(true, mainReferee, assistantReferees, null);
    refereeTeamRepository.save(refereeTeam);
  }

  /**
   * Tests the creation of a referee team. Verifies that the team is created correctly with the main
   * referee and assistant referees.
   */
  @Test
  void shouldCreateRefereeTeam() {
    assertNotNull(refereeTeam.getId());
    assertTrue(refereeTeam.isChampionshipGame());
    assertEquals(mainReferee, refereeTeam.getMainReferee());
    assertEquals(2, refereeTeam.getReferees().size());
  }

  /**
   * Tests adding a new assistant referee to the team. Verifies that the referee was correctly added
   * to the team.
   */
  @Test
  void shouldAddAssistantReferee() {
    Referee newAssistant = new Referee("Pedro", "Alves", false, null);
    refereeRepository.save(newAssistant);
    refereeTeam.addReferee(newAssistant);

    assertTrue(refereeTeam.getReferees().contains(newAssistant));
    assertEquals(3, refereeTeam.getReferees().size());
  }

  /**
   * Tests removing an assistant referee from the team. Verifies that the referee was correctly
   * removed from the team.
   */
  @Test
  void shouldRemoveAssistantReferee() {
    refereeTeam.removeReferee(assistantReferee1);

    assertFalse(refereeTeam.getReferees().contains(assistantReferee1));
    assertEquals(1, refereeTeam.getReferees().size());
  }

  /**
   * Tests finding a referee team by its ID. Verifies that the team can be retrieved correctly from
   * the database.
   */
  @Test
  void shouldFindRefereeTeamById() {
    RefereeTeam foundRefereeTeam =
        refereeTeamRepository.findById(refereeTeam.getId()).orElseThrow();

    assertNotNull(foundRefereeTeam);
    assertEquals(refereeTeam.getId(), foundRefereeTeam.getId());
  }

  /**
   * Tests deleting a referee team. Verifies that the team is correctly deleted from the database.
   */
  @Test
  void shouldDeleteRefereeTeam() {
    Long teamId = refereeTeam.getId();
    refereeTeamRepository.deleteById(teamId);

    assertFalse(refereeTeamRepository.findById(teamId).isPresent());
  }
}
