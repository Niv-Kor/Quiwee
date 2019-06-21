package com.content.service;
import java.sql.SQLException;
import com.content.MysqlDataRow;
import com.main.User;
import javaNK.util.data.MysqlModifier;

public class Service extends MysqlDataRow
{
	private String name;
	private double price;
	private boolean hourly;
	
	public Service(String name, double price, boolean hourly) {
		this.name = new String(name);
		this.price = price;
		this.hourly = hourly;
		save();
	}
	
	public Service(String name) throws SQLException {
		this.name = new String(name);
		String query = selectAllQuery();
		
		if (isInDatabase()) {
			this.price = MysqlModifier.readDECIMAL(query, "price");
			this.hourly = MysqlModifier.readBOOLEAN(query, "hourly");
		}
		else throw new SQLException();
	}
	
	public void setPrice(double p) { price = p; }
	
	public void setHourlyPrice(boolean flag) { hourly = flag; }
	
	public String getName() { return name; }
	
	public double getPrice() { return price; }
	
	public boolean isHourly() { return hourly; }
	
	public String getID() { return getName(); }

	@Override
	protected String tableName() { return "services"; }

	@Override
	protected void addFields() {
		addKeyField(new DataField() {
			@Override
			public String mysqlName() { return "user_id"; }

			@Override
			public Object classMember() { return User.getKey(); }
		});
		
		addKeyField(new DataField() {
			@Override
			public String mysqlName() { return "name"; }

			@Override
			public Object classMember() { return name; }
		});
		
		addLiquidField(new DataField() {
			@Override
			public String mysqlName() { return "price"; }

			@Override
			public Object classMember() { return price; }
		});
		
		addLiquidField(new DataField() {
			@Override
			public String mysqlName() { return "hourly"; }

			@Override
			public Object classMember() { return hourly; }
		});
	}
}