package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class VipUpgradeController {

	public void handleVIPUpgrade(Stage primaryStage, Connection connection, String username) throws IOException {
	    // Display a confirmation dialog to ask the user if they want to upgrade to VIP
	    Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
	    confirmationAlert.setTitle("Upgrade to VIP");
	    confirmationAlert.setHeaderText("Do you want to upgrade to VIP?");
	    confirmationAlert.setContentText("Click OK to upgrade or Cancel to stay as a regular user.");

	    Optional<ButtonType> result = confirmationAlert.showAndWait();
	    if (result.isPresent() && result.get() == ButtonType.OK) {
	        // User has agreed to upgrade to VIP
	       upgradeToVIP(primaryStage,connection,username); // Call the upgradeToVIP function
	       
	    } else {
	        // User chose not to upgrade
	    	new Main().showAlert(primaryStage, Alert.AlertType.INFORMATION, "VIP Upgrade Failed", "user declined to be a vip user");
	    }
	}
	
	public void upgradeToVIP(Stage primaryStage, Connection connection, String username) throws IOException {
	    // Execute an SQL UPDATE statement to set vip_status to 'true' for the given username
	    String updateQuery = "UPDATE users SET VIP_USER = 1 WHERE username = ?";
	    
	    try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
	        preparedStatement.setString(1, username);
	        
	        int rowsUpdated = preparedStatement.executeUpdate();
	        
	        if (rowsUpdated > 0) {
	            // VIP upgrade was successful
	        	new Main().showAlert(primaryStage, Alert.AlertType.INFORMATION, "VIP Upgrade Successful", "Congratulations! You are now a VIP user.LOGIN again to avail the features of vip customer");
	        	new LoginController().showLoginScreen(primaryStage);
	        } else {
	            // No rows were updated, which means the user with the given username was not found.
	        	new Main().showAlert(primaryStage, Alert.AlertType.ERROR, "upgradation Failed",
						"Given username was not found!! ");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        // Handle any database errors
	    }
	}
	
	
	 public int checkVIPStatus(Connection connection, String username) {
	        try {
	            String query = "SELECT VIP_USER FROM users WHERE username = ?";
	            PreparedStatement preparedStatement = connection.prepareStatement(query);
	            preparedStatement.setString(1, username);

	            ResultSet resultSet = preparedStatement.executeQuery();
	            
	            if (resultSet.next()) {
	                // Retrieve the VIP status from the query result
	                int isVIP = resultSet.getInt("VIP_USER");
	                return isVIP;
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        
	        // If an error occurred or the user was not found, assume the user is not a VIP
	        return 0;
	    }
}
