package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RegistrationController {
	
	private static final String DB_URL = "jdbc:sqlite:social_media_analytics.db";

	private Connection connection;
	public Stage primaryStage;
	public void showRegistrationScreen(Stage primaryStage) {
		this.primaryStage=primaryStage;
		// Create a new JavaFX scene for user registration
		VBox registrationLayout = new VBox(10);
		registrationLayout.setStyle("-fx-background-color: #b0e0e6;");
		registrationLayout.setPadding(new Insets(20, 20, 20, 20));

		Label titleLabel = new Label("Registration");
		titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

		Label usernameLabel = new Label("Username:");
		TextField usernameField = new TextField();

		Label passwordLabel = new Label("Password:");
		PasswordField passwordField = new PasswordField();

		Label firstNameLabel = new Label("First Name:");
		TextField firstNameField = new TextField();

		Label lastNameLabel = new Label("Last Name:");
		TextField lastNameField = new TextField();

		Button registerButton = new Button("Register");
		registerButton.setOnAction(e -> {
			try {
				register(usernameField.getText(), passwordField.getText(),
						firstNameField.getText(), lastNameField.getText());
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

		Button backButton = new Button("Back to Login");
		backButton.setOnAction(e -> {
			try {
				new LoginController().showLoginScreen(primaryStage);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

		registrationLayout.getChildren().addAll(titleLabel, usernameLabel, usernameField, passwordLabel, passwordField,
				firstNameLabel, firstNameField, lastNameLabel, lastNameField, registerButton, backButton);

		Scene registrationScene = new Scene(registrationLayout, 400, 300);
		primaryStage.setScene(registrationScene);
	}
	
	public void register(String username, String password, String firstName, String lastName) throws IOException, SQLException {
		// Implement the registration logic here
		//registering users
		connection = DriverManager.getConnection(DB_URL);

		try {
			String query = "INSERT INTO users (username, password, first_name, last_name) VALUES (?, ?, ?, ?)";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, username);
			statement.setString(2, password);
			statement.setString(3, firstName);
			statement.setString(4, lastName);

			statement.executeUpdate();

			new Main().showAlert(primaryStage, Alert.AlertType.INFORMATION, "Registration Successful",
					"Registration successful. You can now log in.");
			new LoginController().showLoginScreen(primaryStage); // Return to the login screen after successful registration
		} catch (SQLException e) {
			new Main().showAlert(primaryStage, Alert.AlertType.ERROR, "Registration Failed",
					"Username already exists. Please choose a different username.");
		}
	}
}
