package pt.ul.fc.css.soccernow.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controller for handling user authentication and login screen interactions.
 *
 * @author Ana Morgado 56957
 * @author João Azevedo 56913
 * @author Sofia Pereira 56352
 */
public class LoginController {

  private static final String VIEWS = "/views";

  @FXML private TextField usernameField;

  @FXML private PasswordField passwordField;

  @FXML private Label errorMessage;

  /**
   * Handles the login button action event.
   *
   * @param event The action event triggered by login button
   */
  @FXML
  private void handleLogin(ActionEvent event) {
    String username = usernameField.getText();
    String password = passwordField.getText();

    if (isValidLogin(username, password)) {
      try {
        loadScene(event, "/home.fxml");
      } catch (IOException e) {
        showError("Erro ao carregar a página!");
        e.printStackTrace();
      }
    } else {
      showError("Credenciais inválidas!");
    }
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
   * Displays an error message.
   *
   * @param message The error message to display
   */
  private void showError(String message) {
    errorMessage.setText(message);
    errorMessage.setVisible(true);
  }

  /**
   * Validates login.
   *
   * @param username The username to validate
   * @param password The password to validate
   * @return true if username matches authorized emails, false otherwise
   */
  private boolean isValidLogin(String username, String password) {
    List<String> emails = new ArrayList<>();
    emails.add("fc56352@alunos.fc.ul.pt");
    emails.add("fc56913@alunos.fc.ul.pt");
    emails.add("fc56957@alunos.fc.ul.pt");
    emails.add("");

    return emails.contains(username);
  }
}
