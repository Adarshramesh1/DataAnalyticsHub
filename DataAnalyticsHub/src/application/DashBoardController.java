package application;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class DashBoardController {
	public Stage primaryStage;

	public void showDashboard(Connection connection, Stage primaryStage, String fullName, String username) {
		// Create the dashboard layout
		this.primaryStage = primaryStage;
		VBox dashboardLayout = new VBox(10);
		dashboardLayout.setStyle("-fx-background-color: #b0e0e6;");
		dashboardLayout.setPadding(new Insets(20, 20, 20, 20));

		Label welcomeLabel = new Label("Welcome, " + fullName);

		welcomeLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
		Button logoutButton = new Button("Logout");
		logoutButton.setOnAction(e -> {
			// Handle the logout action, such as closing the current session or showing a
			// login screen
			try {
				new LoginController().showLoginScreen(primaryStage);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		// for retriving top posts
		Button retrieveTopPostsButton = new Button("Retrieve Top Posts");
		retrieveTopPostsButton.setOnAction(e -> new TopPostsController().showRetrieveTopLikedPostsScreen(connection,
				primaryStage, fullName, username));
		Button retrievePostButton = new Button("Retrieve Post by ID");
		retrievePostButton.setOnAction(e -> new RetreivePostsController().showRetrievePostScreen(connection,
				primaryStage, fullName, username));
		Button deletePostButton = new Button("Delete Post");
		deletePostButton.setOnAction(
				e -> new DeletePostController().showDeleteConfirmation(connection, primaryStage, fullName, username));
		Button editProfileButton = new Button("Edit Profile");
		editProfileButton.setOnAction(
				e -> new EditProfileController().showEditProfileScreen(connection, primaryStage, fullName, username));
		Button addPostsButton = new Button("Add a Post");
		addPostsButton.setOnAction(
				e -> new addPostsController().showAddPostScreen(connection, primaryStage, fullName, username));
		Button exportPostButton = new Button("Export Post to CSV");
		exportPostButton.setOnAction(e -> exportToCSV(connection));

		dashboardLayout.getChildren().addAll(welcomeLabel, retrieveTopPostsButton, retrievePostButton, deletePostButton,
				editProfileButton, addPostsButton, exportPostButton, logoutButton);

		Scene dashboardScene = new Scene(dashboardLayout, 300, 250);
		primaryStage.setScene(dashboardScene);
	}

	private void exportToCSV(Connection connection) {
		// Show a dialog to input the post ID
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Export Post to CSV");
		dialog.setHeaderText("Enter the Post ID to export:");
		dialog.setContentText("Post ID:");

		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			String number = result.get();
			int postID = Integer.parseInt(number);

			// Retrieve post details based on the postID
			String query = "SELECT * FROM posts WHERE id = ?";

			try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
				preparedStatement.setInt(1, postID);
				ResultSet resultSet = preparedStatement.executeQuery();

				if (resultSet.next()) {
					int id = resultSet.getInt("id");
					String content = resultSet.getString("content");
					String author = resultSet.getString("author");
					int likes = resultSet.getInt("likes");
					int shares = resultSet.getInt("shares");
					String dateTime = resultSet.getString("dateTime");

					// Create a CSV line with post details
					String postDetails = id + "," + content + "," + author + "," + likes + "," + shares + ","
							+ dateTime;

					FileChooser fileChooser = new FileChooser();
					fileChooser.setTitle("CSV File Save to Local");
					fileChooser.setInitialFileName("exported_post.csv");
					File file = fileChooser.showSaveDialog(primaryStage);

					if (file != null) {
						// Write the post details to the selected file in CSV format
						try (PrintWriter writer = new PrintWriter(file)) {
							String headings = "Post ID,Content,Author,Likes,Shares,DateTime\n";
							writer.write(headings);
				
							writer.write(postDetails);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}
}
