package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TopPostsController {
	public Stage primaryStage;
	private static final String DB_URL = "jdbc:sqlite:social_media_analytics.db";
	private ListView<String> topPostsListView;

	public void showRetrieveTopLikedPostsScreen(Connection connection, Stage primaryStage, String fullName,
			String username) {
		this.primaryStage = primaryStage;
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
		backButton.setOnAction(
				e -> new DashBoardController().showDashboard(connection, primaryStage, fullName, username));

		retrieveTopPostsLayout.getChildren().addAll(retrieveTopPostsLabel, numberOfPostsField, retrieveButton,
				topPostsListView, backButton);

		Scene retrieveTopPostsScene = new Scene(retrieveTopPostsLayout, 400, 400);
		primaryStage.setScene(retrieveTopPostsScene);
	}

	public void retrieveTopPosts(int numberOfPosts) {

		List<String> topPosts = retrieveTopPostsFromDatabase(numberOfPosts);

		// clearing previous values before displaying new values
		topPostsListView.getItems().clear();
		// Populate the ListView with the retrieved top posts
		topPostsListView.getItems().addAll(topPosts);
	}

	public List<String> retrieveTopPostsFromDatabase(int numberOfPosts) {
		List<String> topPosts = new ArrayList<>();

		try (Connection connection = DriverManager.getConnection(DB_URL);
				PreparedStatement preparedStatement = connection
						.prepareStatement("SELECT * FROM posts ORDER BY likes DESC LIMIT ?")) {

			preparedStatement.setInt(1, numberOfPosts);
			ResultSet resultSet = preparedStatement.executeQuery();
			int count = 1;
			if (resultSet.next()) {

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
			} else {
				new Main().showAlert(primaryStage, Alert.AlertType.ERROR, "Access Failed", "posts not found.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return topPosts;
	}

}
