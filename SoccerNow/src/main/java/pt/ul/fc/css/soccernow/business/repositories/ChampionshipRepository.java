package pt.ul.fc.css.soccernow.business.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pt.ul.fc.css.soccernow.business.entities.Championship;

@Repository
public interface ChampionshipRepository extends JpaRepository<Championship, Long> {

  @Query("SELECT c FROM Championship c WHERE c.id = :id AND c.isActive = true")
  Optional<Championship> findActiveById(Long id);

  List<Championship> findByIsActiveTrue();

  @Query("SELECT c FROM Championship c JOIN c.teams t WHERE t.id = :teamId AND c.isActive = true")
  List<Championship> findByTeams_Id(Long teamId);

  @Query("SELECT c FROM Championship c WHERE c.name = :name AND c.isActive = true")
  List<Championship> findActiveByName(String name);

  @Query("SELECT c FROM Championship c WHERE c.isActive = true AND c.isFinished = true")
  List<Championship> findAllFinished();

  @Query("SELECT c FROM Championship c WHERE c.isActive = true AND c.isFinished = false")
  List<Championship> findAllUnfinished();
}
