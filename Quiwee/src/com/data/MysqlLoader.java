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
	
	public static Object[][] getRows(Class<? extends Pullable> pullableClass) throws SQLException {
		Pullable[] enumConstants = pullableClass.getEnumConstants();
		return getRows(pullableClass, getOverallQuery(enumConstants[0].getTableName()));
	}
	
	public static Object[][] getRows(Class<? extends Pullable> pullableClass, String query) throws SQLException {
		Pullable[] enumConstants = pullableClass.getEnumConstants();
		MysqlColumn[] columns = new MysqlColumn[enumConstants.length];
		
		for (int i = 0; i < columns.length; i++)
			columns[i] = enumConstants[i].getColumn();
		
		return MysqlModifier.getRows(query, columns);
	}
	
	private static String getOverallQuery(String tableName) {
		return "SELECT * "
			 + "FROM " + tableName + " "
			 + "WHERE user_id = '" + userKey + "'";
	}
}