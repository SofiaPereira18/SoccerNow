package pt.ul.fc.css.soccernow.business.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.ul.fc.css.soccernow.business.entities.RefereeTeam;

/**
 * Provides CRUD operations and custom query methods for referee teams management.
 *
 * @author Ana Morgado 56957
 * @author João Azevedo 56913
 * @author Sofia Pereira 56352
 */
@Repository
public interface RefereeTeamRepository extends JpaRepository<RefereeTeam, Long> {

  List<RefereeTeam> findByIsActiveTrue();
}
