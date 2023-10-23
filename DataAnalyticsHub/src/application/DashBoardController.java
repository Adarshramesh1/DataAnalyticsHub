package application;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DashBoardController {
	public Stage primaryStage;

	public void showDashboard(Connection connection, Stage primaryStage, String fullName, String username) {
		// Create the dashboard layout
		//0 is when user is not a vip
		//1 is when user is a vip customer
		int isVIP = new VipUpgradeController().checkVIPStatus(connection, username);
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
		Button dataVisualizationButton = new Button("Data Visualization");
        dataVisualizationButton.setOnAction(e -> {
            new DisplayOfSharesController().displayShareDistributionPieChart(connection, primaryStage);
        });
        Button bulkImportButton = new Button("Bulk Import Posts");
        bulkImportButton.setOnAction(e -> {
            try {
				new importPostsFromCSVcontroller().importSocialMediaPostsFromCSV(connection, primaryStage);
			} catch (SQLException e1) {
				
				e1.printStackTrace();
			}
        });
		exportPostButton.setOnAction(e -> new ExportToCsvController().exportToCSV(connection,primaryStage));
		Button VipUpgrage = new Button("Upgrade to VIP user");
		VipUpgrage.setOnAction(e -> {
			try {
				new VipUpgradeController().handleVIPUpgrade(primaryStage, connection,username);
			} catch (IOException e1) {
				
				e1.printStackTrace();
			}
		});
		if (isVIP==0) {
		dashboardLayout.getChildren().addAll(welcomeLabel, retrieveTopPostsButton, retrievePostButton, deletePostButton,
				editProfileButton, addPostsButton, exportPostButton,VipUpgrage, logoutButton);
		}
		else {
			dashboardLayout.getChildren().addAll(welcomeLabel, retrieveTopPostsButton, retrievePostButton, deletePostButton,
					editProfileButton, addPostsButton, exportPostButton,VipUpgrage,dataVisualizationButton,bulkImportButton, logoutButton);
		}

		Scene dashboardScene = new Scene(dashboardLayout, 400, 545);
		primaryStage.setScene(dashboardScene);
	}
	
	
	
	 
	



	
}
