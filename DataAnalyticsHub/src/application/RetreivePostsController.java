package application;

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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RetreivePostsController {
	public Stage primaryStage;
	public TextArea postTextArea;
	private static final String DB_URL = "jdbc:sqlite:social_media_analytics.db";

	public void showRetrievePostScreen(Connection connection, Stage primaryStage, String fullname, String username) {
		this.primaryStage = primaryStage;
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
		backButton.setOnAction(e -> new DashBoardController().showDashboard(connection, primaryStage, fullname, username));

		retrievePostLayout.getChildren().addAll(retrievePostLabel, postIdField, retrieveButton, postTextArea,
				backButton);

		Scene retrievePostScene = new Scene(retrievePostLayout, 400, 400);
		primaryStage.setScene(retrievePostScene);
	}

	public void retrievePost(String postId) {
		
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
				//clearing previous values before displaying new values
				postTextArea.clear();
				postTextArea.setText(retrievedPostText);
			} else {
				new Main().showAlert(primaryStage, Alert.AlertType.ERROR, "Login Failed", "post id not found.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			postTextArea.setText("Error retrieving post");
		}
		// Example code to set the retrieved post in the TextArea (replace with your
		// actual database query):
	}

}
