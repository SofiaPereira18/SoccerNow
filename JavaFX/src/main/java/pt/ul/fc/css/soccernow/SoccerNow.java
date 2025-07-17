package pt.ul.fc.css.soccernow;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class SoccerNow extends Application {

  private static final String VIEWS = "/views";

  @Override
  public void start(Stage primaryStage) throws Exception {
    FXMLLoader loader = new FXMLLoader(getClass().getResource(VIEWS + "/login.fxml"));
    StackPane root = loader.load();
    Scene scene = new Scene(root, 400, 300);
    primaryStage.setTitle("Login");
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
