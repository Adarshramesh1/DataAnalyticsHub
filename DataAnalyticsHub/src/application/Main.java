package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main extends Application {

	private static final String DB_URL = "jdbc:sqlite:social_media_analytics.db";

	private Connection connection;
	private ListView<String> topPostsListView;
	private TextArea postTextArea;

	private Stage primaryStage;
	private TextField usernameField;
	private PasswordField passwordField;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws FileNotFoundException, IOException {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Social Media App");

		createUserDatabase();
		createPostsDatabase();
		loadPostsDatabase();

		showLoginScreen();
	}

	private void loadPostsDatabase() throws FileNotFoundException, IOException {

		String csvFilePath = "C:\\Users\\adars\\OneDrive\\Desktop\\ADVANced programming\\posts.csv"; // Replace with
																										// your CSV file
																										// path
		try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
			String line;
			boolean isFirstRow = true;
			while ((line = reader.readLine()) != null) {
				if (isFirstRow) {
					isFirstRow = false;
					continue; // Skip the header row
				}

				String[] parts = line.split(",");
				int id = Integer.parseInt(parts[0]);
				String content = parts[1];
				String author = parts[2];
				int likes = Integer.parseInt(parts[3]);
				int shares = Integer.parseInt(parts[4]);
				String dateTime = parts[5];
				String insertQuery = "INSERT OR REPLACE INTO posts (id, content, author, likes, shares, dateTime) VALUES (?, ?, ?, ?, ?, ?)";

				PreparedStatement preparedStatement;
				try {
					preparedStatement = connection.prepareStatement(insertQuery);
					preparedStatement.setInt(1, id);
					preparedStatement.setString(2, content);
					preparedStatement.setString(3, author);
					preparedStatement.setInt(4, likes);
					preparedStatement.setInt(5, shares);
					preparedStatement.setString(6, dateTime);

					// Execute the INSERT statement
					preparedStatement.executeUpdate();

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

	}

	private void createPostsDatabase() {
		try {
			connection = DriverManager.getConnection(DB_URL);
			String query = "CREATE TABLE IF NOT EXISTS posts (" + "id INTEGER UNIQUE NOT NULL,"
					+ "content TEXT NOT NULL," + "author TEXT NOT NULL," + "likes INTEGER NOT NULL,"
					+ "shares INTEGER NOT NULL," + "dateTime TEXT NOT NULL)";
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void createUserDatabase() {
		try {
			connection = DriverManager.getConnection(DB_URL);
			String query = "CREATE TABLE IF NOT EXISTS users (" + "username TEXT UNIQUE NOT NULL,"
					+ "password TEXT NOT NULL," + "first_name TEXT NOT NULL," + // Added column for first name
					"last_name TEXT NOT NULL)"; // Added column for last name
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void showLoginScreen() throws IOException {
		/*VBox loginLayout = new VBox(10);
		loginLayout.setStyle("-fx-background-color: #b0e0e6;");
		loginLayout.setPadding(new Insets(20, 20, 20, 20));

		Label titleLabel = new Label("Social Media App");
		titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

		Label usernameLabel = new Label("Username:");
		usernameField = new TextField();

		Label passwordLabel = new Label("Password:");
		passwordField = new PasswordField();

		Button loginButton = new Button("Login");
		loginButton.setOnAction(e -> login());

		Button registerButton = new Button("Register");
		registerButton.setOnAction(e -> showRegistrationScreen());

		loginLayout.getChildren().addAll(titleLabel, usernameLabel, usernameField, passwordLabel, passwordField,
				loginButton, registerButton);*/
		Parent root=FXMLLoader.load(getClass().getResource("LoginView.fxml"));

		//Scene loginScene = new Scene(loginLayout, 300, 250);
		Scene loginScene=new Scene(root);
		primaryStage.setScene(loginScene);
		primaryStage.show();
	}

	private void showRegistrationScreen() {
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
			}
		});

		Button backButton = new Button("Back to Login");
		backButton.setOnAction(e -> {
			try {
				showLoginScreen();
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

	private void register(String username, String password, String firstName, String lastName) throws IOException {
		// Implement the registration logic here
		//registering users
		try {
			String query = "INSERT INTO users (username, password, first_name, last_name) VALUES (?, ?, ?, ?)";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, username);
			statement.setString(2, password);
			statement.setString(3, firstName);
			statement.setString(4, lastName);

			statement.executeUpdate();

			showAlert(Alert.AlertType.INFORMATION, "Registration Successful",
					"Registration successful. You can now log in.");
			showLoginScreen(); // Return to the login screen after successful registration
		} catch (SQLException e) {
			showAlert(Alert.AlertType.ERROR, "Registration Failed",
					"Username already exists. Please choose a different username.");
		}
	}

	private void login() {
		String username = usernameField.getText();
		String password = passwordField.getText();

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

				showDashboard(fullName, username);
			} else {
				// Failed login, show an error message
				showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void showDashboard(String fullName, String username) {
		// Create the dashboard layout
		VBox dashboardLayout = new VBox(10);
		dashboardLayout.setStyle("-fx-background-color: #b0e0e6;");
		dashboardLayout.setPadding(new Insets(20, 20, 20, 20));

		Label welcomeLabel = new Label("Welcome, " + fullName);

		welcomeLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
		// for retriving top posts
		Button retrieveTopPostsButton = new Button("Retrieve Top Posts");
		retrieveTopPostsButton.setOnAction(e -> showRetrieveTopLikedPostsScreen(fullName, username));
		Button retrievePostButton = new Button("Retrieve Post by ID");
		retrievePostButton.setOnAction(e -> showRetrievePostScreen(fullName, username));

		Button editProfileButton = new Button("Edit Profile");
		editProfileButton.setOnAction(e -> showEditProfileScreen(fullName, username));

		dashboardLayout.getChildren().addAll(welcomeLabel, retrieveTopPostsButton, retrievePostButton,
				editProfileButton);

		Scene dashboardScene = new Scene(dashboardLayout, 300, 250);
		primaryStage.setScene(dashboardScene);
	}

	private void showEditProfileScreen(String fullName, String username) {
		// Create a new JavaFX scene for profile editing
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
		saveButton.setOnAction(e -> saveProfileChanges(username, passwordField.getText(), newUsernameField.getText(),
				firstNameField.getText(), lastNameField.getText()));

		editProfileLayout.getChildren().addAll(titleLabel, CurrentUsernameLabel, NewUsernameLabel, newUsernameField,
				PasswordLabel, passwordField, FirstnameLabel, firstNameField, LastnameLabel, lastNameField, saveButton);

		Scene editProfileScene = new Scene(editProfileLayout, 400, 300);
		primaryStage.setScene(editProfileScene);
	}

	private void saveProfileChanges(String currentUsername, String newPassword, String newUsername, String newFirstName,
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
				showAlert(Alert.AlertType.INFORMATION, "Profile Updated", "Profile updated successfully.");
				showDashboard(currentUsername, newUsername); // Return to the dashboard with the updated username
			} else {
				showAlert(Alert.AlertType.ERROR, "Profile Update Failed", "Failed to update the profile.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while updating the profile.");
		}
	}

	private void showRetrievePostScreen(String fullname, String username) {
		// Create a new JavaFX scene for post retrieval
		VBox retrievePostLayout = new VBox(10);
		retrievePostLayout.setStyle("-fx-background-color: #b0e0e6;");
		retrievePostLayout.setPadding(new Insets(20, 20, 20, 20));

		Label retrievePostLabel = new Label("Retrieve Post by ID:");
		retrievePostLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

		// Create input fields
		TextField postIdField = new TextField();
		Button retrieveButton = new Button("Retrieve");
		retrieveButton.setOnAction(e -> retrievePost(postIdField.getText()));

		// Create a TextArea to display the retrieved post
		postTextArea = new TextArea();
		postTextArea.setEditable(false); // Prevent editing

		// Add a back button to return to the dashboard
		Button backButton = new Button("Back");
		backButton.setOnAction(e -> showDashboard(fullname, username));

		retrievePostLayout.getChildren().addAll(retrievePostLabel, postIdField, retrieveButton, postTextArea,
				backButton);

		Scene retrievePostScene = new Scene(retrievePostLayout, 400, 400);
		primaryStage.setScene(retrievePostScene);
	}

	private void retrievePost(String postId) {
		topPostsListView = new ListView<>();
		// Implement the logic to retrieve the post from the database based on the
		// entered ID
		// Query the database using the postId and populate the postTextArea with the
		// retrieved post data
		// Display an error message if the post is not found
		try (Connection connection = DriverManager.getConnection(DB_URL);
				PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM posts WHERE id = ?")) {

			preparedStatement.setString(1, postId);
			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				int id = resultSet.getInt("id");
				String content = resultSet.getString("content");
				String author = resultSet.getString("author");
				int likes = resultSet.getInt("likes");
				int shares = resultSet.getInt("shares");
				String dateTime = resultSet.getString("dateTime");

				// Display all post information in the TextArea
				String retrievedPostText = "ID: " + id + "\nContent: " + content + "\nAuthor: " + author + "\nLikes: "
						+ likes + "\nShares: " + shares + "\nDateTime: " + dateTime;
				postTextArea.setText(retrievedPostText);
			} else {
				showAlert(Alert.AlertType.ERROR, "Login Failed", "post id not found.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			postTextArea.setText("Error retrieving post");
		}
		// Example code to set the retrieved post in the TextArea (replace with your
		// actual database query):
	}

	private void showRetrieveTopLikedPostsScreen(String fullName, String username) {
		// Create a new JavaFX scene for retrieving top posts
		topPostsListView = new ListView<>();
		VBox retrieveTopPostsLayout = new VBox(10);
		retrieveTopPostsLayout.setStyle("-fx-background-color: #b0e0e6;");
		retrieveTopPostsLayout.setPadding(new Insets(20, 20, 20, 20));

		Label retrieveTopPostsLabel = new Label("Most Liked Posts");
		retrieveTopPostsLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

		// Create input field for N
		TextField numberOfPostsField = new TextField();
		numberOfPostsField.setPromptText("Enter the number of top posts (N)");

		Button retrieveButton = new Button("Retrieve");
		retrieveButton.setOnAction(e -> retrieveTopPosts(Integer.parseInt(numberOfPostsField.getText())));

		// Add a back button to return to the dashboard
		Button backButton = new Button("Back");
		backButton.setOnAction(e -> showDashboard(fullName, username));

		retrieveTopPostsLayout.getChildren().addAll(retrieveTopPostsLabel, numberOfPostsField, retrieveButton,
				topPostsListView, backButton);

		Scene retrieveTopPostsScene = new Scene(retrieveTopPostsLayout, 400, 400);
		primaryStage.setScene(retrieveTopPostsScene);
	}

	private void retrieveTopPosts(int numberOfPosts) {

		// Assuming you have a list of top posts retrieved from the database
		List<String> topPosts = retrieveTopPostsFromDatabase(numberOfPosts);

		// Print the retrieved posts to check if they are correct
		for (String post : topPosts) {
			System.out.println(post);
		}

		// Populate the ListView with the retrieved top posts
		topPostsListView.getItems().addAll(topPosts);
	}

	private List<String> retrieveTopPostsFromDatabase(int numberOfPosts) {
		List<String> topPosts = new ArrayList<>();

		try (Connection connection = DriverManager.getConnection(DB_URL);
				PreparedStatement preparedStatement = connection
						.prepareStatement("SELECT * FROM posts ORDER BY likes DESC LIMIT ?")) {

			preparedStatement.setInt(1, numberOfPosts);
			ResultSet resultSet = preparedStatement.executeQuery();
			int count = 1;
			while (resultSet.next()) {

				int id = resultSet.getInt("id");
				String content = resultSet.getString("content");
				String author = resultSet.getString("author");
				int likes = resultSet.getInt("likes");
				int shares = resultSet.getInt("shares");
				String dateTime = resultSet.getString("dateTime");

				// Create a formatted string for each top post
				String topPost = "ID: " + id + "\nContent: " + content + "\nAuthor: " + author + "\nLikes: " + likes
						+ "\nShares: " + shares + "\nDateTime: " + dateTime;
				topPosts.add("top  " + Integer.toString(count) + "  post");
				topPosts.add(topPost);
				count++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return topPosts;
	}

	private void showAlert(Alert.AlertType alertType, String title, String content) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setContentText(content);
		alert.showAndWait();
	}

}
