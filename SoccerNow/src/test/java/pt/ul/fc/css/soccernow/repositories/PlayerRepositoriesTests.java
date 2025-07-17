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
import pt.ul.fc.css.soccernow.business.repositories.PlayerRepository;

/**
 * Tests for the player repository.
 *
 * @author Ana Morgado 56957
 * @author João Azevedo 56913
 * @author Sofia Pereira 56352
 */
@DataJpaTest
public class PlayerRepositoriesTests {

  @Autowired private PlayerRepository playerRepository;

  private final List<Long> ids = new ArrayList<>();

  /** Sets up initial data for testing, saving players to the repository. */
  @BeforeEach
  void setup() {
    ids.add(
        playerRepository
            .save(new Player("Tomas", "Gregorio", PlayerPosition.OTHER, null, null))
            .getId());
    ids.add(
        playerRepository
            .save(new Player("Margarida", "Ribeiro", PlayerPosition.OTHER, null, null))
            .getId());
  }

  /** Tests saving a new player. Verifies that the player is saved correctly and has a valid ID. */
  @Test
  void shouldSavePlayer() {
    Player newPlayer = new Player("Cristiano", "Ronaldo", PlayerPosition.OTHER, null, null);
    Player savedPlayer = playerRepository.save(newPlayer);
    assertNotNull(savedPlayer.getId());
    assertEquals("Cristiano", savedPlayer.getFirstName());
    assertEquals("Ronaldo", savedPlayer.getLastName());
  }

  /** Tests finding a player by ID. Verifies that the correct player is found by the provided ID. */
  @Test
  void shouldFindPlayerById() {
    Optional<Player> player = playerRepository.findById(ids.get(1));
    assertTrue(player.isPresent());
  }

  /**
   * Tests finding all players in the repository. Verifies that the correct number of players are
   * returned.
   */
  @Test
  void shouldFindAllPlayers() {
    List<Player> players = playerRepository.findAll();
    assertEquals(2, players.size());
  }

  /**
   * Tests deleting a player by ID. Verifies that the player is successfully deleted from the
   * repository.
   */
  @Test
  void shouldDeletePlayer() {
    Long idToDelete = ids.get(0);
    playerRepository.deleteById(idToDelete);
    Optional<Player> deletedPlayer = playerRepository.findById(idToDelete);
    assertTrue(deletedPlayer.isEmpty());
  }

  /**
   * Tests updating a player's information. Verifies that the player's information is updated
   * correctly.
   */
  @Test
  void shouldUpdatePlayer() {
    Player playerToUpdate = playerRepository.findById(ids.get(0)).get();
    playerToUpdate.setFirstName("UpdatedName");
    Player updatedPlayer = playerRepository.save(playerToUpdate);
    assertEquals("UpdatedName", updatedPlayer.getFirstName());
  }

  /**
   * Tests finding players by first name. Verifies that the correct players are returned by the
   * given first name.
   */
  @Test
  void shouldFindPlayerByFirstName() {
    List<Player> players = playerRepository.findByFirstName("Tomas");
    assertEquals(1, players.size());
    assertEquals("Gregorio", players.get(0).getLastName());
  }

  /**
   * Tests finding players by last name. Verifies that the correct players are returned by the given
   * last name.
   */
  @Test
  void shouldFindPlayerByLastName() {
    List<Player> players = playerRepository.findByLastName("Ribeiro");
    assertEquals(1, players.size());
    assertEquals("Margarida", players.get(0).getFirstName());
  }
}
