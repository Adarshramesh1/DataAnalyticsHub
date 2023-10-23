package application;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import javafx.scene.control.TextInputDialog;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
//Prompts the user to input a Post ID, retrieves post details from the database,
//and exports them to a CSV file.

public class ExportToCsvController {
	public void exportToCSV(Connection connection, Stage primaryStage) {
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
