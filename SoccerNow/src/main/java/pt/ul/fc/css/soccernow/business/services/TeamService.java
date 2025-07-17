package pt.ul.fc.css.soccernow.business.services;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import pt.ul.fc.css.soccernow.business.entities.Game;
import pt.ul.fc.css.soccernow.business.entities.Player;
import pt.ul.fc.css.soccernow.business.entities.PodiumPosition;
import pt.ul.fc.css.soccernow.business.entities.StartingTeam;
import pt.ul.fc.css.soccernow.business.entities.Stats;
import pt.ul.fc.css.soccernow.business.entities.Team;
import pt.ul.fc.css.soccernow.business.handlers.TeamHandler;

@Component
public class TeamService {

  @Autowired private TeamHandler tHandler;

  @Autowired
  public TeamService(@Lazy TeamHandler tHandler) {
    this.tHandler = tHandler;
  }

  public void setTeamHandler(TeamHandler tHandler) {
    this.tHandler = tHandler;
  }

  public TeamHandler getTeamHandler() {
    return tHandler;
  }

  public List<Team> getAllActiveTeams() {
    return tHandler.getAllTeams();
  }

  public Team getTeamById(Long teamId) {
    return tHandler.getTeamById(teamId);
  }

  public int getNumberActivePlayers(Team team) {
    int count = 0;
    for (Player player : team.getPlayers()) {
      if (player.getIsActive()) {
        count++;
      }
    }
    return count;
  }

  public int getVictories(Team team) {
    List<Game> allGames = tHandler.getAllGames(team.getId());
    int count = 0;
    for (Game g : allGames) {
      if (g.isActive() && g.isFinished()) {
        Stats.GameResult result = g.getStats().getResult();
        if (result.equals(Stats.GameResult.AWAY_TEAM)) {
          StartingTeam awayteam = g.getStartingTeams().get(1);
          if (awayteam.getTeam().equals(team)) {
            count++;
          }
        } else if (result.equals(Stats.GameResult.HOME_TEAM)) {
          StartingTeam homeTeam = g.getStartingTeams().get(0);
          if (homeTeam.getTeam().equals(team)) {
            count++;
          }
        }
      }
    }
    return count;
  }

  public int getLosses(Team team) {
    List<Game> allGames = tHandler.getAllGames(team.getId());
    int count = 0;
    for (Game g : allGames) {
      if (g.isActive() && g.isFinished()) {
        Stats.GameResult result = g.getStats().getResult();
        if (result.equals(Stats.GameResult.AWAY_TEAM)) {
          StartingTeam awayteam = g.getStartingTeams().get(1);
          if (!awayteam.getTeam().equals(team)) {
            count++;
          }
        } else if (result.equals(Stats.GameResult.HOME_TEAM)) {
          StartingTeam homeTeam = g.getStartingTeams().get(0);
          if (!homeTeam.getTeam().equals(team)) {
            count++;
          }
        }
      }
    }
    return count;
  }

  public int getDraws(Team team) {
    List<Game> allGames = tHandler.getAllGames(team.getId());
    int count = 0;
    for (Game g : allGames) {
      if (g.isActive() && g.isFinished()) {
        Stats.GameResult result = g.getStats().getResult();
        if (result.equals(Stats.GameResult.DRAW)) {
          count++;
        }
      }
    }
    return count;
  }

  public List<Team> filterTeams(
      String name,
      Integer players,
      Integer victories,
      Integer losses,
      Integer draws,
      String conquistas) {

    List<Team> teamList = getAllActiveTeams();

    if (name != null && !name.isEmpty()) {
      teamList =
          teamList.stream()
              .filter(t -> t.getName().equalsIgnoreCase(name))
              .collect(Collectors.toList());
    }

    if (victories != null) {
      teamList =
          teamList.stream().filter(t -> getVictories(t) == victories).collect(Collectors.toList());
    }

    if (losses != null) {
      teamList = teamList.stream().filter(t -> getLosses(t) == losses).collect(Collectors.toList());
    }

    if (draws != null) {
      teamList = teamList.stream().filter(t -> getDraws(t) == draws).collect(Collectors.toList());
    }

    System.out.println(
        "************************************************************************************************************************************");
    System.out.println(conquistas);

    if (conquistas != null && !conquistas.isEmpty()) {
      teamList =
          teamList.stream()
              .filter(
                  t -> {
                    PodiumPosition.Position pos = null;
                    if (conquistas.equalsIgnoreCase("first")) {
                      pos = PodiumPosition.Position.FIRST;
                    } else if (conquistas.equalsIgnoreCase("second")) {
                      pos = PodiumPosition.Position.SECOND;
                    } else if (conquistas.equalsIgnoreCase("third")) {
                      pos = PodiumPosition.Position.THIRD;
                    }
                    List<PodiumPosition> ppList = t.getPodiumPositions();
                    for (PodiumPosition pp : ppList) {
                      if (pp.getPos().equals(pos)) {
                        return true;
                      }
                    }
                    return false;
                  })
              .collect(Collectors.toList());
    }

    return teamList;
  }

  public StartingTeam getStartingTeamById(Long startingTeamId) {
    return tHandler.getStartingTeamById(startingTeamId);
  }
}
