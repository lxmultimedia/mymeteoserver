package esa.ffhs.ch;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class DBConnection {

	public static DBConnection instance = new DBConnection();
	private static Connection dbconnection = null;
	public static final String URL = "jdbc:mysql://localhost:3306/mymeteo";
	public static final String USER = "root";
	public static final String PASSWORD = "***REMOVED***";
	public static final String DRIVER_CLASS = "com.mysql.jdbc.Driver";

	DBConnection() {
		try {
			Class.forName(DRIVER_CLASS);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void createConnection() {

		if (dbconnection != null) {
			return;
		}

		Connection connection = null;

		try {
			connection = DriverManager.getConnection(URL, USER, PASSWORD);
			System.out.println("Connected.");
			System.out.println("");
		} catch (SQLException e) {
			System.out.println("ERROR: Unable to Connect to Database.");
		}

		dbconnection = connection;
	}

	public static Connection getConnection() {
		return dbconnection;
	}

	public void writeJSONObject(String json) {

		Timestamp timeStampNow = new Timestamp(System.currentTimeMillis());

		Timestamp a = ServerMain.DateCheck;

		Timestamp b = timeStampNow;

		long diff = b.getTime() - a.getTime();

		int diffHours = (int) ((diff / (1000 * 60 * 60)) % 24);


		if (diffHours > 24) {
			
			ServerMain.DateCheck = timeStampNow;
			
			ResultSet rs = null;
			Connection connection = null;
			Statement statement = null;

			String queryEmptyTable = "DELETE FROM yahoodata";
			try {
				connection = DBConnection.getConnection();
				statement = connection.createStatement();
				int numRowsChanged = statement.executeUpdate(queryEmptyTable);
				System.out.println("Delete From 'yahoodata' executed.");
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			
			String query = "INSERT INTO yahoodata (jsonobject,jo_datetime) VALUES ('" + json + "',CURRENT_TIMESTAMP())";
			try {
				connection = DBConnection.getConnection();
				statement = connection.createStatement();
				int numRowsChanged = statement.executeUpdate(query);
				System.out.println("City Data written to DB.");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

}