package com.content.client;
import java.sql.Date;
import java.time.LocalDateTime;
import com.content.Saveable;
import com.database.SQLModifier;
import com.main.User;

public class Client implements Saveable {
	private String name, phone;
	private Date joinDate;
	private Address address;
	private boolean saved;
	
	public Client(String name, String phone, Address address) {
		init(name, phone, address, null);
		save();
	}
	
	//import client from db
	public Client(String name, String phone) {
		String query = "SELECT * "
					 + "FROM clients c "
					 + "WHERE c.user_key = '" + User.getKey() + "' "
			 		 + "AND c.full_name = '"+ name + "' "
			 		 + "AND c.phone_number = '"+ phone + "';";
		
		String country = SQLModifier.readVARCHAR(query, "country");
		String city = SQLModifier.readVARCHAR(query, "city");
		String street = SQLModifier.readVARCHAR(query, "street");
		int streetNum = SQLModifier.readINT(query, "st_num");
		Date joined = SQLModifier.readDATE(query, "join_date");
		
		init(name, phone, new Address(country, city, street, streetNum), joined);
		saved = true;
	}
	
	private void init(String name, String phone, Address address, Date joinDate) {
		this.name = new String(name);
		this.phone = new String(phone);
		this.address = address;
		this.joinDate = (joinDate != null) ? joinDate : Date.valueOf(LocalDateTime.now().toLocalDate());
	}
	
	public void save() {
		String query;

		if (!saved) {
			query = "INSERT INTO client(user_key, full_name, phone_number, country, city, street, st_num, join_date) "
			      + "VALUES ( "
			      + User.getKey() + ", " + name + ", " + phone + ", "
			      + address.getCountry() + ", " + address.getCity() + ", "
			      + address.getStreet() + ", " + address.getStreetNum() + ", " + joinDate + ");";
		}
		else {
			query = "UPDATE queues "
			      + "SET full_name = '" + name + "', "
			      + "phone_num = '" + phone + "', "
	    		  + "country = '" + address.getCountry() + "', "
				  + "city = '" + address.getCity() + "', "
				  + "street = '" + address.getStreet() + "', "
				  + "st_num = " + address.getStreetNum() + ", "
			      + "join_date = " + joinDate + ";";
		}
		
		SQLModifier.write(query);
		saved = true;		
	}
	
	public void setName(String n) {
		boolean changed = !name.equals(n);
		name = new String(n);
		if (changed) save();
	}

	public void setPhoneNumber(String p) {
		boolean changed = !phone.equals(p);
		name = new String(p);
		if (changed) save();
	}

	public void setTime(Address a) {
		boolean changed = !address.equals(a);
		address = a;
		if (changed) save();
	}
	
	public String getName() { return name; }
	public String getPhoneNumber() { return phone; }
	public Address getAddress() { return address; }
	public Date getJoinDate() { return joinDate; }
	public String getID() { return getPhoneNumber(); }
}