package application;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DashBoardController {
	public Stage primaryStage;
	private static final String DB_URL = "jdbc:sqlite:social_media_analytics.db";

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
            displayShareDistributionPieChart(connection, primaryStage);
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
		Button VipUpgrage = new Button("uppgrade to Vip user");
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

		Scene dashboardScene = new Scene(dashboardLayout, 300, 250);
		primaryStage.setScene(dashboardScene);
	}
	
	
	
	 
	 private void displayShareDistributionPieChart(Connection connection, Stage primaryStage) {
		    // Fetch data to calculate share distribution
		    List<Integer> shareCounts = calculateShareDistribution(connection);

		    PieChart pieChart = new PieChart();
		    pieChart.getData().addAll(
		        new PieChart.Data("0-99 shares", shareCounts.get(0)),
		        new PieChart.Data("100-999 shares", shareCounts.get(1)),
		        new PieChart.Data("1000+ shares", shareCounts.get(2))
		    );

		    Scene pieChartScene = new Scene(pieChart, 600, 400);
		    Stage pieChartStage = new Stage();
		    pieChartStage.setScene(pieChartScene);
		    pieChartStage.setTitle("Share Distribution Pie Chart");
		    pieChartStage.show();
		}

	 private List<Integer> calculateShareDistribution(Connection connection) {
		    List<Integer> shareCounts = new ArrayList<>(3); // Initialize a list to store the counts

		    // SQL query to fetch share counts in three categories: 0-99, 100-999, and 1000+
		    String query = "SELECT " +
		            "SUM(CASE WHEN shares BETWEEN 0 AND 99 THEN 1 ELSE 0 END) AS range1, " +
		            "SUM(CASE WHEN shares BETWEEN 100 AND 999 THEN 1 ELSE 0 END) AS range2, " +
		            "SUM(CASE WHEN shares >= 1000 THEN 1 ELSE 0 END) AS range3 " +
		            "FROM posts";

		    try (PreparedStatement preparedStatement = connection.prepareStatement(query);
		         ResultSet resultSet = preparedStatement.executeQuery()) {
		        if (resultSet.next()) {
		            // Retrieve counts from the query result
		            int countRange1 = resultSet.getInt("range1");
		            int countRange2 = resultSet.getInt("range2");
		            int countRange3 = resultSet.getInt("range3");

		            shareCounts.add(countRange1);
		            shareCounts.add(countRange2);
		            shareCounts.add(countRange3);
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }

		    return shareCounts;
		}

	
		




	 
		    
		    
		



	
}
