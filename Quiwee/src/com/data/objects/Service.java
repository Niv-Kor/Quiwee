package com.data.objects;
import java.sql.SQLException;
import com.data.MysqlLoader;
import com.data.tables.ServicesTable;
import com.main.User;
import javaNK.util.data.MysqlRow;

public class Service extends MysqlRow
{
	public Service(String name, double price, boolean hourly) {
		super("services");
		
		setField(ServicesTable.NAME.getColumn(), name);
		setPrice(price);
		setHourlyPrice(hourly);
	}
	
	public Service(String name) throws SQLException {
		super("services");
		
		setField(ServicesTable.NAME.getColumn(), name);
		Object[][] row = MysqlLoader.getRows(ServicesTable.class, selectAllQuery());
		
		if (isInDatabase()) {
			setPrice((double) row[0][2]);
			setHourlyPrice((boolean) row[0][3]);
		}
		else throw new SQLException();
	}
	
	@Override
	protected void addFields() {
		addKeyField(ServicesTable.USER_ID.getColumn(), User.getKey());
		addKeyField(ServicesTable.NAME.getColumn(), null);
		addLiquidField(ServicesTable.PRICE.getColumn(), null);
		addLiquidField(ServicesTable.HOURLY_PRICE.getColumn(), null);
	}
	
	public void setPrice(double p) { setField(ServicesTable.PRICE.getColumn(), p); }
	
	public void setHourlyPrice(boolean flag) { setField(ServicesTable.HOURLY_PRICE.getColumn(), flag); }
	
	public String getName() { return (String) getField(ServicesTable.NAME.getColumn()); }
	
	public double getPrice() { return (double) getField(ServicesTable.PRICE.getColumn()); }
	
	public boolean isHourly() { return (boolean) getField(ServicesTable.HOURLY_PRICE.getColumn()); }
	
	public String getID() { return getName(); }
}