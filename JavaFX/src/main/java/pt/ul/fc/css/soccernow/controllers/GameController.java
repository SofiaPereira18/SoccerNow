package pt.ul.fc.css.soccernow.controllers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
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
import pt.ul.fc.css.soccernow.dtos.GameDTO;
import pt.ul.fc.css.soccernow.dtos.GameRequestDTO;
import pt.ul.fc.css.soccernow.dtos.PlayerDTO;
import pt.ul.fc.css.soccernow.dtos.StartingTeamDTO;
import pt.ul.fc.css.soccernow.dtos.StatsDTO;
import pt.ul.fc.css.soccernow.dtos.TableGamesDTO;
import pt.ul.fc.css.soccernow.dtos.TeamDTO;

public class GameController {

  @FXML private DatePicker dataJogo;
  @FXML private TextField horaJogo;
  @FXML private TextField minJogo;
  @FXML private TextField st1Jogo;
  @FXML private TextField st2Jogo;
  @FXML private TextField refereeJogo;
  @FXML private TextField addressJogo;
  @FXML private TextField cityJogo;

  @FXML private TextField idJogo;

  @FXML private TableView<TableGamesDTO> tableView;
  @FXML private TableColumn<TableGamesDTO, Long> idColumn;
  @FXML private TableColumn<TableGamesDTO, String> homeColumn;
  @FXML private TableColumn<TableGamesDTO, Long> totalHColumn;
  @FXML private TableColumn<TableGamesDTO, String> awayColumn;
  @FXML private TableColumn<TableGamesDTO, Long> totalAColumn;
  @FXML private TableColumn<TableGamesDTO, String> locationColumn;
  @FXML private TableColumn<TableGamesDTO, String> turnoColumn;
  @FXML private TableColumn<TableGamesDTO, String> estadoColumn;

  @FXML private TextField playerId;
  @FXML private ChoiceBox<String> teamOption;

  private static final String VIEWS = "/views";
  private final RestTemplate restTemplate = new RestTemplate();
  private final String BASE_URL = "http://localhost:8080/api";

  private static Long gameId = 0L;
  private static Long homeTeamid = 0L;
  private static Long awayTeamid = 0L;

  @FXML
  private void createGame(ActionEvent event) throws IOException {
    if (dataJogo.getValue() != null
        && horaJogo.getText() != null
        && !horaJogo.getText().isEmpty()
        && minJogo.getText() != null
        && !minJogo.getText().isEmpty()
        && st1Jogo.getText() != null
        && !st1Jogo.getText().isEmpty()
        && st2Jogo.getText() != null
        && !st2Jogo.getText().isEmpty()
        && refereeJogo.getText() != null
        && !refereeJogo.getText().isEmpty()
        && addressJogo.getText() != null
        && !addressJogo.getText().isEmpty()
        && cityJogo.getText() != null
        && !cityJogo.getText().isEmpty()) {
      int year = dataJogo.getValue().getYear();
      Month month = dataJogo.getValue().getMonth();
      int day = dataJogo.getValue().getDayOfMonth();
      String hour = horaJogo.getText();
      String min = minJogo.getText();
      String st1 = st1Jogo.getText();
      String st2 = st2Jogo.getText();
      String referee = refereeJogo.getText();
      String address = addressJogo.getText();
      String city = cityJogo.getText();

      List<Long> st = new ArrayList<>();
      st.add(Long.parseLong(st1));
      st.add(Long.parseLong(st2));

      LocalDateTime time =
          LocalDateTime.of(year, month, day, Integer.parseInt(hour), Integer.parseInt(min));

      GameRequestDTO requestDTO =
          new GameRequestDTO(time, address, city, Long.parseLong(referee), st);
      HttpEntity<GameRequestDTO> request = new HttpEntity<GameRequestDTO>(requestDTO);
      try {
        ResponseEntity<GameDTO> response =
            restTemplate.exchange(BASE_URL + "/games", HttpMethod.POST, request, GameDTO.class);
        if (response.getStatusCode() == HttpStatus.CREATED && response.getBody() != null) {
          GameDTO gameDTO = response.getBody();
          TableGamesDTO tableDTO = getTableTeamDTO(gameDTO);
          updateTable(tableDTO);
          gameId = gameDTO.getId();
          homeTeamid = gameDTO.getTeamsIds().get(0);
          awayTeamid = gameDTO.getTeamsIds().get(1);
          showAlert(
              AlertType.INFORMATION,
              "Sucesso",
              "Jogo foi creado com ID: " + gameDTO.getId());
        } else {
          showAlert(AlertType.ERROR, "Erro", "Ocurreu um erro ao cria o Jogo");
        }
      } catch (NumberFormatException e) {
        showAlert(
            AlertType.ERROR, "Erro", "Hora, minuto, ids ou outros campos numéricos inválidos.");
      } catch (Exception e) {
        showAlert(AlertType.ERROR, "Erro", "Ocurreu um erro ao cria o Jogo: " + e.getMessage());
      }
    } else {
      showAlert(AlertType.ERROR, "Erro", "Tem de preencher todos os campos");
    }
  }

  @FXML
  private void getGame(){
    if (idJogo.getText() != null && !idJogo.getText().isEmpty()) {
      Long id = Long.parseLong(idJogo.getText());
      getGameWithId(id);
    } else {
      showAlert(AlertType.ERROR, "Erro", "Tem de preencher o campo ID do Jogo");
    }
  }

  private void getGameWithId(Long id) {
    try {
      ResponseEntity<GameDTO> response =
          restTemplate.exchange(BASE_URL + "/games/" + id, HttpMethod.GET, null, GameDTO.class);
      if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
        GameDTO gameDTO = response.getBody();
        TableGamesDTO tableDTO = getTableTeamDTO(gameDTO);
        updateTable(tableDTO);
        gameId = gameDTO.getId();
        homeTeamid = gameDTO.getTeamsIds().get(0);
        awayTeamid = gameDTO.getTeamsIds().get(1);
      } else {
        showAlert(AlertType.ERROR, "Erro", "Jogo não encontrado com ID: " + id);
      }
    } catch (NumberFormatException e) {
      showAlert(AlertType.ERROR, "Erro", "ID inválido.");
    } catch (Exception e) {
      showAlert(AlertType.ERROR, "Erro", "Ocurreu um erro ao obter o Jogo: " + e.getMessage());
    }
  }

  private void updateTable(TableGamesDTO tableDTO) {
    idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

    homeColumn.setCellFactory(
        column ->
            new TableCell<TableGamesDTO, String>() {
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
    homeColumn.setCellValueFactory(
        cellData ->
            new SimpleStringProperty(
                cellData.getValue().getHomeTeam() != null
                        && !cellData.getValue().getHomeTeam().isEmpty()
                    ? String.join("\n", cellData.getValue().getHomeTeam())
                    : "-"));

    totalHColumn.setCellValueFactory(new PropertyValueFactory<>("totalHomeGoals"));

    awayColumn.setCellFactory(
        column ->
            new TableCell<TableGamesDTO, String>() {
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
    awayColumn.setCellValueFactory(
        cellData ->
            new SimpleStringProperty(
                cellData.getValue().getAwayTeam() != null
                        && !cellData.getValue().getAwayTeam().isEmpty()
                    ? String.join("\n", cellData.getValue().getAwayTeam())
                    : "-"));

    totalAColumn.setCellValueFactory(new PropertyValueFactory<>("totalAwayGoals"));
    locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
    turnoColumn.setCellValueFactory(new PropertyValueFactory<>("turno"));
    estadoColumn.setCellValueFactory(new PropertyValueFactory<>("estado"));
    tableView.getItems().clear();
    tableView.getItems().add(tableDTO);
  }

  private TableGamesDTO getTableTeamDTO(GameDTO gameDTO) {
    StatsDTO statsDTO = getStats(gameDTO.getId());
    String location = gameDTO.getCity() + " - " + gameDTO.getStadium();
    List<String> homeTeam = new ArrayList<>();
    List<String> awayTeam = new ArrayList<>();
    String result = "Decorrer";
    int goalsHome = 0;
    int goalsAway = 0;
    if(statsDTO!=null){
      homeTeam = getTeam(statsDTO.getGoalsHomeTeam(), gameDTO.getTeamsIds().get(0));
      awayTeam = getTeam(statsDTO.getGoalsAwayTeam(), gameDTO.getTeamsIds().get(1));
      goalsHome = getGoals(statsDTO.getGoalsHomeTeam());
      goalsAway = getGoals(statsDTO.getGoalsAwayTeam());
      result= statsDTO.getResult() != null ? statsDTO.getResult().toString() : "Decorrer";

    }else{
      homeTeam = getTeam(null, gameDTO.getTeamsIds().get(0));
      awayTeam = getTeam(null, gameDTO.getTeamsIds().get(1));
    }
    TableGamesDTO tableDTO = new TableGamesDTO();
    tableDTO.setId(gameDTO.getId());
    tableDTO.setHomeTeam(homeTeam);
    tableDTO.setTotalHomeGoals(goalsHome);
    tableDTO.setAwayTeam(awayTeam);
    tableDTO.setTotalAwayGoals(goalsAway);
    tableDTO.setLocation(location);
    if(gameDTO.getGameShift().equals(GameDTO.GameShift.MORNING)){
      tableDTO.setTurno("Morning");
    }else if(gameDTO.getGameShift().equals(GameDTO.GameShift.AFTERNOON)){
      tableDTO.setTurno("Afternoon");
    }else if(gameDTO.getGameShift().equals(GameDTO.GameShift.NIGHT)){
      tableDTO.setTurno("Night");
    }
    tableDTO.setEstado(result);
    return tableDTO;
  }

  public int getGoals(Map<Long, Integer> goals) {
    if (goals != null) {
      Set<Long> playerIds = goals.keySet();
      int gls = 0;
      for (Long id : playerIds) {
        gls += goals.get(id);
      }
      return gls;
    }
    return 0;
  }

  private List<String> getTeam(Map<Long, Integer> goalsTeam, Long teamId) {
    List<String> st = new ArrayList<>();
    try {
      ResponseEntity<StartingTeamDTO> response = restTemplate.exchange(
        BASE_URL + "/team/starting-team/id/" + teamId,
        HttpMethod.GET,
        null,
        StartingTeamDTO.class);
      StartingTeamDTO stDTO = response.getBody();
      if(response.getStatusCode()==HttpStatus.OK && stDTO!=null){
        TeamDTO teamDTO = getTeam(stDTO.getTeamId());
        st.add("ID Team: " + teamDTO.getId() +" | "+teamDTO.getName());
        for (Long playerId : stDTO.getPlayersIds()) {
          if(playerId!=stDTO.getGoalkeeperId()){
            String player = getPlayer(playerId, goalsTeam);
            st.add(player);
          }
        }
        String gk = getPlayer(stDTO.getGoalkeeperId(), goalsTeam);
        st.add(gk);
      }
      
    } catch (Exception e) {
      showAlert(AlertType.ERROR, "Erro", "Erro ao obter equipa: " + e.getMessage());

    }
    return st;
  }

  private String getPlayer(Long id,Map<Long, Integer> goalsTeam ) {
    try {
      ResponseEntity<PlayerDTO> response =
          restTemplate.exchange(BASE_URL + "/player/" + id, HttpMethod.GET, null, PlayerDTO.class);
      if (response.getStatusCode() == HttpStatus.OK) {
        PlayerDTO playerDTO = response.getBody();
        if (playerDTO != null) {
          if(goalsTeam!=null && goalsTeam.get(id)!=null){
            return "ID: "+id+" | "+ playerDTO.getLastName() + " " + playerDTO.getLastName() + " - " + goalsTeam.get(id)+ " gol(s)";
          }else{
            return "ID: "+id+" | "+ playerDTO.getLastName() + " " + playerDTO.getLastName();
          }

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

  private TeamDTO getTeam(Long id) {
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

  private StatsDTO getStats(Long idGame) {
    try {
      ResponseEntity<StatsDTO> response =
          restTemplate.exchange(
            BASE_URL + "/games/" + idGame + "/stats",
            HttpMethod.GET,
            null,
            StatsDTO.class);
      if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
        StatsDTO s = response.getBody();
        if(s!=null){
          return s;
        }else{
          return null;
        }
      } else {
        System.out.println("Erro ao obter stats do jogo com ID: " + idGame);
        return null;
      }
    } catch (Exception e) {
      System.out.println("Erro ao obter stats do jogo: " + e.getMessage());
      return null;
    }
  }

  @FXML
  private void addGoal(){
    if(
      teamOption.getValue()!=null && !teamOption.getValue().isEmpty()&&
      playerId.getText()!=null && !playerId.getText().isEmpty()){
        String teamString = teamOption.getValue();
        Long stId = 0L;
        if(teamString.equals("HomeTeam")){
          stId=homeTeamid;
        }else if(teamString.equals("AwayTeam")){
          stId = awayTeamid;
        }
        if(stId==0L){
          showAlert(AlertType.ERROR, "Erro", "Tem de selecionar uma equipa");
          return;
        }
        Long idPlayer = Long.parseLong(playerId.getText());
        try {
          ResponseEntity<?> response = restTemplate.exchange(
            BASE_URL+"/games/"+gameId+"/goals/"+stId+"/"+idPlayer,
            HttpMethod.PUT,
            null,
            Object.class);
          if (response.getStatusCode() == HttpStatus.OK) {
            showAlert(AlertType.INFORMATION, "Sucesso", "Golo adicionado com sucesso!");
            getGameWithId(gameId);
          } else {
            showAlert(AlertType.ERROR, "Erro", "Erro ao adicionar golo: " + response.getStatusCode());
          }
        } catch (Exception e) {
          showAlert(AlertType.ERROR, "Erro", "Erro ao adicionar golo: " + e.getMessage());
        }
    }
  }

  @FXML
  private void endGame(){
    System.out.println("Ending game with ID: " + gameId);
    if (gameId != 0L) {
      ResponseEntity<StatsDTO.GameResult> response = restTemplate.exchange(
        BASE_URL + "/games/" + gameId + "/stats/result",
        HttpMethod.GET,
        null,
        StatsDTO.GameResult.class);
      if (response.getStatusCode() == HttpStatus.OK) {
        showAlert(AlertType.INFORMATION, "Sucesso", "Jogo terminado com sucesso!");
        getGameWithId(gameId);
      } else {
        showAlert(AlertType.ERROR, "Erro", "Erro ao terminar o jogo: " + response.getStatusCode());
      }
    } else {
      showAlert(AlertType.ERROR, "Erro", "Tem de selecionar um jogo primeiro");
    }
  }

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
