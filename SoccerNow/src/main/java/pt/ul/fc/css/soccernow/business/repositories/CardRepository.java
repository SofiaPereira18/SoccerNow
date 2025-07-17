package pt.ul.fc.css.soccernow.business.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.ul.fc.css.soccernow.business.entities.Card;
import pt.ul.fc.css.soccernow.business.entities.Player;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

  List<Card> findByPlayer(Player player);
}
