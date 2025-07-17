package pt.ul.fc.css.soccernow.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import pt.ul.fc.css.soccernow.dtos.ChampionshipDTO;
import pt.ul.fc.css.soccernow.dtos.GameDTO;
import pt.ul.fc.css.soccernow.dtos.PlayerDTO;
import pt.ul.fc.css.soccernow.dtos.PodiumPositionDTO;
import pt.ul.fc.css.soccernow.dtos.StartingTeamDTO;
import pt.ul.fc.css.soccernow.dtos.TableStartingTeamDTO;
import pt.ul.fc.css.soccernow.dtos.TableTeamDTO;
import pt.ul.fc.css.soccernow.dtos.TeamDTO;

public class TeamController {

  private static final String VIEWS = "/views";
  private final RestTemplate restTemplate = new RestTemplate();
  private final String BASE_URL = "http://localhost:8080/api";

  private TeamDTO teamDTO;

  // FXML components da Team
  @FXML private TextField nameFieldView;
  @FXML private TextField idFieldView;
  @FXML private Button updateButton;
  @FXML private Button removeButton;
  @FXML private TableView<TableTeamDTO> teamDataTable;
  @FXML private TableColumn<TableTeamDTO, Long> idColumn;
  @FXML private TableColumn<TableTeamDTO, String> teamNameColumn;
  @FXML private TableColumn<TableTeamDTO, String> playersColumn;
  @FXML private TableColumn<TableTeamDTO, String> stColumn;
  @FXML private TableColumn<TableTeamDTO, String> champColumn;
  @FXML private TableColumn<TableTeamDTO, String> ppColumn;
  @FXML private TableColumn<TableTeamDTO, String> gamesTColumn;

  // FXML components da StartingTeam
  @FXML private TextField idSTFieldView;
  @FXML private TableView<TableStartingTeamDTO> stDataTable;
  @FXML private TableColumn<TableStartingTeamDTO, Long> idSTColumn;
  @FXML private TableColumn<TableStartingTeamDTO, String> teamIdColumn;
  @FXML private TableColumn<TableStartingTeamDTO, String> playersStColumn;
  @FXML private TableColumn<TableStartingTeamDTO, String> gkColumn;
  @FXML private TableColumn<TableStartingTeamDTO, String> gamesColumn;

  @FXML
  public void initialize() {
    updateButton.setVisible(false);
    removeButton.setVisible(false);
  }

  @FXML
  private void verifyTeam() {
    String id = idFieldView.getText();
    if (id != null && !id.isEmpty()) {
      teamDTO = getTeam(id);
      TableTeamDTO tableDTO = getTableTeamDTO(teamDTO);
      if (tableDTO != null) {
        updateTeamTable(tableDTO);
      } else {
        showAlert(AlertType.ERROR, "Erro", "Team não encontrado.");
      }
    }
  }

  @FXML
  private void createTeam() {
    String name = nameFieldView.getText();
    if (name != null) {
      try {
        ResponseEntity<TeamDTO> response =
            restTemplate.exchange(
                BASE_URL + "/team/name/" + name, HttpMethod.POST, null, TeamDTO.class);
        if (response.getStatusCode() == HttpStatus.CREATED) {
          TeamDTO team = response.getBody();
          if (team != null) {
            this.teamDTO = team;
            TableTeamDTO tableDTO = getTableTeamDTO(team);
            updateTeamTable(tableDTO);
            showAlert(AlertType.INFORMATION, "Sucesso", "Team criada com sucesso.");
          }
        } else {
          showAlert(AlertType.ERROR, "Erro", "Erro ao criar Team.");
        }
      } catch (Exception e) {
        showAlert(AlertType.ERROR, "Erro", "Erro ao criar Team: " + e.getMessage());
      }
    }
  }

  @FXML
  private void verifyST() {
    String id = idSTFieldView.getText();
    if (id != null) {
      try {
        ResponseEntity<StartingTeamDTO> response =
            restTemplate.exchange(
                BASE_URL + "/team/starting-team/id/" + id,
                HttpMethod.GET,
                null,
                StartingTeamDTO.class);
        if (response.getStatusCode() == HttpStatus.OK) {
          StartingTeamDTO sTDto = response.getBody();
          if (sTDto != null) {
            TableStartingTeamDTO stTable = getStTable(sTDto);
            updateSTTable(stTable);
          }
        } else {
          showAlert(AlertType.ERROR, "Erro", "Erro ao encontrar StartingTeam.");
        }

      } catch (Exception e) {
        showAlert(AlertType.ERROR, "Erro", "Erro ao encontrar StartingTeam: " + e.getMessage());
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

  private void updateTeamTable(TableTeamDTO tableDTO) {
    // Populate the table with the team data
    idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
    teamNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    // show player on teamTable
    playersColumn.setCellFactory(
        column ->
            new TableCell<TableTeamDTO, String>() {
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
    playersColumn.setCellValueFactory(
        cellData ->
            new SimpleStringProperty(
                cellData.getValue().getPlayersId() != null
                        && !cellData.getValue().getPlayersId().isEmpty()
                    ? String.join("\n", cellData.getValue().getPlayersId())
                    : "-"));

    stColumn.setCellValueFactory(
        cellData ->
            new SimpleStringProperty(
                cellData.getValue().getStartingTeam() != null
                    ? cellData.getValue().getStartingTeam().stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(", "))
                    : "-"));

    champColumn.setCellFactory(
        column ->
            new TableCell<TableTeamDTO, String>() {
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
    champColumn.setCellValueFactory(
        cellData ->
            new SimpleStringProperty(
                cellData.getValue().getChampionshipsId() != null
                        && !cellData.getValue().getChampionshipsId().isEmpty()
                    ? String.join("\n", cellData.getValue().getChampionshipsId())
                    : "-"));

    ppColumn.setCellFactory(
        column ->
            new TableCell<TableTeamDTO, String>() {
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
    ppColumn.setCellValueFactory(
        cellData ->
            new SimpleStringProperty(
                cellData.getValue().getPodiumPositions() != null
                        && !cellData.getValue().getPodiumPositions().isEmpty()
                    ? String.join("\n", cellData.getValue().getPodiumPositions())
                    : "-"));

    gamesTColumn.setCellValueFactory(
        cellData ->
            new SimpleStringProperty(
                cellData.getValue().getGamesId() != null
                    ? cellData.getValue().getGamesId().stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(", "))
                    : "-"));

    teamDataTable.getItems().clear(); // Clear previous items
    teamDataTable.getItems().add(tableDTO); // Add the new team data
    teamDataTable.refresh();
    updateButton.setVisible(true);
    removeButton.setVisible(true);
  }

  private TableTeamDTO getTableTeamDTO(TeamDTO team) {
    List<String> playersString = getPlayerStrings(team.getPlayersId());
    List<String> ppStrings = getPP(team.getPodiumPositions());
    List<String> champsStrings = getChamps(team.getChampionshipsId());
    List<Long> gamesStrings = getGames(team.getId());
    TableTeamDTO ttDTO =
        new TableTeamDTO(
            team.getId(),
            team.getName(),
            playersString,
            ppStrings,
            team.getStartingTeam(),
            champsStrings,
            gamesStrings);
    return ttDTO;
  }

  private List<Long> getGames(long id) {
    List<Long> gamesId = new ArrayList<>();
    try {
      ResponseEntity<List<GameDTO>> response =
          restTemplate.exchange(
              BASE_URL + "/team/" + id + "/games",
              HttpMethod.GET,
              null,
              new ParameterizedTypeReference<List<GameDTO>>() {});
      if (response.getStatusCode() == HttpStatus.OK) {
        List<GameDTO> games = response.getBody();
        if (games != null && !games.isEmpty()) {
          for (GameDTO dto : games) {
            gamesId.add(dto.getId());
          }
        }
      } else {
        showAlert(AlertType.ERROR, "Erro", "Erro ao encontrar Games.");
      }
    } catch (Exception e) {
    }
    return gamesId;
  }

  private List<String> getChamps(List<Long> championshipsId) {
    List<String> chamStrings = new ArrayList<>();
    if (championshipsId != null && !championshipsId.isEmpty()) {
      try {
        for (Long id : championshipsId) {
          ResponseEntity<ChampionshipDTO> response =
              restTemplate.exchange(
                  BASE_URL + "/championships/id/" + id,
                  HttpMethod.GET,
                  null,
                  ChampionshipDTO.class);
          if (response.getStatusCode() == HttpStatus.OK) {
            ChampionshipDTO champDTO = response.getBody();
            if (champDTO != null) {
              String chamString = champDTO.getName() + ": " + id;
              chamStrings.add(chamString);
            }
          }
        }
      } catch (Exception e) {
      }
    }
    return chamStrings;
  }

  private List<String> getPP(List<Long> podiumPositions) {
    List<String> podiumStrings = new ArrayList<>();
    if (podiumPositions != null && !podiumPositions.isEmpty()) {
      try {
        for (Long id : podiumPositions) {
          ResponseEntity<PodiumPositionDTO> response =
              restTemplate.exchange(
                  BASE_URL + "/championships/podiumposition/" + id,
                  HttpMethod.GET,
                  null,
                  PodiumPositionDTO.class);
          if (response.getStatusCode() == HttpStatus.OK) {
            PodiumPositionDTO podiumDTO = response.getBody();
            if (podiumDTO != null) {
              Long champId = podiumDTO.getChampionshipId();
              if (champId != null) {
                try {
                  ResponseEntity<ChampionshipDTO> responseChamp =
                      restTemplate.exchange(
                          BASE_URL + "/championships/id/" + champId,
                          HttpMethod.GET,
                          null,
                          ChampionshipDTO.class);
                  if (responseChamp.getStatusCode() == HttpStatus.OK) {
                    ChampionshipDTO champDTO = responseChamp.getBody();
                    if (champDTO != null) {
                      String podiumString = champDTO.getName() + ": " + podiumDTO.getPos();
                      podiumStrings.add(podiumString);
                    } else {
                      showAlert(AlertType.ERROR, "Erro", "Championship não encontrado." + champId);
                    }
                  } else {
                    showAlert(AlertType.ERROR, "Erro", "Erro ao encontrar Championship." + champId);
                  }
                } catch (Exception e) {
                  showAlert(
                      AlertType.ERROR, "Erro", "Erro ao encontrar Championship: " + e.getMessage());
                }
              }
            } else {
              showAlert(AlertType.ERROR, "Erro", "PodiumPosition não encontrado." + id);
            }
          } else {
            showAlert(AlertType.ERROR, "Erro", "Erro ao encontrar PodiumPosition." + id);
          }
        }
      } catch (Exception e) {
        showAlert(AlertType.ERROR, "Erro", "Erro ao encontrar PodiumPosition: " + e.getMessage());
      }
    }
    return podiumStrings;
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

  private TableStartingTeamDTO getStTable(StartingTeamDTO sTDto) {
    TeamDTO teamDTO = getTeam("" + sTDto.getTeamId());
    String teamName = teamDTO.getName() + ": " + teamDTO.getId();
    List<String> playersStrings = getPlayerStrings(sTDto.getPlayersIds());
    String gkString = getPlayer(sTDto.getGoalkeeperId());

    return new TableStartingTeamDTO(
        sTDto.getId(), teamName, playersStrings, gkString, sTDto.getGamesId());
  }

  private void updateSTTable(TableStartingTeamDTO stTable) {
    idSTColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

    teamIdColumn.setCellValueFactory(new PropertyValueFactory<>("teamId"));

    playersStColumn.setCellFactory(
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
    playersStColumn.setCellValueFactory(
        cellData ->
            new SimpleStringProperty(
                cellData.getValue().getPlayersIds() != null
                        && !cellData.getValue().getPlayersIds().isEmpty()
                    ? String.join("\n", cellData.getValue().getPlayersIds())
                    : "-"));

    gkColumn.setCellValueFactory(new PropertyValueFactory<>("goalkeeperId"));

    gamesColumn.setCellValueFactory(
        cellData ->
            new SimpleStringProperty(
                cellData.getValue().getGamesId() != null
                    ? cellData.getValue().getGamesId().stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(", "))
                    : "-"));

    stDataTable.getItems().clear();
    stDataTable.getItems().add(stTable);
    stDataTable.refresh();
  }

  @FXML
  public void updateTeam(ActionEvent event) throws IOException {

    if (teamDTO == null) {
      showAlert(
          Alert.AlertType.WARNING, "Aviso", "Por favor, selecione uma equipa para atualizar.");
      return;
    }

    // Carrega o FXML manualmente
    FXMLLoader loader = new FXMLLoader(getClass().getResource(VIEWS + "/teamUpdate.fxml"));
    Parent root = loader.load();

    // Obtém o controller da próxima tela
    TeamUpdateController controller = loader.getController();

    // Passa a equipa selecionada
    controller.setTeamToUpdate(teamDTO);

    // Abre a nova tela
    Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    javafx.geometry.Rectangle2D screenBounds = javafx.stage.Screen.getPrimary().getVisualBounds();

    double width = screenBounds.getWidth() * 0.85;
    double height = screenBounds.getHeight() * 0.85;

    Scene scene = new Scene(root, width, height);
    currentStage.setScene(scene);
    currentStage.centerOnScreen();
    currentStage.show();
  }

  @FXML
  private void removeTeam(ActionEvent event) throws IOException{
    if (teamDTO == null) {
      showAlert(
          Alert.AlertType.WARNING, "Aviso", "Por favor, selecione uma equipa para atualizar.");
      return;
    }
    
    try {
      ResponseEntity<?> response = restTemplate.exchange(
        BASE_URL+"/team/"+teamDTO.getId(),
        HttpMethod.DELETE,
        null,
        Object.class);
      if(response.getStatusCode()==HttpStatus.NO_CONTENT){
        teamDataTable.getItems().clear();
        showAlert(AlertType.INFORMATION, "Sucesso", "Team foi removida com sucesso.");
      }else{
        showAlert(AlertType.ERROR, "ERRO", "ERRO ao remover Team.");
      }
    } catch (Exception e) {
      showAlert(AlertType.ERROR, "ERRO", "ERRO ao remover Team."+e.getMessage());
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
    loadScene(event, "/home.fxml");
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
