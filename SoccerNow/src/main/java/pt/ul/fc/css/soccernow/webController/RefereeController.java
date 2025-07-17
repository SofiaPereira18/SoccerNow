package pt.ul.fc.css.soccernow.webController;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pt.ul.fc.css.soccernow.business.dtos.UserDetailsDTO;
import pt.ul.fc.css.soccernow.business.services.UserService;

/**
 * Controller responsible for handling referee operations.
 *
 * @author Ana Morgado 56957
 * @author João Azevedo 56913
 * @author Sofia Pereira 56352
 */
@Controller
public class RefereeController {

  private final UserService userService;

  /**
   * Constructor that injects the user handler.
   *
   * @param userService the service responsible for referee operations
   */
  @Autowired
  public RefereeController(UserService userService) {
    this.userService = userService;
  }

  /**
   * Filters referees based on optional criteria such as first name, last name, number of cards
   * issued, and number of games refereed. The filtered list is added to the model to be displayed
   * in the view.
   *
   * @param firstName the referee's first name (optional)
   * @param lastName the referee's last name (optional)
   * @param cards the number of cards issued (optional)
   * @param games the number of games refereed (optional)
   * @param model the model used to pass data to the view
   * @return the name of the view to render (referees page)
   */
  @GetMapping("/referees/filter")
  public String filterPlayers(
      @RequestParam(value = "firstName", required = false) String firstName,
      @RequestParam(value = "lastName", required = false) String lastName,
      @RequestParam(value = "cards", required = false) Integer cards,
      @RequestParam(value = "games", required = false) Integer games,
      Model model) {

    List<UserDetailsDTO> referees = userService.filterReferees(firstName, lastName, cards, games);
    model.addAttribute("filtered", referees);

    return "referees";
  }
}
