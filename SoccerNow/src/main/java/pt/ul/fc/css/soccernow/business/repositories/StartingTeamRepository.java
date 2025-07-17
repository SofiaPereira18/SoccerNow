package pt.ul.fc.css.soccernow.business.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pt.ul.fc.css.soccernow.business.entities.StartingTeam;

@Repository
public interface StartingTeamRepository extends JpaRepository<StartingTeam, Long> {

  // Remove or rename this method!
  // Optional<StartingTeam> findById(Long id);

  @Query(
      "SELECT st FROM StartingTeam st JOIN st.players p WHERE st.isActive = true AND p.firstName ="
          + " :firstName AND p.lastName = :lastName")
  List<StartingTeam> findTeamsByPlayerName(String firstName, String lastName);

  // If you want to find by id and active:
  @Query("SELECT st FROM StartingTeam st WHERE st.isActive = true AND st.id = :id")
  Optional<StartingTeam> findActiveById(Long id);
 
  @Query("SELECT st FROM StartingTeam st WHERE st.isActive = true")
  List<StartingTeam> findActiveAll();

  @Query("SELECT st FROM StartingTeam st WHERE st.isActive = true AND st.id IN :ids")
  List<StartingTeam> findAllActiveById(List<Long> ids);
}
