package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.stage.Stage;

public class DisplayOfSharesController {

	 public void displayShareDistributionPieChart(Connection connection, Stage primaryStage) {
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

	 public List<Integer> calculateShareDistribution(Connection connection) {
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
