package pt.ul.fc.css.soccernow.business.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pt.ul.fc.css.soccernow.business.entities.Player;

/**
 * Provides CRUD operations and custom query methods for player management.
 *
 * @author Ana Morgado 56957
 * @author João Azevedo 56913
 * @author Sofia Pereira 56352
 */
@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

  List<Player> findByFirstName(String firstName);

  List<Player> findByLastName(String lastName);

  List<Player> findByIsActiveTrue();

  @Query("SELECT p FROM Player p WHERE p.firstName = :firstName AND p.lastName = :lastName")
  Player findByFirstNameAndLastName(String firstName, String lastName);

  @Query(
      "SELECT p FROM Player p WHERE "
          + "(:firstName IS NULL OR p.firstName LIKE %:firstName%) AND "
          + "(:lastName IS NULL OR p.lastName LIKE %:lastName%) AND "
          + "(:position IS NULL OR p.position = :position)")
  List<Player> filterPlayers(
      @Param("firstName") String firstName,
      @Param("lastName") String lastName,
      @Param("position") String position);
}
