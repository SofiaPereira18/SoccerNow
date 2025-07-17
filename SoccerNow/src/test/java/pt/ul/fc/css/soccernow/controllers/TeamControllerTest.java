package pt.ul.fc.css.soccernow.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import pt.ul.fc.css.soccernow.business.controllers.TeamController;
import pt.ul.fc.css.soccernow.business.dtos.StartingTeamRequestDTO;
import pt.ul.fc.css.soccernow.business.dtos.TeamDTO;
import pt.ul.fc.css.soccernow.business.entities.Player;
import pt.ul.fc.css.soccernow.business.entities.Player.PlayerPosition;
import pt.ul.fc.css.soccernow.business.entities.StartingTeam;
import pt.ul.fc.css.soccernow.business.entities.Team;
import pt.ul.fc.css.soccernow.business.handlers.TeamHandler;
import pt.ul.fc.css.soccernow.business.repositories.TeamRepository;

@ActiveProfiles("test")
@WebMvcTest(TeamController.class)
public class TeamControllerTest {

  @Autowired private MockMvc mockMvc;
  @MockBean private TeamRepository teamRepository;

  @MockBean private TeamHandler teamHandler;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @BeforeEach
  public void setup() {
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
  }

  @Test
  public void testGetAllTeams() throws Exception {
    // Mock the behavior of the teamHandler to return a list of teams
    List<TeamDTO> teams = new ArrayList<>();
    TeamDTO team1 =
        new TeamDTO(
            1L,
            "Team A",
            new ArrayList<>(),
            new ArrayList<>(),
            new ArrayList<>(),
            new ArrayList<>());
    TeamDTO team2 =
        new TeamDTO(
            2L,
            "Team B",
            new ArrayList<>(),
            new ArrayList<>(),
            new ArrayList<>(),
            new ArrayList<>());
    teams.add(team1);
    teams.add(team2);

    when(teamHandler.getAllTeams())
        .thenReturn(
            teams.stream()
                .map(
                    dto -> {
                      Team t = new Team();
                      t.setId(dto.getId());
                      t.setName(dto.getName());
                      t.setPlayers(new ArrayList<>());
                      t.setPodiumPositions(new ArrayList<>());
                      t.setStartingTeam(new ArrayList<>());
                      return t;
                    })
                .toList());

    // Perform the GET request and verify the response
    mockMvc
        .perform(
            get("/api/team")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(1L))
        .andExpect(jsonPath("$[0].name").value("Team A"))
        .andExpect(jsonPath("$[1].id").value(2L))
        .andExpect(jsonPath("$[1].name").value("Team B"));
  }

  @Test
  public void testGetTeamById() throws Exception {
    Long teamId = 3L;
    Team team = new Team();
    team.setId(teamId);
    team.setName("Benfica");
    team.setPlayers(new ArrayList<>());
    team.setPodiumPositions(new ArrayList<>());
    team.setStartingTeam(new ArrayList<>());

    when(teamHandler.getTeamById(teamId)).thenReturn(team);

    mockMvc
        .perform(get("/api/team/id/" + teamId).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(3L))
        .andExpect(jsonPath("$.name").value("Benfica"));
  }

  @Test
  public void testGetTeamByName() throws Exception {
    Long teamId = 4L;
    String name = "Porto";
    Team team = new Team();
    team.setId(teamId);
    team.setName(name);
    team.setPlayers(new ArrayList<>());
    team.setPodiumPositions(new ArrayList<>());
    team.setStartingTeam(new ArrayList<>());

    when(teamHandler.getTeamByName(name)).thenReturn(team);

    mockMvc
        .perform(get("/api/team/name/" + name).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(4L))
        .andExpect(jsonPath("$.name").value("Porto"));
  }

  @Test
  public void testCreateTeam() throws Exception {
    Long id = 1L;

    Team team =
        new Team(
            "Sporting", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    team.setId(id);

    when(teamHandler.registerTeam(team.getName())).thenReturn(team);

    mockMvc
        .perform(post("/api/team/name/" + team.getName()).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(id))
        .andExpect(jsonPath("$.name").value("Sporting"));
  }

  @Test
  public void testCreatStartingTeamWithPlayers() throws Exception {
    Long teamId = 3L;
    Long stId = 2L;
    Long p1 = 1L;
    Long p2 = 1L;
    Long p3 = 1L;
    Long p4 = 1L;
    Long p5 = 1L;

    Team team = new Team();
    team.setStartingTeam(new ArrayList<>());
    team.setPlayers(new ArrayList<>());
    team.setId(teamId);
    team.setName("Braga");

    StartingTeam st = new StartingTeam();
    st.setId(stId);
    st.setTeam(team);
    st.setGames(new ArrayList<>());
    st.setPlayers(new ArrayList<>());
    team.addStartingTeam(st);

    Player pl1 = new Player();
    pl1.setId(p1);
    pl1.setFirstName("João");
    pl1.setLastName("Azevedo");
    pl1.setPosition(PlayerPosition.OTHER);
    pl1.setStartingTeams(new ArrayList<>());
    pl1.setTeams(new ArrayList<>());
    pl1.addStartingTeam(st);
    st.addPlayer(pl1);
    team.addPlayer(pl1);

    Player pl2 = new Player();
    pl2.setId(p2);
    pl2.setFirstName("Ana");
    pl2.setLastName("Morgado");
    pl2.setPosition(PlayerPosition.OTHER);
    pl2.setStartingTeams(new ArrayList<>());
    pl2.setTeams(new ArrayList<>());
    pl2.addStartingTeam(st);
    st.addPlayer(pl2);
    team.addPlayer(pl2);

    Player pl3 = new Player();
    pl3.setId(p3);
    pl3.setFirstName("Sofia");
    pl3.setLastName("Pereira");
    pl3.setPosition(PlayerPosition.OTHER);
    pl3.setStartingTeams(new ArrayList<>());
    pl3.setTeams(new ArrayList<>());
    pl3.addStartingTeam(st);
    st.addPlayer(pl3);
    team.addPlayer(pl3);

    Player pl4 = new Player();
    pl4.setId(p4);
    pl4.setFirstName("Pedro");
    pl4.setLastName("Pascal");
    pl4.setPosition(PlayerPosition.OTHER);
    pl4.setStartingTeams(new ArrayList<>());
    pl4.setTeams(new ArrayList<>());
    pl4.addStartingTeam(st);
    st.addPlayer(pl4);
    team.addPlayer(pl4);

    Player pl5 = new Player();
    pl5.setId(p5);
    pl5.setFirstName("Rita");
    pl5.setLastName("Gomes");
    pl5.setPosition(PlayerPosition.GOALKEEPER);
    pl5.setStartingTeams(new ArrayList<>());
    pl5.setTeams(new ArrayList<>());
    pl5.addStartingTeam(st);
    st.addGK(pl5);
    team.addPlayer(pl5);

    List<Long> playersId = new ArrayList<>();
    playersId.add(p1);
    playersId.add(p2);
    playersId.add(p3);
    playersId.add(p4);

    StartingTeamRequestDTO stRDTO = new StartingTeamRequestDTO();
    stRDTO.setPlayers(playersId);
    stRDTO.setGoalKeeper(p5);

    when(teamHandler.createStartingTeamWithPlayer(teamId, playersId, p5)).thenReturn(st);

    mockMvc
        .perform(
            post("/api/team/" + teamId + "/starting-team/players")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(stRDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(stId));
  }

  @Test
  public void testDeleteTeam() throws Exception {
    Long teamid = 5L;
    Team team = new Team();
    team.setId(teamid);
    team.setName("Beleneses");

    when(teamHandler.removeTeam(teamid)).thenReturn(true);

    mockMvc
        .perform(delete("/api/team/" + teamid).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());
  }
}
