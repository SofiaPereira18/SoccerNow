package pt.ul.fc.css.soccernow.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main controller handling home page.
 *
 * @author Ana Morgado 56957
 * @author João Azevedo 56913
 * @author Sofia Pereira 56352
 */
public class HomeController {

  private static final String VIEWS = "/views";

  /**
   * Handles navigation to the players management screen.
   *
   * @param event The action event triggering navigation
   * @throws IOException if the players.fxml file cannot be loaded
   */
  @FXML
  private void goToPlayersPage(ActionEvent event) throws IOException {
    loadScene(event, "/players.fxml");
  }

  /**
   * Handles navigation to the referees management screen.
   *
   * @param event The action event triggering navigation
   * @throws IOException if the referees.fxml file cannot be loaded
   */
  @FXML
  private void goToRefereesPage(ActionEvent event) throws IOException {
    loadScene(event, "/referees.fxml");
  }

  /**
   * Handles navigation to the teams management screen.
   *
   * @param event The action event triggering navigation
   * @throws IOException if the teams.fxml file cannot be loaded
   */
  @FXML
  private void goToTeamsPage(ActionEvent event) throws IOException {
    loadScene(event, "/team.fxml");
  }

  /**
   * Handles navigation to the teams management screen.
   *
   * @param event The action event triggering navigation
   * @throws IOException if the teams.fxml file cannot be loaded
   */
  @FXML
  private void goToGamesPage(ActionEvent event) throws IOException {
    loadScene(event, "/games.fxml");
  }

  /**
   * Handles navigation to the championships management screen.
   *
   * @param event The action event triggering navigation
   * @throws IOException if the championships.fxml file cannot be loaded
   */
  @FXML
  private void goToChampionshipsPage(ActionEvent event) throws IOException {
    loadScene(event, "/championships.fxml");
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
