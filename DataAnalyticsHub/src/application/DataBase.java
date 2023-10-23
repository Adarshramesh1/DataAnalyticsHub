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
	
}
