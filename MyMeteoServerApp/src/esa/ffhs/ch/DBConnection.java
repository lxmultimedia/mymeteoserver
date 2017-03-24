package esa.ffhs.ch;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class DBConnection {

	public static DBConnection instance = new DBConnection();
	private static Connection dbconnection = null;
	public static final String URL = "jdbc:mysql://localhost:3306/mymeteo";
	//debug
	//public static final String URL = "jdbc:mysql://1163.vps.hostfactory.ch:3306/mymeteo";
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
			System.out.println("Connected to DB : " + URL);
			System.out.println("");
		} catch (SQLException e) {
			System.out.println("ERROR: Unable to Connect to Database.");
		}

		dbconnection = connection;
	}

	public static Connection getConnection() {
		return dbconnection;
	}
	
	public void DeleteOldRows() throws SQLException {
		Connection connection = null;
		Statement statement = null;

		connection = DBConnection.getConnection();
		statement = connection.createStatement();

		Timestamp timeStampNow = new Timestamp(System.currentTimeMillis());

		Timestamp a = ServerMain.DateCheck;

		Timestamp b = timeStampNow;

		long diff = a.getTime() - b.getTime();

		int diffHours = -(int) ((diff / (1000 * 60 * 60)) % 24);

		System.out.println("Diff hours: " + diffHours);
		
		if (diffHours > 23) 
		{
			ServerMain.DateCheck = timeStampNow;
					
			String queryEmptyTable = "DELETE FROM yahoodata";
			try {

				int numRowsChanged = statement.executeUpdate(queryEmptyTable);
				System.out.println("-----------------");
				System.out.println("Delete From 'yahoodata' executed : " + numRowsChanged + " rows");
				System.out.println("-----------------");
				System.out.println();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public void writeJSONObject(String json) throws SQLException {

		Connection connection = null;
		Statement statement = null;

		connection = DBConnection.getConnection();
		statement = connection.createStatement();


		String query = "INSERT INTO yahoodata (jsonobject,jo_datetime) VALUES ('" + json + "',CURRENT_TIMESTAMP())";
		try {
			int numRowsChanged = statement.executeUpdate(query);
			System.out.println("City Data written to DB : " + numRowsChanged + " rows");
			System.out.println();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

}