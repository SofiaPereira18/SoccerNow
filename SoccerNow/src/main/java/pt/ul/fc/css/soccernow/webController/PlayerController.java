package pt.ul.fc.css.soccernow.webController;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pt.ul.fc.css.soccernow.business.dtos.UserDetailsDTO;
import pt.ul.fc.css.soccernow.business.entities.Player;
import pt.ul.fc.css.soccernow.business.services.UserService;

/**
 * Controller responsible for handling requests related to player.
 *
 * @author Ana Morgado 56957
 * @author João Azevedo 56913
 * @author Sofia Pereira 56352
 */
@Controller
public class PlayerController {

  private final UserService userService;

  /**
   * Constructor that injects the user handler.
   *
   * @param userService the service responsible for the player operations
   */
  @Autowired
  public PlayerController(UserService userService) {
    this.userService = userService;
  }

  /**
   * Searches for a player by ID and adds the result to the model. If the player is found, it is
   * passed to the view; otherwise, an error message appears.
   *
   * @param searchId the ID of the player to search
   * @param model the model used to pass attributes to the view
   * @return the name of the view to render (players page)
   */
  @GetMapping("/players/search")
  public String searchPlayerById(
      @RequestParam(value = "searchId", required = false) Long searchId, Model model) {

    Player player = userService.getPlayerById(searchId);

    if (player != null) {
      if (!player.getIsActive()) {
          model.addAttribute("inactivePlayer", true); 
      } else {
          model.addAttribute("player", player); 
      }
    }

    return "players";
  }

  /**
   * Filters players based on multiple optional criteria such as name, position, number of goals,
   * cards, and games played. The filtered list is added to the model.
   *
   * @param firstName the first name of the player (optional)
   * @param lastName the last name of the player (optional)
   * @param position the player's position (optional)
   * @param goals the number of goals scored (optional)
   * @param cardsGoal the number of cards received (optional)
   * @param games the number of games played (optional)
   * @param model the model used to pass attributes to the view
   * @return the name of the view to render (players page)
   */
  @GetMapping("/players/filter")
  public String filterPlayers(
      @RequestParam(value = "firstName", required = false) String firstName,
      @RequestParam(value = "lastName", required = false) String lastName,
      @RequestParam(value = "position", required = false) String position,
      @RequestParam(value = "goals", required = false) Integer goals,
      @RequestParam(value = "cardsGoal", required = false) Integer cardsGoal,
      @RequestParam(value = "games", required = false) Integer games,
      Model model) {

    List<UserDetailsDTO> filteredPlayers =
        userService.filterPlayers(firstName, lastName, position, goals, cardsGoal, games);
    model.addAttribute("filteredPlayers", filteredPlayers);

    return "players";
  }
}
