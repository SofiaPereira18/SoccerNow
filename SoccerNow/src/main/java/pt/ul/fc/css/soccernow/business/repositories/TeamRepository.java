package pt.ul.fc.css.soccernow.business.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pt.ul.fc.css.soccernow.business.entities.Team;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

  @Query("SELECT t FROM Team t WHERE t.name = :name AND t.isActive = true")
  Optional<Team> findByName(String name);

  @Query("SELECT t FROM Team t WHERE t.isActive = true")
  List<Team> findAllTeams();

  @Query("SELECT t FROM Team t JOIN t.players p WHERE p.id = :playerId")
  List<Team> findTeamsByPlayerId(Long playerId);

  Optional<Team> findById(Long id);

  @Query("SELECT t FROM Team t WHERE t.isActive = true AND t.id = :id")
  Optional<Team>findActiveById(Long id);
}
