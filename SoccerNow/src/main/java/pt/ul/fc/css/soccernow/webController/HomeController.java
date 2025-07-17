package pt.ul.fc.css.soccernow.webController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pt.ul.fc.css.soccernow.business.services.UserService;

/**
 * Controller responsible for handling home operations.
 *
 * @author Ana Morgado 56957
 * @author João Azevedo 56913
 * @author Sofia Pereira 56352
 */
@Controller
public class HomeController {

  private final UserService userService;

  /**
   * Constructor that injects the user handler.
   *
   * @param userService the service responsible for user operations
   */
  @Autowired
  public HomeController(UserService userService) {
    this.userService = userService;
  }

  /**
   * Handles the root path and redirects to the login page.
   *
   * @return name of the view corresponding to the login page
   */
  @GetMapping("/")
  public String home() {
    return "login";
  }

  /**
   * Handles the home route and redirects to the home page.
   *
   * @return name of the view corresponding to the home page
   */
  @GetMapping("/home")
  public String showHomePage() {
    return "home";
  }

  /**
   * Handles the players route and redirects to the players page.
   *
   * @return name of the view corresponding to the players page
   */
  @GetMapping("/players")
  public String showPlayerPage() {
    return "players";
  }

  /**
   * Handles the referees route and redirects to the referees page.
   *
   * @return name of the view corresponding to the referees page
   */
  @GetMapping("/referees")
  public String showRefereePage() {
    return "referees";
  }

  @GetMapping("/championships")
  public String showChampionshipsPage() {
    return "championships";
  }

  @GetMapping("/games")
  public String showGamesPage() {
    return "games";
  }

  @GetMapping("/teams")
  public String showTeamsPage() {
    return "teams";
  }

  /**
   * Processes the login form submission.
   *
   * @param username the submitted username
   * @param password the submitted password
   * @param model the model used to pass attributes to the view
   * @return the name of the view to be rendered depending on login success or failure
   */
  @PostMapping("/login")
  public String login(
      @RequestParam("username") String username,
      @RequestParam("password") String password,
      Model model) {
    boolean loginDone = userService.login(username, password);

    if (loginDone) {
      model.addAttribute("username", username);
      return "home";
    } else {
      model.addAttribute("error", "Incorrect username or password!");
      return "login";
    }
  }
}
