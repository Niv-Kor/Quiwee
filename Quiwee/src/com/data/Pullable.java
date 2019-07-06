package com.data;
import java.sql.SQLException;
import java.util.List;
import javaNK.util.data.MysqlColumn;
import javaNK.util.data.MysqlModifier;

public interface Pullable
{
	List<Object> getList();
	void setList(List<Object> l);
	String getTableName();
	MysqlColumn getColumn();
	
	static Pullable[] getValues() { return new Pullable[0]; }
	
	static Object[][] getRows(String query) throws SQLException {
		MysqlColumn[] columns = new MysqlColumn[getValues().length];
		
		for (int i = 0; i < columns.length; i++)
			columns[i] = getValues()[i].getColumn();

		return MysqlModifier.getRows(query, columns);
	}
}