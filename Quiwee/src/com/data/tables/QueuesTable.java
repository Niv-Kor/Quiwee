package com.data.tables;
import java.util.List;
import com.data.Pullable;
import javaNK.util.data.DataType;
import javaNK.util.data.MysqlColumn;

public enum QueuesTable implements Pullable
{
	USER_ID(new MysqlColumn("user_id", DataType.VARCHAR)),
	CLIENT_PHONE(new MysqlColumn("client_phone_number", DataType.VARCHAR)),
	SERVICE_NAME(new MysqlColumn("serive_name", DataType.VARCHAR)),
	PRICE(new MysqlColumn("price", DataType.DECIMAL)),
	START_TIME(new MysqlColumn("start_time", DataType.TIMESTAMP)),
	END_TIME(new MysqlColumn("end_time", DataType.TIMESTAMP));
	
	private MysqlColumn column;
	private List<Object> list;
	
	private QueuesTable(MysqlColumn column) {
		this.column = column;
	}
	
	public List<Object> getList() { return list; }
	
	public void setList(List<Object> l) { list = l; }

	@Override
	public String getTableName() { return "queues"; }

	@Override
	public MysqlColumn getColumn() { return column; }
	
	public static Pullable[] getValues() { return values(); }
}