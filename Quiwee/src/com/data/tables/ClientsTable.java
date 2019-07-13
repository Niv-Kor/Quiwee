package com.data.tables;
import java.util.List;
import com.data.Pullable;
import javaNK.util.data.DataType;
import javaNK.util.data.MysqlColumn;

public enum ClientsTable implements Pullable
{
	USER_ID(new MysqlColumn("user_id", DataType.VARCHAR)),
	PHONE(new MysqlColumn("phone_number", DataType.VARCHAR)),
	NAME(new MysqlColumn("full_name", DataType.VARCHAR)),
	COUNTRY(new MysqlColumn("country", DataType.VARCHAR)),
	CITY(new MysqlColumn("city", DataType.VARCHAR)),
	STREET(new MysqlColumn("street", DataType.VARCHAR)),
	STREET_NUM(new MysqlColumn("st_num", DataType.INTEGER)),
	JOIN_DATE(new MysqlColumn("join_date", DataType.DATETIME));
	
	private MysqlColumn column;
	private List<Object> list;
	
	private ClientsTable(MysqlColumn column) {
		this.column = column;
	}
	
	public List<Object> getList() { return list; }
	
	public void setList(List<Object> l) { list = l; }

	@Override
	public String getTableName() { return "clients"; }

	@Override
	public MysqlColumn getColumn() { return column; }
	
	public static Pullable[] getValues() { return values(); }
}