package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

public class LoginController {
	private static final String DB_URL = "jdbc:sqlite:social_media_analytics.db";

	private Connection connection;
	private TextField usernameField;
	private PasswordField passwordField;
	public Stage primaryStage;
	
	public void showLoginScreen(Stage primaryStage) throws IOException {
		this.primaryStage=primaryStage;
		VBox loginLayout = new VBox(10);
		loginLayout.setStyle("-fx-background-color: #b0e0e6;");
		loginLayout.setPadding(new Insets(20, 20, 20, 20));

		Label titleLabel = new Label("Social Media App");
		titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

		Label usernameLabel = new Label("Username:");
		usernameField = new TextField();

		Label passwordLabel = new Label("Password:");
		passwordField = new PasswordField();

		Button loginButton = new Button("Login");
		loginButton.setOnAction(e -> {
			try {
				login();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		});

		Button registerButton = new Button("Register");
		registerButton.setOnAction(e -> new RegistrationController().showRegistrationScreen(primaryStage));

		loginLayout.getChildren().addAll(titleLabel, usernameLabel, usernameField, passwordLabel, passwordField,
				loginButton, registerButton);
		//Parent root=FXMLLoader.load(getClass().getResource("LoginView.fxml"));

		Scene loginScene = new Scene(loginLayout, 300, 400);
		//Scene loginScene=new Scene(root);
		primaryStage.setScene(loginScene);
		primaryStage.show();
	}


	public void login() throws SQLException {
		String username = usernameField.getText();
		String password = passwordField.getText();
		connection = DriverManager.getConnection(DB_URL);

		// Validate username and password (you should hash and compare passwords
		// securely)

		try {
			String query = "SELECT * FROM users WHERE username = ? AND password = ?";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, username);
			statement.setString(2, password);

			ResultSet resultSet = statement.executeQuery();

			if (resultSet.next()) {
				// Successful login
				String fullName = resultSet.getString("first_name") + " " + resultSet.getString("last_name");

				new DashBoardController().showDashboard(connection,primaryStage, fullName, username);
			} else {
				// Failed login, show an error message
				new Main().showAlert(primaryStage, Alert.AlertType.ERROR, "Login Failed", "Invalid username or password.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
