package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;


public class Main extends Application {


	@Override
	public void start(Stage stage) throws Exception {
		try {
            Parent root = FXMLLoader.load(getClass().getResource("MainScene.fxml"));
            Scene scene = new Scene(root);
            scene.getStylesheets().add("application/application.css");
            stage.setScene(scene);
            stage.setMinHeight(480);
            stage.setMinWidth(640);
            stage.setTitle("TicTacToe");
            stage.show();
        }
		catch (IOException e) {
            e.printStackTrace();
        }
	}


	public static void main(String[] args) {
		launch(args);
	}
}
