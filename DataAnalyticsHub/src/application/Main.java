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
		new DataBase().loadPostsDatabase();

		new LoginController().showLoginScreen(primaryStage);
	}


	   

	public void showEditProfileScreen(Connection connection, Stage primaryStage, String fullName, String username) {
		// Create a new JavaFX scene for profile editing
		this.primaryStage = primaryStage;
		VBox editProfileLayout = new VBox(10);
		editProfileLayout.setStyle("-fx-background-color: #b0e0e6;");
		editProfileLayout.setPadding(new Insets(20, 20, 20, 20));

		Label titleLabel = new Label("Edit Profile");
		titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

		// Display the current username as a label
		Label CurrentUsernameLabel = new Label("Current Username: " + username);
		Label PasswordLabel = new Label("New Password: ");
		Label FirstnameLabel = new Label("New First Name: ");
		Label LastnameLabel = new Label("New Last Name: ");
		Label NewUsernameLabel = new Label("New Username: ");

		// Add fields for editing
		PasswordField passwordField = new PasswordField();
		passwordField.setPromptText("Enter your password");
		TextField newUsernameField = new TextField(); // Field for editing the username
		passwordField.setPromptText("Enter your New User Name");
		TextField firstNameField = new TextField();
		passwordField.setPromptText("Enter your New First Name");
		TextField lastNameField = new TextField();
		passwordField.setPromptText("Enter your New Last Name");

		Button saveButton = new Button("Save");
		saveButton.setOnAction(e -> saveProfileChanges(connection,username, passwordField.getText(), newUsernameField.getText(),
				firstNameField.getText(), lastNameField.getText()));
		Button backButton = new Button("Back to Login");
		backButton.setOnAction(e -> {
			new DashBoardController().showDashboard(connection, primaryStage, fullName, username);
		});

		editProfileLayout.getChildren().addAll(titleLabel, CurrentUsernameLabel, NewUsernameLabel, newUsernameField,
				PasswordLabel, passwordField, FirstnameLabel, firstNameField, LastnameLabel, lastNameField, saveButton,
				backButton);

		Scene editProfileScene = new Scene(editProfileLayout, 400, 300);
		primaryStage.setScene(editProfileScene);
	}

	private void saveProfileChanges(Connection connection, String currentUsername, String newPassword, String newUsername, String newFirstName,
			String newLastName) {
		// Implement the logic to update the user's profile in the database
		
		try {
			String query = "UPDATE users SET username = ?, password = ?, first_name = ?, last_name = ? WHERE username = ?";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, newUsername);
			statement.setString(2, newPassword);
			statement.setString(3, newFirstName);
			statement.setString(4, newLastName);
			statement.setString(5, currentUsername);
			int affectedRows = statement.executeUpdate();

			if (affectedRows > 0) {
				showAlert(primaryStage, Alert.AlertType.INFORMATION, "Profile Updated",
						"Profile updated successfully.");
				new DashBoardController().showDashboard(connection, primaryStage, currentUsername, newUsername); // Return to the
																										// dashboard
																										// with the
																										// updated
																										// username
			} else {
				showAlert(primaryStage, Alert.AlertType.ERROR, "Profile Update Failed",
						"Failed to update the profile.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			showAlert(primaryStage, Alert.AlertType.ERROR, "Database Error",
					"An error occurred while updating the profile.");
		}
	}

	
	
	public void showAlert(Stage primaryStage, Alert.AlertType alertType, String title, String content) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setContentText(content);
		alert.showAndWait();
	}

}
