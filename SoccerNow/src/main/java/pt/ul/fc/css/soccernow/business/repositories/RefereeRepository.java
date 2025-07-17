package pt.ul.fc.css.soccernow.business.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.ul.fc.css.soccernow.business.entities.Referee;

/**
 * Provides CRUD operations and custom query methods for referee management.
 *
 * @author Ana Morgado 56957
 * @author João Azevedo 56913
 * @author Sofia Pereira 56352
 */
@Repository
public interface RefereeRepository extends JpaRepository<Referee, Long> {

  List<Referee> findByIsActiveTrue();

  List<Referee> findByFirstName(String firstName);

  List<Referee> findByLastName(String lastName);

  List<Referee> findByHasCertificate(boolean hasCertificate);

  List<Referee> findByFirstNameAndLastName(String firstName, String lastName);
}
