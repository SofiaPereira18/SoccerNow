package pt.ul.fc.css.soccernow.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
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
import pt.ul.fc.css.soccernow.dtos.PlayerDTO;
import pt.ul.fc.css.soccernow.dtos.StartingTeamDTO;
import pt.ul.fc.css.soccernow.dtos.StartingTeamRequestDTO;
import pt.ul.fc.css.soccernow.dtos.TableStartingTeamDTO;
import pt.ul.fc.css.soccernow.dtos.TeamDTO;

public class TeamUpdateController {

  @FXML private Label teamNameTitle;

  @FXML private TextField idJogadorTextField;
  @FXML private TextField idJogadorRemove;
  @FXML private TextField idstRemove;
  @FXML private TableView<PlayerDTO> playersTable;
  @FXML private TableColumn<PlayerDTO, Long> idColumn;
  @FXML private TableColumn<PlayerDTO, String> nameColumn;
  @FXML private TableColumn<PlayerDTO, String> positionColumn;
  @FXML private TableColumn<PlayerDTO, String> stColumn;

  @FXML private TextField player1IdTF;
  @FXML private TextField player2IdTF;
  @FXML private TextField player3IdTF;
  @FXML private TextField player4IdTF;
  @FXML private TextField gkIdTF;
  @FXML private TableView<TableStartingTeamDTO> stTable;
  @FXML private TableColumn<TableStartingTeamDTO, Long> idstColumn;
  @FXML private TableColumn<TableStartingTeamDTO, String> playerColumn;
  @FXML private TableColumn<TableStartingTeamDTO, String> gkColumn;
  @FXML private TableColumn<TableStartingTeamDTO, String> gamesColumn;

  private TeamDTO teamToUpdate;
  private List<PlayerDTO> players;
  private List<TableStartingTeamDTO> startingTeams;

  private static final String VIEWS = "/views";
  private final RestTemplate restTemplate = new RestTemplate();
  private final String BASE_URL = "http://localhost:8080/api";

  @FXML
  public void initialize() {
    updatePlayerTable();
    updateSTTable();
  }

  private void updateSTTable() {
    idstColumn.setCellValueFactory(
        cellData -> new SimpleLongProperty(cellData.getValue().getId()).asObject());
    idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
    // show player on teamTable
    playerColumn.setCellFactory(
        column ->
            new TableCell<TableStartingTeamDTO, String>() {
              @Override
              protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                  setText(null);
                } else {
                  setText(item);
                }
              }
            });
    playerColumn.setCellValueFactory(
        cellData ->
            new SimpleStringProperty(
                cellData.getValue().getPlayersIds() != null
                        && !cellData.getValue().getPlayersIds().isEmpty()
                    ? String.join("\n", cellData.getValue().getPlayersIds())
                    : "-"));
    gkColumn.setCellValueFactory(
        cellData -> new SimpleStringProperty(cellData.getValue().getGoalkeeperId()));
    gamesColumn.setCellValueFactory(
        cellData ->
            new SimpleStringProperty(
                cellData.getValue().getGamesId() != null
                        && !cellData.getValue().getGamesId().isEmpty()
                    ? cellData.getValue().getGamesId().stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(", "))
                    : "-"));
  }

  private void updatePlayerTable() {
    // Liga a coluna ao campo id do PlayerDTO
    idColumn.setCellValueFactory(
        cellData -> new SimpleLongProperty(cellData.getValue().getId()).asObject());
    nameColumn.setCellValueFactory(
        cellData ->
            new SimpleStringProperty(
                cellData.getValue().getFirstName() + " " + cellData.getValue().getLastName()));
    positionColumn.setCellValueFactory(
        cellData -> new SimpleStringProperty(cellData.getValue().getPosition()));
    stColumn.setCellValueFactory(
        cellData ->
            new SimpleStringProperty(
                cellData.getValue().getStartingTeams() != null
                        && !cellData.getValue().getStartingTeams().isEmpty()
                    ? cellData.getValue().getStartingTeams().stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(", "))
                    : "-"));
  }

  public void setTeamToUpdate(TeamDTO team) {
    teamNameTitle.setText("Update Team: " + team.getName());
    this.teamToUpdate = team;
    players = getPlayersFromTeam(team.getId());
    startingTeams = getSTFromTeam(team.getId());
    playersTable.getItems().setAll(players);
    stTable.getItems().setAll(startingTeams);
  }

  private List<TableStartingTeamDTO> getSTFromTeam(Long id) {
    List<TableStartingTeamDTO> startingTeams = new ArrayList<>();
    if (id != null) {
      try {
        ResponseEntity<List<StartingTeamDTO>> response =
            restTemplate.exchange(
                BASE_URL + "/team/" + id + "/starting-team",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<StartingTeamDTO>>() {});
        if (response.getStatusCode() == HttpStatus.OK) {
          List<StartingTeamDTO> stDtos = response.getBody();
          if (stDtos == null || stDtos.isEmpty()) {
            return startingTeams;
          }
          for (StartingTeamDTO dto : stDtos) {
            startingTeams.add(getStTable(dto));
          }
        }
      } catch (Exception e) {
        showAlert(AlertType.ERROR, "Erro", "Erro ao encontrar StartingTeam: " + e.getMessage());
      }
    }

    return startingTeams;
  }

  private TableStartingTeamDTO getStTable(StartingTeamDTO sTDto) {
    String teamName = teamToUpdate.getName() + ": " + teamToUpdate.getId();
    List<String> playersStrings = getPlayerStrings(sTDto.getPlayersIds());
    String gkString = getPlayer(sTDto.getGoalkeeperId());

    return new TableStartingTeamDTO(
        sTDto.getId(), teamName, playersStrings, gkString, sTDto.getGamesId());
  }

  private List<String> getPlayerStrings(List<Long> playersId) {
    List<String> playerStrings = new ArrayList<>();
    if (playersId != null && !playersId.isEmpty()) {
      for (Long id : playersId) {
        String pString = getPlayer(id);
        playerStrings.add(pString);
      }
    }

    return playerStrings;
  }

  private String getPlayer(Long id) {
    try {
      ResponseEntity<PlayerDTO> response =
          restTemplate.exchange(BASE_URL + "/player/" + id, HttpMethod.GET, null, PlayerDTO.class);
      if (response.getStatusCode() == HttpStatus.OK) {
        PlayerDTO playerDTO = response.getBody();
        if (playerDTO != null) {
          return playerDTO.getLastName() + " " + playerDTO.getLastName() + ": " + id;

        } else {
          showAlert(AlertType.ERROR, "Erro", "Player não encontrado." + id);
        }
      } else {
        showAlert(AlertType.ERROR, "Erro", "Erro ao encontrar Player." + id);
      }
      ;
    } catch (Exception e) {
      showAlert(AlertType.ERROR, "Erro", "Erro ao encontrar Player: " + e.getMessage());
    }
    return null;
  }

  private List<PlayerDTO> getPlayersFromTeam(Long team) {
    List<PlayerDTO> players = new ArrayList<>();
    if (team != null) {
      try {
        ResponseEntity<List<PlayerDTO>> response =
            restTemplate.exchange(
                BASE_URL + "/team/" + team + "/players",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<PlayerDTO>>() {});
        if (response.getStatusCode().is2xxSuccessful()) {
          players = response.getBody();
        } else {
          System.out.println("Failed to retrieve players: " + response.getStatusCode());
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    return players;
  }

  @FXML
  private void addPlayer() {
    String id = idJogadorTextField.getText();
    if (id != null && !id.isEmpty()) {
      try {
        ResponseEntity<TeamDTO> response =
            restTemplate.exchange(
                BASE_URL + "/team/" + teamToUpdate.getId() + "/" + id,
                HttpMethod.POST,
                null,
                TeamDTO.class);
        if (response.getStatusCode() == HttpStatus.OK) {
          TeamDTO updatedTeam = response.getBody();
          if (updatedTeam != null) {
            teamToUpdate = updatedTeam;
            players = getPlayersFromTeam(teamToUpdate.getId());
            playersTable.getItems().setAll(players);
            idJogadorTextField.clear();
          } else {
            System.out.println("Failed to update team: No body returned");
          }
        } else {
          System.out.println("Failed to add player: " + response.getStatusCode());
        }
      } catch (Exception e) {
        System.out.println("An error occurred while adding the player: " + e.getMessage());
      }
    }
  }

  private TeamDTO getTeam(String id) {
    try {
      ResponseEntity<TeamDTO> response =
          restTemplate.exchange(BASE_URL + "/team/id/" + id, HttpMethod.GET, null, TeamDTO.class);
      if (response.getStatusCode() == HttpStatus.OK) {
        return response.getBody();
      } else {
        showAlert(AlertType.ERROR, "Erro", "Erro ao encontrar Team.");
      }

    } catch (Exception e) {
      showAlert(AlertType.ERROR, "Erro", "Erro ao encontrar Team: " + e.getMessage());
    }
    return null;
  }

  @FXML
  private void removePlayer() {
    String id = idJogadorRemove.getText();
    if (id != null && !id.isEmpty()) {
      try {
        ResponseEntity<Boolean> response =
            restTemplate.exchange(
                BASE_URL + "/team/" + teamToUpdate.getId() + "/player/" + id,
                HttpMethod.DELETE,
                null,
                Boolean.class);
        if (response.getStatusCode() == HttpStatus.OK && Boolean.TRUE.equals(response.getBody())) {
          teamToUpdate = getTeam("" + teamToUpdate.getId());
          players = getPlayersFromTeam(teamToUpdate.getId());
          playersTable.getItems().setAll(players);
          idJogadorTextField.clear();
        } else {
          showAlert(AlertType.ERROR, "Erro", "Erro ao remover player.");
        }
      } catch (Exception e) {
        showAlert(AlertType.ERROR, "Erro", "Erro ao remover Player: " + e.getMessage());
      }
    }
  }

  @FXML
  private void addST() {
    List<Long> players = validateTextFields();
    if (players != null && !players.isEmpty()) {
      Long gk = players.get(4);
      players.remove(4);
      StartingTeamRequestDTO requestDTO = new StartingTeamRequestDTO(players, gk);
      HttpEntity<StartingTeamRequestDTO> request = new HttpEntity<>(requestDTO);
      try {
        ResponseEntity<StartingTeamDTO> response =
            restTemplate.exchange(
                BASE_URL + "/team/" + teamToUpdate.getId() + "/starting-team/players",
                HttpMethod.POST,
                request,
                StartingTeamDTO.class);
        if (response.getStatusCode() == HttpStatus.OK) {
          teamToUpdate = getTeam("" + teamToUpdate.getId());
          setTeamToUpdate(teamToUpdate);
        }
      } catch (Exception e) {
        showAlert(AlertType.ERROR, "Erro", "Erro ao criar uma StartingTeam: " + e.getMessage());
      }
    }
  }

  private List<Long> validateTextFields() {
    List<Long> players = new ArrayList<>();
    String player1Id = player1IdTF.getText();
    String player2Id = player2IdTF.getText();
    String player3Id = player3IdTF.getText();
    String player4Id = player4IdTF.getText();
    String gkId = gkIdTF.getText();
    if (player1Id != null
        && !player1Id.isEmpty()
        && player2Id != null
        && !player2Id.isEmpty()
        && player3Id != null
        && !player3Id.isEmpty()
        && player4Id != null
        && !player4Id.isEmpty()
        && gkId != null
        && !gkId.isEmpty()) {
      if (!player1Id.equals(player2Id)
          && !player1Id.equals(player3Id)
          && !player1Id.equals(player4Id)
          && !player1Id.equals(gkId)
          && !player2Id.equals(player3Id)
          && !player2Id.equals(player4Id)
          && !player2Id.equals(gkId)
          && !player3Id.equals(player4Id)
          && !player3Id.equals(gkId)
          && !player4Id.equals(gkId)) {
        players.add(Long.parseLong(player1Id));
        players.add(Long.parseLong(player2Id));
        players.add(Long.parseLong(player3Id));
        players.add(Long.parseLong(player4Id));
        players.add(Long.parseLong(gkId));
      } else {
        showAlert(AlertType.ERROR, "Erro", "Não pode repetir jogadores.");
      }
    } else {
      showAlert(AlertType.ERROR, "Erro", "Tem de preencher todos os campos.");
    }

    return players;
  }

  @FXML
  private void removeST() {
    String id = idstRemove.getText();
    if (id != null && !id.isEmpty()) {
      try {
        ResponseEntity<Boolean> response =
            restTemplate.exchange(
                BASE_URL + "/team/" + teamToUpdate.getId() + "/StartingTeam/" + id,
                HttpMethod.DELETE,
                null,
                Boolean.class);
        if (response.getStatusCode() == HttpStatus.OK) {
          teamToUpdate = getTeam("" + teamToUpdate.getId());
          setTeamToUpdate(teamToUpdate);
        }
      } catch (Exception e) {
        showAlert(AlertType.ERROR, "Erro", "Erro ao remover uma StartingTeam: " + e.getMessage());
      }
    }
  }

  /**
   * Navigates back to the home screen.
   *
   * @param event The action event triggering the navigation
   * @throws IOException if the FXML file cannot be loaded
   */
  @FXML
  private void goBack(ActionEvent event) throws IOException {
    loadScene(event, "/team.fxml");
  }

  /**
   * Loads a new scene in the current window with responsive sizing. The scene will be sized to 85%
   * of the screen dimensions and centered.
   *
   * @param event The source event containing window reference
   * @param file Path to the target FXML file
   * @throws IOException if the FXML file cannot be loaded
   */
  private void loadScene(ActionEvent event, String file) throws IOException {
    Parent homeRoot = FXMLLoader.load(getClass().getResource(VIEWS + file));
    Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

    javafx.geometry.Rectangle2D screenBounds = javafx.stage.Screen.getPrimary().getVisualBounds();

    double width = screenBounds.getWidth() * 0.85;
    double height = screenBounds.getHeight() * 0.85;

    Scene scene = new Scene(homeRoot, width, height);

    currentStage.setScene(scene);
    currentStage.centerOnScreen();
    currentStage.show();
  }

  /**
   * Displays an alert dialog to the user.
   *
   * @param type The type of alert (ERROR, INFORMATION, etc.)
   * @param title The title of the alert dialog
   * @param message The content message to display
   */
  private void showAlert(AlertType type, String title, String message) {
    Alert alert = new Alert(type);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }
}
