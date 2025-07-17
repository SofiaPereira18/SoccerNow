package pt.ul.fc.css.soccernow.webController;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pt.ul.fc.css.soccernow.business.entities.Championship;
import pt.ul.fc.css.soccernow.business.services.ChampionshipService;

@Controller
public class WebChampionshipController {

  @Autowired ChampionshipService champService;

  public WebChampionshipController() {
    super();
  }

  @GetMapping("/championships/search")
  public String searchChampionshipById(
      @RequestParam(value = "searchId", required = false) Long searchId, Model model) {
    Championship championship = null;
    if (searchId != null) {
      championship = champService.getChampionshipById(searchId);
    }
    model.addAttribute("championships", championship);
    return "championships";
  }

  @GetMapping("/championships/filter")
  public String filterChampionships(
      @RequestParam(value = "name", required = false) String name,
      @RequestParam(value = "team", required = false) String team,
      @RequestParam(value = "finishedGames", required = false) Integer fGames,
      @RequestParam(value = "unfinishedGames", required = false) Integer uGames,
      @RequestParam(value = "status", required = false) String status,
      Model model) {

    List<Championship> filteredChampionships =
        champService.filterChampionships(name, team, fGames, uGames, status);

    model.addAttribute("filteredChampionships", filteredChampionships);
    return "championships";
  }
}
