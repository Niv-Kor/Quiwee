package com.database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import com.mysql.cj.jdbc.exceptions.CommunicationsException;

public class SQLConnector {
	private final static String HOST = "jdbc:mysql://sql7.freemysqlhosting.net/sql7287323?useLegacyDatetimeCode=false&serverTimezone=UTC";
	private final static String USERNAME = "sql7287323";
	private final static String PASSWORD = "EQjYc7Nidj";
	
	private Connection connection;
	
	public Statement connect() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(HOST, USERNAME, PASSWORD);
			return connection.createStatement();
		}
		catch(CommunicationsException e) {
			System.err.println("Could not connect to MySQL database due to network failure.\n"
							 + "Please check your internet connection and try again.");
		}
		catch (Exception e) {
			System.err.println("Could not connect to MySQL database.");
			e.printStackTrace();
		}
		
		return null;
	}
}