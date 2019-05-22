package com.content.client;

public class Address {
	private String country, city, street;
	private int streetNum;
	
	public Address(String country, String city, String street, int num) {
		this.country = new String(country);
		this.city = new String(city);
		this.street = new String(street);
		this.streetNum = num;
	}
	 
	public void setCountry(String c) { country = c; }
	public void setCity(String c) { city = c; }
	public void setStreet(String s) { street = s; } 
	public void setStreetNum(int sn) { streetNum = sn; }
	public String getCountry() { return country; }
	public String getCity() { return city; } 
	public String getStreet() { return street; }
	public int getStreetNum() { return streetNum; }
}