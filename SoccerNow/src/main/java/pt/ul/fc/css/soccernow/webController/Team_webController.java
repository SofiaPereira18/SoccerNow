package pt.ul.fc.css.soccernow.webController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pt.ul.fc.css.soccernow.business.entities.PodiumPosition;
import pt.ul.fc.css.soccernow.business.entities.Team;
import pt.ul.fc.css.soccernow.business.services.TeamService;

@Controller
public class Team_webController {

  private final TeamService tService;

  @Autowired
  public Team_webController(TeamService tService) {
    this.tService = tService;
  }

  @GetMapping("/teams/search")
  public String searchTeamById(
      @RequestParam(value = "searchId", required = false) Long teamId, Model model) {

    Team team = tService.getTeamById(teamId);
    if (team != null) {
      int pSize = tService.getNumberActivePlayers(team);
      int victories = tService.getVictories(team);
      int losses = tService.getLosses(team);
      int draws = tService.getDraws(team);
      List<PodiumPosition> pp =
          team.getPodiumPositions() != null ? team.getPodiumPositions() : new ArrayList<>();

      model.addAttribute("team", team);
      model.addAttribute("pSize", pSize);
      model.addAttribute("victories", victories);
      model.addAttribute("losses", losses);
      model.addAttribute("draws", draws);
      model.addAttribute("pp", pp);
    }

    return "teams";
  }

  @GetMapping("/teams/filter")
  public String filterTeams(
      @RequestParam(value = "name", required = false) String name,
      @RequestParam(value = "players", required = false) Integer players,
      @RequestParam(value = "victories", required = false) Integer victories,
      @RequestParam(value = "losses", required = false) Integer losses,
      @RequestParam(value = "draws", required = false) Integer draws,
      @RequestParam(value = "status", required = false) String conquistas,
      Model model) {

    List<Team> teamList = tService.filterTeams(name, players, victories, losses, draws, conquistas);

    Map<Long, Integer> victoriesM = new HashMap<>();
    Map<Long, Integer> lossesM = new HashMap<>();
    Map<Long, Integer> drawsM = new HashMap<>();
    for (Team team : teamList) {
      victoriesM.put(team.getId(), tService.getVictories(team));
      lossesM.put(team.getId(), tService.getLosses(team));
      drawsM.put(team.getId(), tService.getDraws(team));
    }

    model.addAttribute("filteredTeams", teamList);
    model.addAttribute("victories", victoriesM);
    model.addAttribute("losses", lossesM);
    model.addAttribute("draws", drawsM);

    return "teams";
  }
}
