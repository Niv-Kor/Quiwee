package com.database;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;
import java.lang.Exception;

public class SQLModifier
{
	public static final int NaN = -999;
	
	private static SQLConnector connector;
	private static Statement statement;
	private static ResultSet resultSet;
	
	
	public static void init() {
		connector = new SQLConnector();
		statement = connector.connect();
	}
	
	public static void write(String query) {
		try { statement.executeUpdate(query); }
		catch (Exception e) { printError(query, "", false); }
	}
	
	public static int readINT(String query, String column) {
		try {
			resultSet = statement.executeQuery(query);
			if (resultSet.next()) return resultSet.getInt(column);
		}
		catch (Exception e) { printError(query, "integer", true); }
		return NaN;
	}
	
	public static List<Integer> readAllINT(String query, String column) {
		List<Integer> list = new ArrayList<Integer>();
		
		try {
			resultSet = statement.executeQuery(query);
			while (resultSet.next()) list.add(resultSet.getInt(column));
		}
		catch (Exception e) { printError(query, "integer", true); }
		return list;
	}
	
	public static double readDECIMAL(String query, String column, Class<?> c) {
		try {
			resultSet = statement.executeQuery(query);
			if (resultSet.next()) return resultSet.getDouble(column);
		}
		catch (Exception e) { printError(query, "decimal", true); }
		return NaN;
	}
	
	public static List<Double> readAllDECIMAL(String query, String column) {
		List<Double> list = new ArrayList<Double>();
		
		try {
			resultSet = statement.executeQuery(query);
			while (resultSet.next()) list.add(resultSet.getDouble(column));
		}
		catch (Exception e) { printError(query, "decimal", true); }
		return list;
	}
	
	public static String readVARCHAR(String query, String column) {
		try {
			resultSet = statement.executeQuery(query);
			if (resultSet.next()) return resultSet.getString(column);
		}
		catch (Exception e) { printError(query, "varchar", true); }
		return null;
	}
	
	public static List<String> readAllVARCHAR(String query, String column) {
		List<String> list = new ArrayList<String>();
		
		try {
			resultSet = statement.executeQuery(query);
			while (resultSet.next()) list.add(resultSet.getString(column));
		}
		catch (Exception e) { printError(query, "varchar", true); }
		return list;
	}
	
	public static boolean readBOOLEAN(String query, String column) {
		try {
			resultSet = statement.executeQuery(query);
			if (resultSet.next()) return resultSet.getBoolean(column);
		}
		catch (Exception e) { printError(query, "boolean", true); }
		return false;
	}
	
	public static List<Boolean> readAllBOOLEAN(String query, String column) {
		List<Boolean> list = new ArrayList<Boolean>();
		
		try {
			resultSet = statement.executeQuery(query);
			while (resultSet.next()) list.add(resultSet.getBoolean(column));
		}
		catch (Exception e) { printError(query, "boolean", true); }
		return list;
	}
	
	public static Date readDATE(String query, String column) {
		try {
			resultSet = statement.executeQuery(query);
			if (resultSet.next()) return resultSet.getDate(column);
		}
		catch (Exception e) { printError(query, "date", true); }
		return null;
	}
	
	public static List<Date> readAllDATE(String query, String column) {
		List<Date> list = new ArrayList<Date>();
		
		try {
			resultSet = statement.executeQuery(query);
			while (resultSet.next()) list.add(resultSet.getDate(column));
		}
		catch (Exception e) { printError(query, "date", true); }
		return list;
	}
	
	public static String buildQuery(String select, String from, String where, int equals) {
		return "SELECT " + select + " "
			 + "FROM " + from + " "
			 + "WHERE " + where + " = " + equals + ";";
	}
	
	public static String buildQuery(String select, String from, String where, double equals) {
		return "SELECT " + select + " "
			 + "FROM " + from + " "
			 + "WHERE " + where + " = " + equals + ";";
	}
	
	public static String buildQuery(String select, String from, String where, String equals) {
		return "SELECT " + select + " "
			 + "FROM " + from + " "
			 + "WHERE " + where + " = '" + equals + "';";
	}
	
	public static String buildQuery(String select, String from, String where, boolean equals) {
		return "SELECT " + select + " "
			 + "FROM " + from + " "
			 + "WHERE " + where + " = " + equals + ";";
	}
	
	public static void printError(String query, String dataType, boolean read) {
		String action = read ? "retrieve" : "write";
		if (dataType.equals("")) dataType = "the following";
		System.err.println("Could not " + action + " " + dataType + " data for the query: \n" + query + "\n\n");
		
	}
}