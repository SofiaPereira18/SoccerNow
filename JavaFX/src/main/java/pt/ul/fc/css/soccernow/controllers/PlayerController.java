package pt.ul.fc.css.soccernow.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import pt.ul.fc.css.soccernow.dtos.PlayerDTO;
import pt.ul.fc.css.soccernow.dtos.UserDetailsDTO;

/**
 * Controller for managing player operations.
 *
 * @author Ana Morgado 56957
 * @author João Azevedo 56913
 * @author Sofia Pereira 56352
 */
public class PlayerController {

  @FXML private TextField firstNameFieldCreate;
  @FXML private TextField lastNameFieldCreate;
  @FXML private ComboBox<String> positionComboBoxCreate;
  @FXML private TextField teamsFieldCreate;
  @FXML private TextField startingTeamsFieldCreate;

  @FXML private TextField idFieldUpdate;
  @FXML private TextField firstNameFieldUpdate;
  @FXML private TextField lastNameFieldUpdate;
  @FXML private ComboBox<String> positionComboBoxUpdate;
  @FXML private TextField teamsFieldUpdate;
  @FXML private TextField startingTeamsFieldUpdate;

  @FXML private TextField idFieldDelete;

  @FXML private TextField idFieldView;

  // para a tabela visualizar jogador
  @FXML private TableView<UserDetailsDTO> playerDataTable;
  @FXML private TableColumn<UserDetailsDTO, Long> idColumn;
  @FXML private TableColumn<UserDetailsDTO, String> firstNameColumn;
  @FXML private TableColumn<UserDetailsDTO, String> lastNameColumn;
  @FXML private TableColumn<UserDetailsDTO, String> typeUserColumn;
  @FXML private TableColumn<UserDetailsDTO, String> positionColumn;
  @FXML private TableColumn<UserDetailsDTO, String> teamsColumn;
  @FXML private TableColumn<UserDetailsDTO, Integer> totalGoalsColumn;
  @FXML private TableColumn<UserDetailsDTO, Integer> totalCardsColumn;
  @FXML private TableColumn<UserDetailsDTO, Integer> totalMatchesColumn;
  @FXML private TableColumn<UserDetailsDTO, Boolean> hasCertificateColumn;
  @FXML private TableColumn<UserDetailsDTO, String> refereeTeamsColumn;

  private static final String VIEWS = "/views";
  private final RestTemplate restTemplate = new RestTemplate();
  private final String BASE_URL = "http://localhost:8080/api";

  /**
   * Initializes the controller class. Sets up the position combo boxes with available player
   * positions.
   */
  @FXML
  public void initialize() {
    positionComboBoxCreate.setItems(FXCollections.observableArrayList("GOALKEEPER", "OTHER"));
    positionComboBoxUpdate.setItems(FXCollections.observableArrayList("GOALKEEPER", "OTHER"));
  }

  /** Handles the creation of a new player. */
  @FXML
  private void createPlayer() {
    String firstName = firstNameFieldCreate.getText();
    String lastName = lastNameFieldCreate.getText();
    String position = positionComboBoxCreate.getValue();
    String teams = teamsFieldCreate.getText();
    String startingTeams = startingTeamsFieldCreate.getText();

    if (firstName.isEmpty() || lastName.isEmpty() || position == null) {
      showAlert(
          AlertType.ERROR, "Campos obrigatórios", "Preencha os campos dos nomes e a posição.");
      return;
    }

    try {
      List<Long> teamIds =
          Arrays.stream(teams.split(","))
              .map(String::trim)
              .filter(s -> !s.isEmpty())
              .map(Long::parseLong)
              .collect(Collectors.toList());

      List<Long> startingTeamIds =
          Arrays.stream(startingTeams.split(","))
              .map(String::trim)
              .filter(s -> !s.isEmpty())
              .map(Long::parseLong)
              .collect(Collectors.toList());

      PlayerDTO playerDTO = new PlayerDTO();
      playerDTO.setFirstName(firstName);
      playerDTO.setLastName(lastName);
      playerDTO.setPosition(position);
      playerDTO.setTeams(teamIds);
      playerDTO.setStartingTeams(startingTeamIds);

      HttpEntity<PlayerDTO> request = new HttpEntity<>(playerDTO);
      ResponseEntity<Long> response =
          restTemplate.exchange(BASE_URL + "/players", HttpMethod.POST, request, Long.class);

      if (response.getStatusCode() == HttpStatus.CREATED) {
        showAlert(AlertType.INFORMATION, "Sucesso", "Jogador criado com ID: " + response.getBody());
      } else {
        showAlert(
            AlertType.ERROR, "Erro", "Erro ao criar jogador. Status: " + response.getStatusCode());
      }

    } catch (Exception e) {
      showAlert(AlertType.ERROR, "Erro", "Erro ao criar jogador: " + e.getMessage());
    }
  }

  /** Handles the updating of an existing player. */
  @FXML
  private void updatePlayer() {
    String id = idFieldUpdate.getText();
    String firstName = firstNameFieldUpdate.getText();
    String lastName = lastNameFieldUpdate.getText();
    String position = positionComboBoxUpdate.getValue();
    String teams = teamsFieldUpdate.getText();
    String startingTeams = startingTeamsFieldUpdate.getText();

    if (firstName.isEmpty() || lastName.isEmpty() || position.isEmpty()) {
      showAlert(
          AlertType.ERROR, "Campos obrigatórios", "Preencha os campos dos nomes e a posição.");
      return;
    }

    try {
      List<Long> teamIds =
          Arrays.stream(teams.split(","))
              .map(String::trim)
              .filter(s -> !s.isEmpty())
              .map(Long::parseLong)
              .collect(Collectors.toList());

      List<Long> startingTeamIds =
          Arrays.stream(startingTeams.split(","))
              .map(String::trim)
              .filter(s -> !s.isEmpty())
              .map(Long::parseLong)
              .collect(Collectors.toList());

      PlayerDTO playerDTO = new PlayerDTO();
      playerDTO.setId(Long.parseLong(id));
      playerDTO.setFirstName(firstName);
      playerDTO.setLastName(lastName);
      playerDTO.setPosition(position);
      playerDTO.setTeams(teamIds);
      playerDTO.setStartingTeams(startingTeamIds);

      HttpEntity<PlayerDTO> request = new HttpEntity<>(playerDTO);
      ResponseEntity<Long> response =
          restTemplate.exchange(BASE_URL + "/players", HttpMethod.PUT, request, Long.class);

      if (response.getStatusCode() == HttpStatus.OK) {
        showAlert(
            AlertType.INFORMATION, "Sucesso", "Jogador atualizado com ID: " + response.getBody());
      } else {
        showAlert(
            AlertType.ERROR,
            "Erro",
            "Erro ao atualizar o jogador. Status: " + response.getStatusCode());
      }

    } catch (Exception e) {
      showAlert(AlertType.ERROR, "Erro", "Erro ao atualizar jogador: " + e.getMessage());
    }
  }

  /** Handles the deleting of an player. */
  @FXML
  private void deletePlayer() {
    String id = idFieldDelete.getText();

    if (id.isEmpty()) {
      showAlert(AlertType.ERROR, "Campo obrigatório", "Forneça o ID do jogador.");
      return;
    }

    try {
      ResponseEntity<Boolean> response =
          restTemplate.exchange(
              BASE_URL + "/players/" + id, HttpMethod.DELETE, null, Boolean.class);

      if (response.getStatusCode() == HttpStatus.OK && Boolean.TRUE.equals(response.getBody())) {
        showAlert(AlertType.INFORMATION, "Sucesso", "Jogador eliminado com sucesso.");
      } else {
        showAlert(AlertType.ERROR, "Erro", "Jogador não encontrado ou não foi eliminado.");
      }

    } catch (Exception e) {
      showAlert(AlertType.ERROR, "Erro", "Erro ao eliminar jogador: " + e.getMessage());
    }
  }

  /** Retrieves and displays a single player's information. */
  @FXML
  private void verifyPlayer() {
    String id = idFieldView.getText();

    if (id.isEmpty()) {
      showAlert(AlertType.ERROR, "Campo obrigatório", "Forneça o ID do jogador.");
      return;
    }

    try {
      ResponseEntity<String> response =
          restTemplate.exchange(
              BASE_URL + "/users/" + id + "/check", HttpMethod.GET, null, String.class);

      if (response.getStatusCode() == HttpStatus.OK) {
        ObjectMapper mapper = new ObjectMapper();
        UserDetailsDTO player = mapper.readValue(response.getBody(), UserDetailsDTO.class);

        if (!player.getTypeUser().equals("PLAYER")) {
          showAlert(AlertType.ERROR, "Jogador não existe", "Este jogador não existe.");
          return;
        }

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        typeUserColumn.setCellValueFactory(new PropertyValueFactory<>("typeUser"));
        positionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));
        teamsColumn.setCellValueFactory(
            cellData ->
                new SimpleStringProperty(
                    cellData.getValue().getTeams() != null
                        ? cellData.getValue().getTeams().stream()
                            .map(team -> String.valueOf(team.getId()))
                            .collect(Collectors.joining(", "))
                        : "-"));

        totalGoalsColumn.setCellValueFactory(new PropertyValueFactory<>("totalGoals"));
        totalCardsColumn.setCellValueFactory(new PropertyValueFactory<>("totalCards"));
        totalMatchesColumn.setCellValueFactory(new PropertyValueFactory<>("totalMatches"));
        hasCertificateColumn.setCellValueFactory(new PropertyValueFactory<>("hasCertificate"));
        refereeTeamsColumn.setCellValueFactory(
            cellData ->
                new SimpleStringProperty(
                    cellData.getValue().getRefereeTeams() != null
                        ? cellData.getValue().getRefereeTeams().stream()
                            .map(team -> String.valueOf(team.getId()))
                            .collect(Collectors.joining(", "))
                        : "-"));

        playerDataTable.getItems().clear();
        playerDataTable.getItems().add(player);

      } else {
        showAlert(
            AlertType.ERROR,
            "Jogador nao ativo",
            "Este jogador está inativo e nao pode ser visualizado.");
        return;
      }
    } catch (Exception e) {
      showAlert(
          AlertType.ERROR,
          "Jogador nao ativo",
          "Este jogador está inativo e nao pode ser visualizado.");
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
