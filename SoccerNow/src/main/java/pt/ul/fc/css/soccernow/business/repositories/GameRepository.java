package pt.ul.fc.css.soccernow.business.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pt.ul.fc.css.soccernow.business.entities.Game;
import pt.ul.fc.css.soccernow.business.entities.Game.GameShift;
import pt.ul.fc.css.soccernow.business.entities.GameLocation;
import pt.ul.fc.css.soccernow.business.entities.StartingTeam;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

  // Find games by game type (Friendly or Championship)
  @Query("SELECT g FROM Game g WHERE g.isActive = true AND g.gameType = :gameType")
  List<Game> findByGameType(Game.GameType gameType);

  // find games by a team
  List<Game> findByStartingTeams(StartingTeam starting);

  // find games by location
  List<Game> findByLocation(GameLocation location);

  List<Game> findByIsActiveTrue();

  @Query("SELECT g FROM Game g WHERE g.id = :gameId AND g.isActive = true")
  Optional<Game> findActiveById(Long gameId);

  @Query("SELECT g FROM Game g WHERE g.isActive = true AND g.isFinished = true")
  List<Game> findAllFinished();

  @Query("SELECT g FROM Game g WHERE g.isActive = true AND g.isFinished = false")
  List<Game> findAllUnfinished();

  @Query("SELECT g FROM Game g WHERE g.isActive = true AND g.gameShift = :gameShift")
  List<Game> findGameByGameShift(GameShift gameShift);
}
