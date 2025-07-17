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
import pt.ul.fc.css.soccernow.dtos.RefereeDTO;
import pt.ul.fc.css.soccernow.dtos.UserDetailsDTO;

/**
 * Controller for managing referee operations.
 *
 * @author Ana Morgado 56957
 * @author João Azevedo 56913
 * @author Sofia Pereira 56352
 */
public class RefereeController {

  @FXML private TextField firstNameFieldCreate;
  @FXML private TextField lastNameFieldCreate;
  @FXML private ComboBox<String> hasCertificateComboCreate;
  @FXML private TextField teamsFieldCreate;

  @FXML private TextField idFieldUpdate;
  @FXML private TextField firstNameFieldUpdate;
  @FXML private TextField lastNameFieldUpdate;
  @FXML private ComboBox<String> hasCertificateComboUpdate;
  @FXML private TextField teamsFieldUpdate;

  @FXML private TextField idFieldDelete;

  @FXML private TextField idFieldView;

  // para a tabela visualizar árbitro
  @FXML private TableView<UserDetailsDTO> refereeDataTable;
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
   * Initializes the controller class. Sets up the certificate combo boxes with available options.
   */
  @FXML
  public void initialize() {
    hasCertificateComboCreate.setItems(FXCollections.observableArrayList("SIM", "NAO"));
    hasCertificateComboUpdate.setItems(FXCollections.observableArrayList("SIM", "NAO"));
  }

  /** Handles the creation of a new referee. */
  @FXML
  private void createReferee() {
    String firstName = firstNameFieldCreate.getText();
    String lastName = lastNameFieldCreate.getText();
    String certificate = hasCertificateComboCreate.getValue();
    String teams = teamsFieldCreate.getText();

    if (firstName.isEmpty() || lastName.isEmpty() || certificate == null) {
      showAlert(
          AlertType.ERROR,
          "Campos obrigatórios",
          "Preencha os campos dos nomes e se tem certificado.");
      return;
    }

    try {
      boolean hasCertificate = certificate.equals("SIM");

      List<Long> teamIds =
          Arrays.stream(teams.split(","))
              .map(String::trim)
              .filter(s -> !s.isEmpty())
              .map(Long::parseLong)
              .collect(Collectors.toList());

      RefereeDTO refereeDTO = new RefereeDTO();
      refereeDTO.setFirstName(firstName);
      refereeDTO.setLastName(lastName);
      refereeDTO.setHasCertificate(hasCertificate);
      refereeDTO.setRefereeTeams(teamIds);

      HttpEntity<RefereeDTO> request = new HttpEntity<>(refereeDTO);
      ResponseEntity<Long> response =
          restTemplate.exchange(BASE_URL + "/referees", HttpMethod.POST, request, Long.class);

      if (response.getStatusCode() == HttpStatus.CREATED) {
        showAlert(
            AlertType.INFORMATION, "Sucesso", "Árbitro criado com o ID: " + response.getBody());
      } else {
        showAlert(
            AlertType.ERROR,
            "Erro",
            "Erro ao criar o árbitro. Status: " + response.getStatusCode());
      }

    } catch (Exception e) {
      showAlert(AlertType.ERROR, "Erro", "Erro ao criar o árbitro: " + e.getMessage());
    }
  }

  /** Handles the updating an existing referee. */
  @FXML
  private void updateReferee() {
    String id = idFieldUpdate.getText();
    String firstName = firstNameFieldUpdate.getText();
    String lastName = lastNameFieldUpdate.getText();
    String certificate = hasCertificateComboUpdate.getValue();
    String teams = teamsFieldUpdate.getText();

    if (firstName.isEmpty() || lastName.isEmpty() || certificate.isEmpty()) {
      showAlert(
          AlertType.ERROR,
          "Campos obrigatórios",
          "Preencha os campos dos nomes e se tem certificado.");
      return;
    }

    try {
      boolean hasCertificate = certificate.equals("SIM");

      List<Long> teamIds =
          Arrays.stream(teams.split(","))
              .map(String::trim)
              .filter(s -> !s.isEmpty())
              .map(Long::parseLong)
              .collect(Collectors.toList());

      RefereeDTO refereeDTO = new RefereeDTO();
      refereeDTO.setId(Long.parseLong(id));
      refereeDTO.setFirstName(firstName);
      refereeDTO.setLastName(lastName);
      refereeDTO.setHasCertificate(hasCertificate);
      refereeDTO.setRefereeTeams(teamIds);

      HttpEntity<RefereeDTO> request = new HttpEntity<>(refereeDTO);
      ResponseEntity<Long> response =
          restTemplate.exchange(BASE_URL + "/referees", HttpMethod.PUT, request, Long.class);

      if (response.getStatusCode() == HttpStatus.OK) {
        showAlert(
            AlertType.INFORMATION, "Sucesso", "Árbitro atualizado com ID: " + response.getBody());
      } else {
        showAlert(
            AlertType.ERROR,
            "Erro",
            "Erro ao atualizar o árbitro. Status: " + response.getStatusCode());
      }

    } catch (Exception e) {
      showAlert(AlertType.ERROR, "Erro", "Erro ao atualizar árbitro: " + e.getMessage());
    }
  }

  /** Handles the deleting of an referee. */
  @FXML
  private void deleteReferee() {
    String id = idFieldDelete.getText();

    if (id.isEmpty()) {
      showAlert(AlertType.ERROR, "Campo obrigatório", "Forneça o ID do árbitro.");
      return;
    }

    try {
      ResponseEntity<Boolean> response =
          restTemplate.exchange(
              BASE_URL + "/referees/" + id, HttpMethod.DELETE, null, Boolean.class);

      if (response.getStatusCode() == HttpStatus.OK && Boolean.TRUE.equals(response.getBody())) {
        showAlert(AlertType.INFORMATION, "Sucesso", "Árbitro eliminado com sucesso.");
      } else {
        showAlert(
            AlertType.ERROR,
            "Erro",
            "Erro ao eliminar o árbitro. Status: " + response.getStatusCode());
      }

    } catch (Exception e) {
      showAlert(AlertType.ERROR, "Erro", "Erro ao eliminar árbitro: " + e.getMessage());
    }
  }

  /** Retrieves and displays a single referee's information. */
  @FXML
  private void verifyReferee() {
    String id = idFieldView.getText();

    if (id.isEmpty()) {
      showAlert(AlertType.ERROR, "Campo obrigatório", "Forneça o ID do árbitro.");
      return;
    }

    try {
      ResponseEntity<String> response =
          restTemplate.exchange(
              BASE_URL + "/users/" + id + "/check", HttpMethod.GET, null, String.class);

      if (response.getStatusCode() == HttpStatus.OK) {
        ObjectMapper mapper = new ObjectMapper();
        UserDetailsDTO referee = mapper.readValue(response.getBody(), UserDetailsDTO.class);

        if (!referee.getTypeUser().equals("REFEREE")) {
          showAlert(AlertType.ERROR, "Árbitro não existe", "Este árbitro não existe.");
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

        refereeDataTable.getItems().clear();
        refereeDataTable.getItems().add(referee);

      } else {
        showAlert(
            AlertType.ERROR,
            "Arbitro nao ativo",
            "Este arbitro está inativo e nao pode ser visualizado.");
      }
    } catch (Exception e) {
      showAlert(
          AlertType.ERROR,
          "Arbitro nao ativo",
          "Este arbitro está inativo e nao pode ser visualizado.");
    }
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
}
