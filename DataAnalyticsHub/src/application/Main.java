package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Main extends Application {

	public Stage primaryStage;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws FileNotFoundException, IOException, SQLException {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Social Media App");

		new DataBase().createUserDatabase();
		new DataBase().createPostsDatabase();
		

		new LoginController().showLoginScreen(primaryStage);
	}


	   

	
	
	
	public void showAlert(Stage primaryStage, Alert.AlertType alertType, String title, String content) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setContentText(content);
		alert.showAndWait();
	}

}
