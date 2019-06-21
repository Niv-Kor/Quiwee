package com.content.client;
import java.sql.SQLException;
import java.sql.Timestamp;
import com.main.User;
import javaNK.util.data.MysqlDataRow;
import javaNK.util.data.MysqlModifier;

public class Client extends MysqlDataRow
{
	private String name, phoneNum;
	private Timestamp joinDate;
	private String country, city, street;
	private Integer streetNum;
	
	public Client(String name, String phone, String country, String city, String street, Integer stNum) {
		this.name = new String(name);
		this.phoneNum = new String(phone);
		this.country = new String(country);
		this.city = new String(city);
		this.street = new String(street);
		this.streetNum = stNum;
		this.joinDate = new Timestamp(System.currentTimeMillis());
		save();
	}
	
	public Client(String phone) throws SQLException {
		this.phoneNum = new String(phone);
		String query = selectAllQuery();
		
		if (isInDatabase()) {
			this.name = MysqlModifier.readVARCHAR(query, "full_name");
			this.country = MysqlModifier.readVARCHAR(query, "country");
			this.city = MysqlModifier.readVARCHAR(query, "city");
			this.street = MysqlModifier.readVARCHAR(query, "street");
			this.streetNum = MysqlModifier.readINT(query, "st_num");
			this.joinDate = MysqlModifier.readTIMESTAMP(query, "join_date");
		}
		else throw new SQLException();
	}
	
	public void setName(String n) { name = n; }

	public void setPhoneNumber(String p) { phoneNum = p; }

	public void setCounter(String c) { country = c; }
	
	public void setCity(String c) { city = c; }
	
	public void setStreet(String s) { street = s; }
	
	public void setStreetNum(Integer num) { streetNum = num; }
	
	public String getName() { return name; }
	
	public String getPhoneNumber() { return phoneNum; }
	
	public String getCounter() { return country; }
	
	public String getCity() { return city; }
	
	public String getStreet() { return street; }
	
	public Integer getStreetNum() { return streetNum; }
	
	public Timestamp getJoinDate() { return joinDate; }
	
	public String getID() { return getPhoneNumber(); }

	@Override
	protected String tableName() { return "clients"; }

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
			public String mysqlName() { return "phone_number"; }

			@Override
			public Object classMember() { return phoneNum; }
		});
		
		addLiquidField(new DataField() {
			@Override
			public String mysqlName() { return "full_name"; }

			@Override
			public Object classMember() { return name; }
		});
		
		addLiquidField(new DataField() {
			@Override
			public String mysqlName() { return "country"; }

			@Override
			public Object classMember() { return country; }
		});
		
		addLiquidField(new DataField() {
			@Override
			public String mysqlName() { return "city"; }

			@Override
			public Object classMember() { return city; }
		});

		addLiquidField(new DataField() {
			@Override
			public String mysqlName() { return "street"; }

			@Override
			public Object classMember() { return street; }
		});
		
		addLiquidField(new DataField() {
			@Override
			public String mysqlName() { return "st_num"; }

			@Override
			public Object classMember() { return streetNum; }
		});
		
		addLiquidField(new DataField() {
			@Override
			public String mysqlName() { return "join_date"; }

			@Override
			public Object classMember() { return joinDate; }
		});
	}
}