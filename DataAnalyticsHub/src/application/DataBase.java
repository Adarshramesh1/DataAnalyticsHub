package application;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBase {

	private static final String DB_URL = "jdbc:sqlite:social_media_analytics.db";

	private Connection connection;
	
	public void createUserDatabase() {
		try {
			connection = DriverManager.getConnection(DB_URL);
			String query = "CREATE TABLE IF NOT EXISTS users (" + "username TEXT UNIQUE NOT NULL,"
					+ "password TEXT NOT NULL," + "first_name TEXT NOT NULL," + // Added column for first name
					"last_name TEXT NOT NULL," + "VIP_USER INT NOT NULL DEFAULT 0)"; 
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void createPostsDatabase() {
		try {
			connection = DriverManager.getConnection(DB_URL);
			String query = "CREATE TABLE IF NOT EXISTS posts (" + "id INTEGER UNIQUE NOT NULL,"
					+ "content TEXT NOT NULL," + "author TEXT NOT NULL," + "likes INTEGER NOT NULL,"
					+ "shares INTEGER NOT NULL," + "dateTime TEXT NOT NULL)";
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void loadPostsDatabase() throws FileNotFoundException, IOException, SQLException {

		String csvFilePath = "C:\\Users\\adars\\OneDrive\\Desktop\\ADVANced programming\\posts.csv"; // Replace with
																										// your CSV file
																										// path
		connection = DriverManager.getConnection(DB_URL);
		try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
			String line;
			boolean isFirstRow = true;
			while ((line = reader.readLine()) != null) {
				if (isFirstRow) {
					isFirstRow = false;
					continue; // Skip the header row
				}

				String[] parts = line.split(",");
				int id = Integer.parseInt(parts[0]);
				String content = parts[1];
				String author = parts[2];
				int likes = Integer.parseInt(parts[3]);
				int shares = Integer.parseInt(parts[4]);
				String dateTime = parts[5];
				String insertQuery = "INSERT OR REPLACE INTO posts (id, content, author, likes, shares, dateTime) VALUES (?, ?, ?, ?, ?, ?)";

				PreparedStatement preparedStatement;
				try {
					preparedStatement = connection.prepareStatement(insertQuery);
					preparedStatement.setInt(1, id);
					preparedStatement.setString(2, content);
					preparedStatement.setString(3, author);
					preparedStatement.setInt(4, likes);
					preparedStatement.setInt(5, shares);
					preparedStatement.setString(6, dateTime);

					// Execute the INSERT statement
					preparedStatement.executeUpdate();

				} catch (SQLException e) {
					e.printStackTrace();
				}

			}
		}

	}
}
