package application;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



public class DeletePostController {
	private static final String DB_URL = "jdbc:sqlite:social_media_analytics.db";

	    public void showDeleteConfirmation(Connection connection, Stage primaryStage, String fullName, String username) {
	        Stage confirmationStage = new Stage();
	        confirmationStage.setTitle("Delete Confirmation");

	        VBox confirmationLayout = new VBox(10);
	        confirmationLayout.setStyle("-fx-background-color: #b0e0e6;");
	        confirmationLayout.setPadding(new Insets(20, 20, 20, 20));

	        Label confirmationLabel = new Label("Enter the Post ID to delete:");
	        confirmationLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

	        TextField postIdField = new TextField();
	        postIdField.setPromptText("Post ID");

	        Button confirmButton = new Button("Confirm");
	        confirmButton.setOnAction(e -> confirmDeletion(connection,primaryStage, confirmationStage, postIdField.getText(),fullName,username));

	        Button cancelButton = new Button("Cancel");
	        cancelButton.setOnAction(e -> confirmationStage.close());

	        confirmationLayout.getChildren().addAll(confirmationLabel, postIdField, confirmButton, cancelButton);

	        Scene confirmationScene = new Scene(confirmationLayout, 400, 200);
	        confirmationStage.setScene(confirmationScene);
	        confirmationStage.show();
	    }

	    private void confirmDeletion(Connection connection, Stage primaryStage, Stage confirmationStage, String postId, String fullName, String username) {
	        // Fetch post details based on postId and show them for confirmation
	        String postDetails = fetchPostDetails(postId);
	        
	        if (postDetails != null) {
	            // Create a confirmation dialog with post details
	            Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
	            confirmationDialog.setTitle("Confirm Deletion");
	            confirmationDialog.setHeaderText("Are you sure you want to delete this post?");
	            confirmationDialog.setContentText(postDetails);

	            confirmationDialog.showAndWait().ifPresent(response -> {
	                if (response == ButtonType.OK) {
	                    // User confirmed deletion
	                    try {
							deletePost(connection,primaryStage,postId,fullName,username);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	                    confirmationStage.close();
	                    // Update the main screen or perform any other necessary actions
	                }
	            });
	        } else {
	            new Main().showAlert(primaryStage, Alert.AlertType.ERROR, "Post not found", "Post with ID " + postId + " not found.");
	        }
	    }

	   

	    private String fetchPostDetails(String postId) {
	        String postDetails = null;
	        
	        // Connect to the database (You should replace this with your actual database connection logic)
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

	                postDetails = "ID: " + id + "\nContent: " + content + "\nAuthor: " + author +
	                        "\nLikes: " + likes + "\nShares: " + shares + "\nDateTime: " + dateTime;
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        
	        return postDetails;
	    }

	    private void deletePost(Connection connection, Stage primaryStage, String postId, String fullName, String username) throws IOException {
	        // Connect to the database and delete the post
	        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM posts WHERE id = ?")) {
	            preparedStatement.setString(1, postId);
	            int rowsAffected = preparedStatement.executeUpdate();

	            if (rowsAffected > 0) {
	                new Main().showAlert(primaryStage, Alert.AlertType.INFORMATION, "Post with ID " + postId + " has been deleted.",
	    					"Deletion successful");
	    			new DashBoardController().showDashboard(connection, primaryStage, fullName, username); 
	            } else {
	            	new Main().showAlert(primaryStage, Alert.AlertType.ERROR, "deletion Failed",
	    					"Username already exists. Please choose a different username.");
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            // Handle any database errors
	        }
	    }


	  
	}


