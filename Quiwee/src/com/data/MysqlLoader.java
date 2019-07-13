package com.data;
import java.sql.SQLException;
import java.util.List;
import javaNK.util.data.DataType;
import javaNK.util.data.MysqlColumn;
import javaNK.util.data.MysqlModifier;

public class MysqlLoader
{
	private static String userKey;
	
	public static void init(String key) {
		userKey = new String(key);
	}
	
	public static List<Object> pullAll(Pullable column) {
		//try to pull an existing list of that column
		List<Object> list = column.getList();
		
		//create a new list
		if (list == null) {
			DataType dataType = column.getColumn().getDataType();
			String columnName = column.getColumn().getName();
			String tableName = column.getTableName();
			list = dataType.getAllValues(getOverallQuery(tableName), columnName);
			column.setList(list);
		}
		
		return list;
	}
	
	public static Object[][] getRows(Class<? extends Pullable> pullableClass) {
		Pullable[] enumConstants = pullableClass.getEnumConstants();
		return getRows(pullableClass, getOverallQuery(enumConstants[0].getTableName()));
	}
	
	public static Object[][] getRows(Class<? extends Pullable> pullableClass, String query) {
		Pullable[] enumConstants = pullableClass.getEnumConstants();
		MysqlColumn[] columns = new MysqlColumn[enumConstants.length];
		
		for (int i = 0; i < columns.length; i++)
			columns[i] = enumConstants[i].getColumn();
		
		try { return MysqlModifier.getRows(query, columns); }
		catch (SQLException ex) { return new Object[0][enumConstants.length]; }
	}
	
	public static Object[][] getKeywordRows(Class<? extends Pullable> pullableClass, String keyword, Pullable... fields) {
		Pullable[] enumConstants = pullableClass.getEnumConstants();
		String tableName = enumConstants[0].getTableName();
		char firstChar = tableName.charAt(0);
		String query = getOverallQuery(tableName);
		
		//build query
		if (keyword != null && !keyword.equals("")) {
			query = query.concat("AND (");
			for (int i = 0; i < fields.length; i++) {
				query = query.concat(firstChar + "." + fields[i].getColumn().getName() + " "
								   + "LIKE '%" + keyword + "%'");
				
				if (i < fields.length - 1) query = query.concat(" OR ");
				else query = query.concat(")");
			}
		}
		
		//retrieve corresponding rows
		return getRows(pullableClass, query);
	}
	
	private static String getOverallQuery(String tableName) {
		return "SELECT * "
			 + "FROM " + tableName + " " + tableName.charAt(0) + " "
			 + "WHERE user_id = '" + userKey + "'";
	}
}