package pt.ul.fc.css.soccernow.business.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.ul.fc.css.soccernow.business.entities.Stats;

@Repository
public interface StatsRepository extends JpaRepository<Stats, Long> {}
