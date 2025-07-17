package pt.ul.fc.css.soccernow.business.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.ul.fc.css.soccernow.business.entities.Championship;
import pt.ul.fc.css.soccernow.business.entities.Team;
import pt.ul.fc.css.soccernow.business.handlers.ChampionshipHandler;
import pt.ul.fc.css.soccernow.business.handlers.TeamHandler;

@Service
public class ChampionshipService {

  @Autowired private ChampionshipHandler championshipHandler;

  @Autowired private TeamHandler teamHandler;

  public Championship getChampionshipById(Long searchId) {
    return championshipHandler.getChampionshipById(searchId);
  }

  public List<Championship> filterChampionships(
      String name, String teamName, Integer fGames, Integer uGames, String status) {
    List<Championship> filteredChampionships = championshipHandler.getAllChampionships();

    // Filter by championship name
    if (name != null && !name.isEmpty()) {
      filteredChampionships =
          filteredChampionships.stream()
              .filter(c -> c.getName().equalsIgnoreCase(name))
              .collect(Collectors.toList());
    }

    // Filter by team
    if (teamName != null && !teamName.isEmpty()) {
      Team team = teamHandler.getTeamByName(teamName);
      if (team != null) {
        List<Championship> teamFilter = championshipHandler.getChampionshipsByTeamId(team.getId());
        filteredChampionships.retainAll(teamFilter);
      } else {
        // No such team exists, return empty list
        return new ArrayList<>();
      }
    }

    // Filter by number of finished games
    if (fGames != null) {
      List<Championship> fgamesFilter =
          championshipHandler.getChampionshipByNumberOfFinishedGames(fGames);
      filteredChampionships.retainAll(fgamesFilter);
    }

    // Filter by number of unfinished games
    if (uGames != null) {
      List<Championship> ugamesFilter =
          championshipHandler.getChampionshipByNumberOfUnfinishedGames(uGames);
      filteredChampionships.retainAll(ugamesFilter);
    }

    // Filter by status
    if (status != null && !status.isEmpty()) {
      List<Championship> statusFilter = new ArrayList<>();
      if (status.equalsIgnoreCase("finished")) {
        statusFilter = championshipHandler.getFinishedChampionships();
      } else if (status.equalsIgnoreCase("unfinished")) {
        statusFilter = championshipHandler.getUnfinishedChampionships();
      }
      filteredChampionships.retainAll(statusFilter);
    }

    return filteredChampionships;
  }
}
