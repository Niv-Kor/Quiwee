package com.data.objects;
import java.sql.SQLException;
import java.time.LocalDateTime;
import com.data.MysqlLoader;
import com.data.tables.ClientsTable;
import com.main.User;
import javaNK.util.data.MysqlRow;
import javaNK.util.real_time.TimeStampConverter;

public class Client extends MysqlRow
{
	public Client(String name, String phone, String country, String city, String street, Integer stNum) {
		super("clients");
		
		setName(name);
		setField(ClientsTable.PHONE.getColumn(), phone);
		setCountry(country);
		setCity(city);
		setStreet(street);
		setStreetNum(stNum);
		setJoinDate(LocalDateTime.now());
	}
	
	public Client(String phone) throws SQLException {
		super("clients");
		setField(ClientsTable.PHONE.getColumn(), phone);
		
		if (isInDatabase()) {
			Object[][] rows = MysqlLoader.getRows(ClientsTable.class, selectAllQuery());
			
			setName((String) rows[0][2]);
			setCountry((String) rows[0][3]);
			setCity((String) rows[0][4]);
			setStreet((String) rows[0][5]);
			setStreetNum((int) rows[0][6]);
			setJoinDate(TimeStampConverter.toLocalDateTime((String) rows[0][7]));
		}
		else throw new SQLException();
	}
	
	@Override
	protected void addFields() {
		addKeyField(ClientsTable.USER_ID.getColumn(), User.getKey());
		addKeyField(ClientsTable.PHONE.getColumn(), null);
		addLiquidField(ClientsTable.NAME.getColumn(), null);
		addLiquidField(ClientsTable.COUNTRY.getColumn(), null);
		addLiquidField(ClientsTable.CITY.getColumn(), null);
		addLiquidField(ClientsTable.STREET.getColumn(), null);
		addLiquidField(ClientsTable.STREET_NUM.getColumn(), null);
		addLiquidField(ClientsTable.JOIN_DATE.getColumn(), null);
	}
	
	public void setName(String n) { setField(ClientsTable.NAME.getColumn(), n); }

	public void setCountry(String c) { setField(ClientsTable.COUNTRY.getColumn(), c); }
	
	public void setCity(String c) { setField(ClientsTable.CITY.getColumn(), c); }
	
	public void setStreet(String s) { setField(ClientsTable.STREET.getColumn(), s); }
	
	public void setStreetNum(Integer num) { setField(ClientsTable.STREET_NUM.getColumn(), num); }
	
	public void setJoinDate(LocalDateTime t) {
		setField(ClientsTable.JOIN_DATE.getColumn(), TimeStampConverter.toString(t));
	}
	
	public String getName() { return (String) getField(ClientsTable.NAME.getColumn()); }
	
	public String getPhoneNumber() { return (String) getField(ClientsTable.PHONE.getColumn()); }
	
	public String getCountry() { return (String) getField(ClientsTable.COUNTRY.getColumn()); }
	
	public String getCity() { return (String) getField(ClientsTable.CITY.getColumn()); }
	
	public String getStreet() { return (String) getField(ClientsTable.STREET.getColumn()); }
	
	public int getStreetNum() { return (int) getField(ClientsTable.STREET_NUM.getColumn()); }
	
	public LocalDateTime getJoinDate() {
		return TimeStampConverter.toLocalDateTime((String) getField(ClientsTable.JOIN_DATE.getColumn()));
	}
	
	public String getID() { return getPhoneNumber(); }
}