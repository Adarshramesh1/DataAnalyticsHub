package application;

import java.sql.Connection;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DashBoardController {
	public Stage primaryStage;
	public void showDashboard(Connection connection, Stage primaryStage,String fullName, String username) {
		// Create the dashboard layout
		this.primaryStage=primaryStage;
		VBox dashboardLayout = new VBox(10);
		dashboardLayout.setStyle("-fx-background-color: #b0e0e6;");
		dashboardLayout.setPadding(new Insets(20, 20, 20, 20));

		Label welcomeLabel = new Label("Welcome, " + fullName);

		welcomeLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
		// for retriving top posts
		Button retrieveTopPostsButton = new Button("Retrieve Top Posts");
		retrieveTopPostsButton.setOnAction(e -> new TopPostsController().showRetrieveTopLikedPostsScreen(connection,primaryStage,fullName, username));
		Button retrievePostButton = new Button("Retrieve Post by ID");
		retrievePostButton.setOnAction(e -> new RetreivePostsController().showRetrievePostScreen(connection,primaryStage,fullName,username));
		Button deletePostButton = new Button("Delete Post");
        deletePostButton.setOnAction(e -> new DeletePostController().showDeleteConfirmation(connection,primaryStage,fullName,username));
		Button editProfileButton = new Button("Edit Profile");
		editProfileButton.setOnAction(e -> new Main().showEditProfileScreen(connection, primaryStage,fullName,username));

		dashboardLayout.getChildren().addAll(welcomeLabel, retrieveTopPostsButton, retrievePostButton,deletePostButton,
				editProfileButton);

		Scene dashboardScene = new Scene(dashboardLayout, 300, 250);
		primaryStage.setScene(dashboardScene);
	}
}
