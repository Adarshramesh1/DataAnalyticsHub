package application;

import java.sql.Connection;
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

public class EditProfileController {
	public Stage primaryStage;
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

		Scene editProfileScene = new Scene(editProfileLayout, 400, 600);
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
				new Main().showAlert(primaryStage, Alert.AlertType.INFORMATION, "Profile Updated",
						"Profile updated successfully.");
				//pass new full name when it goes back to the dashboard
				String fullName = newFirstName + " " + newLastName;
				new DashBoardController().showDashboard(connection, primaryStage, fullName, newUsername); // Return to the
																										// dashboard
																										// with the
																										// updated
																										// username
			} else {
				new Main().showAlert(primaryStage, Alert.AlertType.ERROR, "Profile Update Failed",
						"Failed to update the profile.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			new Main().showAlert(primaryStage, Alert.AlertType.ERROR, "Database Error",
					"An error occurred while updating the profile.");
		}
	}

}
