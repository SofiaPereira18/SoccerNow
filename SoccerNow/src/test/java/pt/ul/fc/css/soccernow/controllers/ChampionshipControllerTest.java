package pt.ul.fc.css.soccernow.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import pt.ul.fc.css.soccernow.business.controllers.ChampionshipController;
import pt.ul.fc.css.soccernow.business.dtos.ChampionshipRequestDTO;
import pt.ul.fc.css.soccernow.business.dtos.GameRequestDTO;
import pt.ul.fc.css.soccernow.business.entities.Championship;
import pt.ul.fc.css.soccernow.business.entities.ChampionshipByPoints;
import pt.ul.fc.css.soccernow.business.entities.PodiumPosition;
import pt.ul.fc.css.soccernow.business.entities.Team;
import pt.ul.fc.css.soccernow.business.handlers.ChampionshipHandler;

@ActiveProfiles("test")
@WebMvcTest(ChampionshipController.class)
class ChampionshipControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private ChampionshipHandler championshipHandler;

  @Test
  void testGetAllChampionships() throws Exception {
    Championship championship = new Championship();
    championship.setId(1L);
    championship.setName("Test");
    List<Championship> championships = List.of(championship);

    when(championshipHandler.getAllChampionships()).thenReturn(championships);

    mockMvc
        .perform(get("/api/championships"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(1L));
  }

  @Test
  void testGetChampionshipById_found() throws Exception {
    Championship championship = new Championship();
    championship.setId(1L);
    championship.setName("Test");

    when(championshipHandler.getChampionshipById(1L)).thenReturn(championship);

    mockMvc
        .perform(get("/api/championships/id/{id}", 1L))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L));
  }

  @Test
  void testGetChampionshipById_notFound() throws Exception {
    when(championshipHandler.getChampionshipById(2L)).thenReturn(null);

    mockMvc.perform(get("/api/championships/id/{id}", 2L)).andExpect(status().isNotFound());
  }

  @Test
  void testGetChampionshipsByName_found() throws Exception {
    Championship championship = new Championship();
    championship.setId(1L);
    championship.setName("Test");
    List<Championship> championships = List.of(championship);

    when(championshipHandler.getChampionshipsByName("Test")).thenReturn(championships);

    mockMvc
        .perform(get("/api/championships/name/{name}", "Test"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].name").value("Test"));
  }

  @Test
  void testGetChampionshipsByName_notFound() throws Exception {
    when(championshipHandler.getChampionshipsByName("Unknown")).thenReturn(null);

    mockMvc
        .perform(get("/api/championships/name/{name}", "Unknown"))
        .andExpect(status().isNotFound());
  }

  @Test
  void testGetChampionshipsByFinishedGames_found() throws Exception {
    Championship championship = new Championship();
    championship.setId(1L);
    List<Championship> championships = List.of(championship);

    when(championshipHandler.getChampionshipByNumberOfFinishedGames(3)).thenReturn(championships);

    mockMvc
        .perform(get("/api/championships/finished/number/{number}", 3))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(1L));
  }

  @Test
  void testGetChampionshipsByFinishedGames_notFound() throws Exception {
    when(championshipHandler.getChampionshipByNumberOfFinishedGames(5)).thenReturn(null);

    mockMvc
        .perform(get("/api/championships/finished/number/{number}", 5))
        .andExpect(status().isNotFound());
  }

  @Test
  void testGetChampionshipsByUnfinishedGames_found() throws Exception {
    Championship championship = new Championship();
    championship.setId(2L);
    List<Championship> championships = List.of(championship);

    when(championshipHandler.getChampionshipByNumberOfUnfinishedGames(2)).thenReturn(championships);

    mockMvc
        .perform(get("/api/championships/unfinished/number/{number}", 2))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(2L));
  }

  @Test
  void testGetChampionshipsByUnfinishedGames_notFound() throws Exception {
    when(championshipHandler.getChampionshipByNumberOfUnfinishedGames(10)).thenReturn(null);

    mockMvc
        .perform(get("/api/championships/unfinished/number/{number}", 10))
        .andExpect(status().isNotFound());
  }

  @Test
  void testGetChampionshipsByTeamId_found() throws Exception {
    Championship championship = new Championship();
    championship.setId(1L);
    List<Championship> championships = List.of(championship);

    when(championshipHandler.getChampionshipsByTeamId(1L)).thenReturn(championships);

    mockMvc
        .perform(get("/api/championships/team/{teamId}", 1L))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(1L));
  }

  @Test
  void testGetChampionshipsByTeamId_notFound() throws Exception {
    when(championshipHandler.getChampionshipsByTeamId(99L)).thenReturn(null);

    mockMvc.perform(get("/api/championships/team/{teamId}", 99L)).andExpect(status().isNotFound());
  }

  @Test
  void testCreateChampionshipByPoints() throws Exception {
    ChampionshipRequestDTO dto = new ChampionshipRequestDTO();
    ChampionshipByPoints championship = new ChampionshipByPoints();
    championship.setId(1L);

    when(championshipHandler.createChampionshipByPoints(any(ChampionshipRequestDTO.class)))
        .thenReturn(championship);

    mockMvc
        .perform(
            post("/api/championships")
                .contentType("application/json")
                .content(new ObjectMapper().writeValueAsString(dto)))
        .andExpect(status().isCreated())
        .andExpect(header().string("Location", "/api/championshipsByPoints/1"));
  }

  @Test
  void testAddGameToChampionship_success() throws Exception {
    Long championshipId = 1L;
    GameRequestDTO gameDTO = new GameRequestDTO();

    when(championshipHandler.addGameToChampionship(eq(championshipId), any(GameRequestDTO.class)))
        .thenReturn(true);

    mockMvc
        .perform(
            post("/api/championships/{championshipId}/games/add", championshipId)
                .contentType("application/json")
                .content(new ObjectMapper().writeValueAsString(gameDTO)))
        .andExpect(status().isOk());
  }

  @Test
  void testAddGameToChampionship_failure() throws Exception {
    Long championshipId = 1L;
    GameRequestDTO gameDTO = new GameRequestDTO();

    when(championshipHandler.addGameToChampionship(eq(championshipId), any(GameRequestDTO.class)))
        .thenReturn(false);

    mockMvc
        .perform(
            post("/api/championships/{championshipId}/games/add", championshipId)
                .contentType("application/json")
                .content(new ObjectMapper().writeValueAsString(gameDTO)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void testAddTeamsToChampionship_success() throws Exception {
    Long championshipId = 1L;
    List<Long> teamIds = List.of(1L, 2L);

    when(championshipHandler.addTeamsToChampionship(eq(championshipId), anyList()))
        .thenReturn(true);

    mockMvc
        .perform(
            post("/api/championships/{championshipId}/teams/add", championshipId)
                .contentType("application/json")
                .content(new ObjectMapper().writeValueAsString(teamIds)))
        .andExpect(status().isOk());
  }

  @Test
  void testAddTeamsToChampionship_failure() throws Exception {
    Long championshipId = 1L;
    List<Long> teamIds = List.of(1L, 2L);

    when(championshipHandler.addTeamsToChampionship(eq(championshipId), anyList()))
        .thenReturn(false);

    mockMvc
        .perform(
            post("/api/championships/{championshipId}/teams/add", championshipId)
                .contentType("application/json")
                .content(new ObjectMapper().writeValueAsString(teamIds)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void testRemoveTeamFromChampionship_success() throws Exception {
    Long championshipId = 1L;
    Long teamId = 2L;

    when(championshipHandler.removeTeamFromChampionship(championshipId, teamId)).thenReturn(true);

    mockMvc
        .perform(
            post(
                "/api/championships/{championshipId}/teams/{teamId}/remove",
                championshipId,
                teamId))
        .andExpect(status().isOk());
  }

  @Test
  void testRemoveTeamFromChampionship_failure() throws Exception {
    Long championshipId = 1L;
    Long teamId = 2L;

    when(championshipHandler.removeTeamFromChampionship(championshipId, teamId)).thenReturn(false);

    mockMvc
        .perform(
            post(
                "/api/championships/{championshipId}/teams/{teamId}/remove",
                championshipId,
                teamId))
        .andExpect(status().isBadRequest());
  }

  @Test
  void testRemoveGameFromChampionship_success() throws Exception {
    Long championshipId = 1L;
    Long gameId = 3L;

    when(championshipHandler.removeGameFromChampionship(championshipId, gameId)).thenReturn(true);

    mockMvc
        .perform(
            post(
                "/api/championships/{championshipId}/games/{gameId}/remove",
                championshipId,
                gameId))
        .andExpect(status().isOk());
  }

  @Test
  void testRemoveGameFromChampionship_failure() throws Exception {
    Long championshipId = 1L;
    Long gameId = 3L;

    when(championshipHandler.removeGameFromChampionship(championshipId, gameId)).thenReturn(false);

    mockMvc
        .perform(
            post(
                "/api/championships/{championshipId}/games/{gameId}/remove",
                championshipId,
                gameId))
        .andExpect(status().isBadRequest());
  }

  @Test
  void testDeleteChampionship_success() throws Exception {
    Long championshipId = 1L;

    when(championshipHandler.deleteChampionship(championshipId)).thenReturn(true);

    mockMvc
        .perform(delete("/api/championships/{championshipId}/delete", championshipId))
        .andExpect(status().isOk());
  }

  @Test
  void testDeleteChampionship_failure() throws Exception {
    Long championshipId = 1L;

    when(championshipHandler.deleteChampionship(championshipId)).thenReturn(false);

    mockMvc
        .perform(delete("/api/championships/{championshipId}/delete", championshipId))
        .andExpect(status().isBadRequest());
  }

  @Test
  void testShowChampionshipPoints_found() throws Exception {
    Long championshipId = 1L;
    Map<Long, Integer> points = Map.of(1L, 10, 2L, 8);

    when(championshipHandler.getChampionshipPoints(championshipId)).thenReturn(points);

    mockMvc
        .perform(get("/api/championships/{championshipId}/points", championshipId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.1").value(10))
        .andExpect(jsonPath("$.2").value(8));
  }

  @Test
  void testShowChampionshipPoints_notFound() throws Exception {
    Long championshipId = 1L;

    when(championshipHandler.getChampionshipPoints(championshipId)).thenReturn(null);

    mockMvc
        .perform(get("/api/championships/{championshipId}/points", championshipId))
        .andExpect(status().isNotFound());
  }

  @Test
  void testFinishChampionship_success() throws Exception {
    Long championshipId = 1L;
    PodiumPosition pos1 = new PodiumPosition();
    pos1.setTeam(new Team());
    pos1.setChampionship(new Championship());
    PodiumPosition pos2 = new PodiumPosition();
    pos2.setTeam(new Team());
    pos2.setChampionship(new Championship());
    List<PodiumPosition> podium = List.of(pos1, pos2);

    when(championshipHandler.finishChampionship(championshipId)).thenReturn(true);
    when(championshipHandler.getChampionshipPodiumPositions(championshipId)).thenReturn(podium);

    mockMvc
        .perform(post("/api/championships/{championshipId}/finish", championshipId))
        .andExpect(status().isOk());
  }

  @Test
  void testFinishChampionship_notFound() throws Exception {
    Long championshipId = 1L;

    when(championshipHandler.finishChampionship(championshipId)).thenReturn(true);
    when(championshipHandler.getChampionshipPodiumPositions(championshipId)).thenReturn(null);

    mockMvc
        .perform(post("/api/championships/{championshipId}/finish", championshipId))
        .andExpect(status().isNotFound());
  }

  @Test
  void testFinishChampionship_badRequest() throws Exception {
    Long championshipId = 1L;

    when(championshipHandler.finishChampionship(championshipId)).thenReturn(false);

    mockMvc
        .perform(post("/api/championships/{championshipId}/finish", championshipId))
        .andExpect(status().isBadRequest());
  }
}
