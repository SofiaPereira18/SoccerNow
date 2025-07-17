package pt.ul.fc.css.soccernow.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import pt.ul.fc.css.soccernow.dtos.ChampionshipByPointsDTO;
import pt.ul.fc.css.soccernow.dtos.ChampionshipDTO;
import pt.ul.fc.css.soccernow.dtos.ChampionshipRequestDTO;
import pt.ul.fc.css.soccernow.dtos.GameDTO;
import pt.ul.fc.css.soccernow.dtos.GameRequestDTO;
import pt.ul.fc.css.soccernow.dtos.PodiumPositionDTO;
import pt.ul.fc.css.soccernow.dtos.TableChampionshipDTO;
import pt.ul.fc.css.soccernow.dtos.TeamDTO;

public class ChampionshipController {

  private static final String VIEWS = "/views";
  private final RestTemplate restTemplate = new RestTemplate();
  private final String BASE_URL = "http://localhost:8080/api";

  @FXML private TextField championshipName;
  @FXML private TextField teamsChampionship;
  @FXML private Button backToHomeMenuButton;

  @FXML private TextField championshipId;
  @FXML private TextField newTeams;
  @FXML private TextField deletedTeams;
  @FXML private TextField team1Name;
  @FXML private TextField team2Name;
  @FXML private TextField refereeTeamId;
  @FXML private DatePicker gameDate;
  @FXML private TextField gameTime;
  @FXML private TextField gameIdToRemove;

  @FXML private TableView<TableChampionshipDTO> championshipTable;
  @FXML private TableColumn<TableChampionshipDTO, Long> idColumn;
  @FXML private TableColumn<TableChampionshipDTO, String> nameColumn;
  @FXML private TableColumn<TableChampionshipDTO, String> teamsColumn;
  @FXML private TableColumn<TableChampionshipDTO, String> gamesColumn;
  @FXML private TableColumn<TableChampionshipDTO, String> statusColumn;
  @FXML private TableColumn<TableChampionshipDTO, String> podiumColumn;

  @FXML
  public void initialize() {

    // Set up columns
    idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
    nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    teamsColumn.setCellValueFactory(new PropertyValueFactory<>("teamsNamesString"));
    gamesColumn.setCellValueFactory(new PropertyValueFactory<>("gamesInfoString"));
    statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    podiumColumn.setCellValueFactory(new PropertyValueFactory<>("podiumPositionsString"));

    // Fetch and map data as before
    ResponseEntity<List<ChampionshipDTO>> response =
        restTemplate.exchange(
            BASE_URL + "/championships",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<ChampionshipDTO>>() {});

    if (!response.getStatusCode().is2xxSuccessful()) {
      System.err.println("Failed to fetch championships: " + response.getStatusCode());
      return;
    }

    List<ChampionshipDTO> championships = response.getBody();

    if (championships == null || championships.isEmpty()) {
      System.out.println("No championships found.");
      return;
    }

    List<TableChampionshipDTO> tableChampionships =
        championships.stream()
            .map(
                championship -> {
                  List<String> teamNames =
                      championship.getTeamsIds().stream().map(this::getName).toList();

                  DateTimeFormatter dateTimeFormatter =
                      DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
                  List<String> gamesInfo =
                      championship.getGamesIds().stream()
                          .map(this::getGame)
                          .filter(Objects::nonNull)
                          .map(
                              game -> {
                                List<String> gameTeams =
                                    game.getTeamsIds().stream().map(this::getName).toList();

                                return "ID: "
                                    + game.getId()
                                    + " | "
                                    + String.join(" vs ", gameTeams)
                                    + " ("
                                    + game.getDateTime().format(dateTimeFormatter)
                                    + ")";
                              })
                          .toList();

                  List<String> podiumTeamNames =
                      championship.getPodiumPositionsIds().stream().map(this::getName).toList();

                  String status = getStatusText(championship);

                  return new TableChampionshipDTO(
                      championship.getId(),
                      championship.getName(),
                      podiumTeamNames,
                      gamesInfo,
                      teamNames,
                      status);
                })
            .toList();

    ObservableList<TableChampionshipDTO> championshipData =
        FXCollections.observableArrayList(tableChampionships);

    championshipTable.setItems(championshipData);
  }

  @FXML
  private void backToHomeMenu() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource(VIEWS + "/home.fxml"));
      Parent root = loader.load();
      Stage stage = (Stage) backToHomeMenuButton.getScene().getWindow();
      Scene currentScene = stage.getScene();
      Scene newScene = new Scene(root, currentScene.getWidth(), currentScene.getHeight());
      stage.setScene(newScene);
      stage.show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void createChampionship() {

    String name = championshipName.getText();
    if (name == null || name.isEmpty()) {
      showAlert(AlertType.ERROR, "Campos Obrigatórios", "Preencha o nome do campeonato.");
      return;
    }

    String teams = teamsChampionship.getText();
    List<Long> teamsIds = new ArrayList<>();
    System.out.println("Teams: " + teams);
    if (teams != null) {
      String[] teamNamesArray = teams.split(",");
      System.out.println("Team Names Array: " + teamNamesArray.length);
      for (String teamName : teamNamesArray) {
        if (!teamName.isEmpty()) {
          try {
            ResponseEntity<TeamDTO> response =
                restTemplate.exchange(
                    BASE_URL + "/team/name/" + teamName, HttpMethod.GET, null, TeamDTO.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
              TeamDTO team = response.getBody();
              if (team != null) {
                teamsIds.add(team.getId());
              } else {
                showAlert(AlertType.ERROR, "Erro", "Equipa'" + teamName + "' não encontrada.");
                return;
              }
            } else {
              showAlert(AlertType.ERROR, "Erro", "Equipa'" + teamName + "' não encontrada.");
              return;
            }
          } catch (Exception e) {
            showAlert(AlertType.ERROR, "Erro", "Erro ao buscar equipa: " + e.getMessage());
            return;
          }
        }
      }
    }

    ChampionshipRequestDTO championshipDTO = new ChampionshipRequestDTO();
    championshipDTO.setName(name);

    if (teams != null) {
      championshipDTO.setTeams(teamsIds);
    }

    HttpEntity<ChampionshipRequestDTO> request = new HttpEntity<>(championshipDTO);

    ResponseEntity<ChampionshipByPointsDTO> response =
        restTemplate.exchange(
            BASE_URL + "/championships", HttpMethod.POST, request, ChampionshipByPointsDTO.class);

    if (response.getStatusCode() == HttpStatus.CREATED) {
      ChampionshipByPointsDTO responseBody = response.getBody();
      if (responseBody != null) {
        showAlert(AlertType.INFORMATION, "Sucesso", "Campeonato criado:" + responseBody.getName());
      } else {
        showAlert(AlertType.INFORMATION, "Sucesso", "Campeonato criado com sucesso.");
      }
    } else {
      showAlert(AlertType.ERROR, "Erro", "Campeonato já existe ou dados inválidos.");
    }

    championshipName.clear();
    refreshChampionshipsList();
  }

  @FXML
  private void handleAddTeams() {
    Long id = parseChampionshipId();
    if (id == null) return;

    String newTeamsInput = newTeams.getText();
    if (newTeamsInput == null || newTeamsInput.trim().isEmpty()) {
      showAlert(AlertType.ERROR, "Erro", "Nenhum nome de equipa válido foi fornecido.");
      return;
    }
    List<Long> teamIds = getTeamsIds(newTeamsInput.split(","));
    if (teamIds.isEmpty()) {
      showAlert(AlertType.ERROR, "Erro", "Nenhum nome de equipa válido foi fornecido.");
      return;
    }
    if (!addTeamsToChampionship(id, teamIds)) {
      showAlert(AlertType.ERROR, "Erro", "Erro ao adicionar equipas ao campeonato.");
      return;
    }
    showAlert(AlertType.INFORMATION, "Sucesso", "Equipas adicionadas com sucesso!");
    newTeams.clear();
    championshipId.clear();
    refreshChampionshipsList();
  }

  @FXML
  private void handleRemoveTeams() {
    Long id = parseChampionshipId();
    if (id == null) return;

    String delTeamsInput = deletedTeams.getText();
    if (delTeamsInput == null || delTeamsInput.trim().isEmpty()) {
      showAlert(
          AlertType.ERROR, "Erro", "Nenhum nome de equipa válido foi fornecido para remoção.");
      return;
    }
    List<Long> teamIds = getTeamsIds(delTeamsInput.split(","));
    if (teamIds.isEmpty()) {
      showAlert(
          AlertType.ERROR, "Erro", "Nenhum nome de equipa válido foi fornecido para remoção.");
      return;
    }
    for (Long teamId : teamIds) {
      if (!deletedTeams(id, teamId)) {
        showAlert(AlertType.ERROR, "Erro", "Erro ao remover equipas do campeonato.");
        return;
      }
    }
    showAlert(AlertType.INFORMATION, "Sucesso", "Equipas removidas com sucesso!");
    deletedTeams.clear();
    championshipId.clear();
    refreshChampionshipsList();
  }

  @FXML
  private void handleAddGame() {
    Long id = parseChampionshipId();
    if (id == null) return;

    String team1 = team1Name.getText();
    String team2 = team2Name.getText();
    String refereeText = refereeTeamId.getText();
    LocalDate date = gameDate.getValue();
    String timeStr = gameTime.getText();

    if (team1 == null
        || team2 == null
        || refereeText == null
        || team1.trim().isEmpty()
        || team2.trim().isEmpty()
        || refereeText.trim().isEmpty()
        || date == null
        || timeStr == null
        || timeStr.trim().isEmpty()) {
      showAlert(AlertType.ERROR, "Erro", "Preencha todos os campos do jogo.");
      return;
    }

    Long refereeTeam = Long.parseLong(refereeText.trim());
    List<Long> teamIds = getTeamsIds(new String[] {team1, team2});
    if (teamIds.size() != 2) {
      showAlert(AlertType.ERROR, "Erro", "Por favor, forneça dois nomes de equipas válidos.");
      return;
    }
    LocalTime time = parseTime(timeStr);
    if (time == null) {
      showAlert(AlertType.ERROR, "Erro", "Hora inválida.");
      return;
    }
    LocalDateTime dateTime = LocalDateTime.of(date, time);
    GameRequestDTO gameRequest = new GameRequestDTO();
    gameRequest.setDateTime(dateTime);
    gameRequest.setTeamsIds(teamIds);
    gameRequest.setCity("Default City");
    gameRequest.setStadium("Default Stadium");
    gameRequest.setRefereeTeamId(refereeTeam);

    if (!createGame(id, gameRequest)) {
      showAlert(AlertType.ERROR, "Erro", "Erro ao criar partida no campeonato.");
      return;
    }
    showAlert(AlertType.INFORMATION, "Sucesso", "Jogo adicionado com sucesso!");
    team1Name.clear();
    team2Name.clear();
    refereeTeamId.clear();
    gameDate.setValue(null);
    gameTime.clear();
    championshipId.clear();
    refreshChampionshipsList();
  }

  @FXML
  private void handleRemoveGame() {
    Long id = parseChampionshipId();
    if (id == null) return;

    String gameIdText = gameIdToRemove.getText();
    if (gameIdText == null || gameIdText.trim().isEmpty()) {
      showAlert(AlertType.ERROR, "Erro", "Id do jogo não pode ser vazio.");
      return;
    }
    Long gameId = Long.parseLong(gameIdText.trim());
    if (!removeGame(id, gameId)) {
      showAlert(AlertType.ERROR, "Erro", "Erro ao remover partida do campeonato.");
      return;
    }
    showAlert(AlertType.INFORMATION, "Sucesso", "Jogo removido com sucesso!");
    gameIdToRemove.clear();
    championshipId.clear();
    refreshChampionshipsList();
  }

  @FXML
  public void handleFinishChampionship() {
    Long id = parseChampionshipId();
    if (id == null) return;

    ResponseEntity<PodiumPositionDTO[]> response =
        restTemplate.exchange(
            BASE_URL + "/championships/" + id + "/finish",
            HttpMethod.POST,
            null,
            PodiumPositionDTO[].class);

    PodiumPositionDTO[] podium = response.getBody();
    if (response.getStatusCode().is2xxSuccessful() && podium != null) {
      showAlert(AlertType.INFORMATION, "Sucesso", "Campeonato finalizado com sucesso!");
    } else {
      showAlert(AlertType.ERROR, "Erro", "Erro ao finalizar o campeonato.");
    }
    championshipId.clear();
    refreshChampionshipsList();
  }

  private Long parseChampionshipId() {
    String idText = championshipId.getText();
    if (idText == null || idText.trim().isEmpty()) {
      showAlert(AlertType.ERROR, "Erro", "ID do campeonato não pode ser nulo.");
      return null;
    }
    return Long.parseLong(idText.trim());
  }

  private Boolean addTeamsToChampionship(Long championshipId, List<Long> teamIds) {
    HttpEntity<List<Long>> requestEntity = new HttpEntity<>(teamIds);
    ResponseEntity<Boolean> response =
        restTemplate.exchange(
            BASE_URL + "/championships/" + championshipId + "/teams/add",
            HttpMethod.POST,
            requestEntity,
            Boolean.class);

    if (response.getStatusCode().is2xxSuccessful()) {
      return true;
    }
    return false;
  }

  private Boolean deletedTeams(Long championshipId, long teamId) {
    ResponseEntity<Boolean> response =
        restTemplate.exchange(
            BASE_URL + "/championships/" + championshipId + "/teams/" + teamId + "/remove",
            HttpMethod.POST,
            null,
            Boolean.class);

    if (response.getStatusCode().is2xxSuccessful()) {
      return true;
    }
    return false;
  }

  private Boolean createGame(Long championshipId, GameRequestDTO gameRequest) {
    HttpEntity<GameRequestDTO> requestEntity = new HttpEntity<>(gameRequest);
    ResponseEntity<Boolean> response =
        restTemplate.exchange(
            BASE_URL + "/championships/" + championshipId + "/games/add",
            HttpMethod.POST,
            requestEntity,
            Boolean.class);

    if (response.getStatusCode().is2xxSuccessful()) {
      return true;
    }
    return false;
  }

  private Boolean removeGame(Long championshipId, Long gameId) {
    ResponseEntity<Boolean> response =
        restTemplate.exchange(
            BASE_URL + "/championships/" + championshipId + "/games/" + gameId + "/remove",
            HttpMethod.POST,
            null,
            Boolean.class);

    if (response.getStatusCode().is2xxSuccessful()) {
      return true;
    }
    return false;
  }

  @FXML
  private void deleteChampionship() {
    Long id = parseChampionshipId();
    if (id == null) return;

    ResponseEntity<Void> response =
        restTemplate.exchange(
            BASE_URL + "/championships/" + id + "/delete", HttpMethod.DELETE, null, Void.class);

    if (response.getStatusCode() == HttpStatus.OK) {
      showAlert(AlertType.INFORMATION, "Sucesso", "Campeonato removido com sucesso!");
    } else {
      showAlert(AlertType.ERROR, "Erro", "Erro ao remover campeonato.");
    }
    championshipId.clear();
    refreshChampionshipsList();
  }

  private List<Long> getTeamsIds(String[] teamNamesArray) {
    List<Long> ids = new java.util.ArrayList<>();
    for (String name : teamNamesArray) {
      String trimmedName = name.trim();
      if (!trimmedName.isEmpty()) {
        try {
          ResponseEntity<pt.ul.fc.css.soccernow.dtos.TeamDTO> response =
              restTemplate.exchange(
                  BASE_URL + "/team/name/" + trimmedName, HttpMethod.GET, null, TeamDTO.class);
          TeamDTO team = response.getBody();
          if (team != null && team.getId() != 0) {
            ids.add(team.getId());
          }
        } catch (Exception e) {
        }
      }
    }
    return ids;
  }

  private LocalTime parseTime(String timeStr) {
    if (timeStr == null || timeStr.trim().isEmpty()) return null;
    try {
      return LocalTime.parse(timeStr.trim(), DateTimeFormatter.ofPattern("HH:mm"));
    } catch (Exception e) {
      showAlert(AlertType.ERROR, "Erro", "Hora inválida. Use o formato HH:mm.");
      return null;
    }
  }

  private String getName(Long id) {
    ResponseEntity<TeamDTO> response =
        restTemplate.exchange(BASE_URL + "/team/id/" + id, HttpMethod.GET, null, TeamDTO.class);
    if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
      System.err.println("Failed to fetch team with id " + id);
      return "Unknown Team";
    }
    TeamDTO team = response.getBody();
    return (team != null) ? team.getName() : "Unknown Team";
  }

  private GameDTO getGame(Long id) {
    try {
      ResponseEntity<GameDTO> response =
          restTemplate.exchange(BASE_URL + "/games/" + id, HttpMethod.GET, null, GameDTO.class);
      if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
        System.err.println("Failed to fetch game with id " + id);
        return null;
      }
      return response.getBody();
    } catch (org.springframework.web.client.HttpClientErrorException.NotFound e) {
      System.err.println("Game not found with id " + id);
      return null;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  private String getStatusText(ChampionshipDTO championship) {
    return championship.isFinished()
        ? "Finalizado"
        : championship.getGamesIds().isEmpty() ? "Sem jogos" : "Em andamento";
  }

  @FXML
  public void refreshChampionshipsList() {
    championshipTable.getItems().clear();
    initialize();
  }

  private void showAlert(AlertType alertType, String title, String message) {
    javafx.scene.control.Alert alert = new javafx.scene.control.Alert(alertType);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }
}
