package pt.ul.fc.css.soccernow.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pt.ul.fc.css.soccernow.business.entities.Player;
import pt.ul.fc.css.soccernow.business.entities.Player.PlayerPosition;
import pt.ul.fc.css.soccernow.business.entities.Referee;
import pt.ul.fc.css.soccernow.business.entities.User;
import pt.ul.fc.css.soccernow.business.repositories.PlayerRepository;
import pt.ul.fc.css.soccernow.business.repositories.RefereeRepository;
import pt.ul.fc.css.soccernow.business.repositories.UserRepository;

/**
 * Tests for the user repository.
 *
 * @author Ana Morgado 56957
 * @author João Azevedo 56913
 * @author Sofia Pereira 56352
 */
@DataJpaTest
public class UserRepositoriesTests {

  @Autowired private UserRepository userRepository;

  @Autowired private PlayerRepository playerRepository;

  @Autowired private RefereeRepository refereeRepository;

  private final List<Long> ids = new ArrayList<>();

  /**
   * Setup method to prepare test data before each test. It creates and saves players and referee,
   * storing their IDs for further tests.
   */
  @BeforeEach
  void setup() {
    ids.add(
        playerRepository
            .save(new Player("Sofia", "Pereira", PlayerPosition.OTHER, null, null))
            .getId());
    ids.add(
        playerRepository
            .save(new Player("Joao", "Azevedo", PlayerPosition.OTHER, null, null))
            .getId());
    ids.add(refereeRepository.save(new Referee("Ana", "Morgado", true, null)).getId());
  }

  /**
   * Tests the saving of a new user. Verifies that a player is successfully saved to the repository.
   */
  @Test
  void shouldSaveUser() {
    Player newPlayer = new Player("Cristiano", "Ronaldo", PlayerPosition.OTHER, null, null);
    Player savedPlayer = playerRepository.save(newPlayer);
    assertNotNull(savedPlayer.getId());
    assertEquals("Cristiano", savedPlayer.getFirstName());
    assertEquals("Ronaldo", savedPlayer.getLastName());
  }

  /** Tests finding a user by its ID. Verifies that the user can be found by its ID. */
  @Test
  void shouldFindUserById() {
    Optional<User> user = userRepository.findById(ids.get(1));
    assertTrue(user.isPresent());
  }

  /**
   * Tests the deletion of a user. Verifies that a user can be deleted and no longer exists in the
   * repository.
   */
  @Test
  void shouldDeleteUser() {
    Long idToDelete = ids.get(0);
    playerRepository.deleteById(idToDelete);
    Optional<Player> deletedPlayer = playerRepository.findById(idToDelete);
    assertTrue(deletedPlayer.isEmpty());
  }

  /** Tests the updating of a user. Verifies that the player's first name is updated correctly. */
  @Test
  void shouldUpdateUser() {
    Player playerToUpdate = playerRepository.findById(ids.get(0)).get();
    playerToUpdate.setFirstName("UpdatedName");
    Player updatedPlayer = playerRepository.save(playerToUpdate);
    assertEquals("UpdatedName", updatedPlayer.getFirstName());
  }

  /**
   * Tests retrieving all users from the repository. Verifies that the correct number of users is
   * returned.
   */
  @Test
  void shouldFindAllUsers() {
    List<User> users = userRepository.findAll();
    assertEquals(3, users.size());
  }

  /**
   * Tests retrieving all players from the repository. Verifies that the correct number of players
   * is returned.
   */
  @Test
  void shouldFindAllPlayers() {
    List<Player> players = playerRepository.findAll();
    assertEquals(2, players.size());
  }

  /**
   * Tests retrieving all referees from the repository. Verifies that the correct number of referees
   * is returned.
   */
  @Test
  void shouldFindAllReferees() {
    List<Referee> referees = refereeRepository.findAll();
    assertEquals(1, referees.size());
  }

  /** Tests finding a non-existent user. Verifies that a non-existent user cannot be found. */
  @Test
  void shouldNotFindNonExistentUser() {
    Long id = 999L;
    Optional<User> user = userRepository.findById(id);
    assertTrue(user.isEmpty());
  }

  /**
   * Tests finding a player by his last name. Verifies that players can be found by their last name.
   */
  @Test
  void shouldFindPlayerByLastName() {
    List<Player> players = playerRepository.findByLastName("Pereira");
    assertTrue(players.size() == 1);

    assertEquals("Pereira", players.get(0).getLastName());
  }
}
