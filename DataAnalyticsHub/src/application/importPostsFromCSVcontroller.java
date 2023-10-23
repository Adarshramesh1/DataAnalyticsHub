package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class importPostsFromCSVcontroller {
	 public void importSocialMediaPostsFromCSV(Connection connection, Stage primaryStage) throws SQLException {
		    FileChooser fileChooser = new FileChooser();
		    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
		    File selectedFile = fileChooser.showOpenDialog(primaryStage);
		    

		    if (selectedFile != null) {
		        boolean importSuccess=false;
				try {
					importSuccess = importPostsFromCSV(selectedFile, connection,primaryStage);
				} catch (Exception e) {
					
					e.printStackTrace();
				}
				if (importSuccess) {
				    new Main().showAlert(primaryStage, Alert.AlertType.INFORMATION, "Import Successful", "Social media posts were imported successfully.");
				} else {
				    new Main().showAlert(primaryStage, Alert.AlertType.ERROR, "Import Failed", "An error occurred during import.");
				}
		    }
		}
	 
	 
	 public boolean importPostsFromCSV(File csvFile, Connection connection, Stage primaryStage) {
		    try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
		        String line;
		        System.out.println(csvFile);
		        // Skip the header row if it exists
		        boolean isFirstRow = true;
		        while ((line = reader.readLine()) != null) {
		            if (isFirstRow) {
		                isFirstRow = false;
		                continue; // Skip the header row
		            }

		            String[] parts = line.split(",");
		            if (parts.length == 6) {
		                int id = Integer.parseInt(parts[0]);
		                String content = parts[1];
		                String author = parts[2];
		                int likes = Integer.parseInt(parts[3]);
		                int shares = Integer.parseInt(parts[4]);
		                String dateTime = parts[5];
		                

		                // Insert the data into the database
		                String insertQuery = "INSERT INTO posts (id, content, author, likes, shares, dateTime) " +
		                        "VALUES (?, ?, ?, ?, ?, ?)";

		                try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
		                    preparedStatement.setInt(1, id);
		                    preparedStatement.setString(2, content);
		                    preparedStatement.setString(3, author);
		                    preparedStatement.setInt(4, likes);
		                    preparedStatement.setInt(5, shares);
		                    preparedStatement.setString(6, dateTime);

		                    preparedStatement.executeUpdate();
		                } catch (SQLException e) {
		                
		                    return false; // Return false if an error occurs
		                }
		            } else {
		                // Handle CSV format errors
		                return false;
		            }
		        }
		        
		        return true; // Return true if the import is successful
		    } catch (IOException e) {
		        e.printStackTrace();
		        return false; // Return false if an error occurs
		    }
		}
}
