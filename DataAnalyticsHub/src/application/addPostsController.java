package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class addPostsController {
	private static final String DB_URL = "jdbc:sqlite:social_media_analytics.db";
	public Stage primaryStage;
	public void showAddPostScreen(Connection connection,Stage primaryStage,String fullName,String username) {
	    this.primaryStage = primaryStage;

	    // Create a new JavaFX scene for adding a post
	    VBox addPostLayout = new VBox(10);
	    addPostLayout.setStyle("-fx-background-color: #b0e0e6;");
	    addPostLayout.setPadding(new Insets(20, 20, 20, 20));

	    Label addPostLabel = new Label("Add Social Media Post");
	    addPostLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

	    // Create input fields
	    TextField idField = new TextField();
	    idField.setPromptText("Post ID");
	    TextField contentField = new TextField();
	    contentField.setPromptText("Post Content");
	    TextField authorField = new TextField();
	    authorField.setPromptText("Author");
	    TextField likesField = new TextField();
	    likesField.setPromptText("Likes");
	    TextField sharesField = new TextField();
	    sharesField.setPromptText("Shares");
	    TextField dateTimeField = new TextField();
	    dateTimeField.setPromptText("Date and Time");

	    Button addButton = new Button("Add Post");
	    addButton.setOnAction(e -> addSocialPost(connection,idField.getText(), contentField.getText(),
	            authorField.getText(), likesField.getText(), sharesField.getText(), dateTimeField.getText(),fullName,username));

	    Button backButton = new Button("Back to Dashboard");
	    backButton.setOnAction(e -> new DashBoardController().showDashboard(connection, primaryStage, fullName, username));

	    addPostLayout.getChildren().addAll(addPostLabel, idField, contentField, authorField, likesField, sharesField,
	            dateTimeField, addButton, backButton);

	    Scene addPostScene = new Scene(addPostLayout, 400, 400);
	    primaryStage.setScene(addPostScene);
	}
	
	private void addSocialPost(Connection connection, String id, String content, String author, String likes, String shares, String dateTime, String fullName, String username) {
	    // Implement validation here (e.g., checking if likes and shares are numeric, ensuring ID uniqueness, etc.)
	    try {
	        // Convert likes and shares to integers
	        int likesInt = Integer.parseInt(likes);
	        int sharesInt = Integer.parseInt(shares);

	        // Perform additional validations as needed

	        // Add the post to the database (Replace with your actual database insertion logic)
	        insertPost(connection,id, content, author, likesInt, sharesInt, dateTime,fullName,username);

	        
	    } catch (NumberFormatException e) {
	    	new Main().showAlert(primaryStage, Alert.AlertType.ERROR, "Invalid Input",
	                "Likes and Shares should be numeric.");
	    } catch (Exception e) {
	    	new Main().showAlert(primaryStage, Alert.AlertType.ERROR, "Error",
	                "An error occurred while adding the post.");
	        e.printStackTrace();
	    }
	}
	
	private void insertPost(Connection connection, String id, String content, String author, int likes, int shares, String dateTime, String fullName, String username) throws IOException {
	    String query = "INSERT INTO posts (id, content, author, likes, shares, dateTime) VALUES (?, ?, ?, ?, ?, ?)";

	    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
	        preparedStatement.setString(1, id);
	        preparedStatement.setString(2, content);
	        preparedStatement.setString(3, author);
	        preparedStatement.setInt(4, likes);
	        preparedStatement.setInt(5, shares);
	        preparedStatement.setString(6, dateTime);

	        int rowsInserted = preparedStatement.executeUpdate();
	        if (rowsInserted > 0) {
	        	// Optionally, show a success message or return to the dashboard
	        	new Main().showAlert(primaryStage, Alert.AlertType.INFORMATION, "Addition Successful",
						"Addition of post successful. You can now view the posts from dashboard.");
	        	new DashBoardController().showDashboard(connection, primaryStage, fullName, username);
	        }
	        else {
	        	new Main().showAlert(primaryStage, Alert.AlertType.ERROR, "post couldn't be added to the database",
						"Failed to add the post.");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        System.err.println("Error: Unable to insert the post into the database.");
	    }
	}



}
